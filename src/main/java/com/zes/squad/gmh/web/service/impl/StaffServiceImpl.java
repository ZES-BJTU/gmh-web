package com.zes.squad.gmh.web.service.impl;

import java.util.Date;
import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.github.pagehelper.PageInfo;
import com.google.common.base.Preconditions;
import com.google.common.base.Strings;
import com.zes.squad.gmh.common.constant.RedisConsts;
import com.zes.squad.gmh.common.converter.CommonConverter;
import com.zes.squad.gmh.common.entity.PagedList;
import com.zes.squad.gmh.common.exception.ErrorCodeEnum;
import com.zes.squad.gmh.common.exception.GmhException;
import com.zes.squad.gmh.common.util.EncryptUtils;
import com.zes.squad.gmh.web.cache.RedisComponent;
import com.zes.squad.gmh.web.entity.dto.StaffDto;
import com.zes.squad.gmh.web.entity.dto.StockTypeDto;
import com.zes.squad.gmh.web.entity.po.StaffPo;
import com.zes.squad.gmh.web.entity.po.StaffTokenPo;
import com.zes.squad.gmh.web.entity.po.StockTypePo;
import com.zes.squad.gmh.web.entity.vo.StaffVo;
import com.zes.squad.gmh.web.mail.MailHelper;
import com.zes.squad.gmh.web.mail.MailParams;
import com.zes.squad.gmh.web.mapper.StaffMapper;
import com.zes.squad.gmh.web.mapper.StaffTokenMapper;
import com.zes.squad.gmh.web.service.StaffService;

@Service("staffService")
public class StaffServiceImpl implements StaffService {

    private static final String CACHE_KEY_TOKEN_PREFIX     = "_cache_key_token_prefix_%s";
    private static final String CACHE_KEY_AUTH_CODE_PREFIX = "_cache_key_auth_code_prefix_%s";

    @Autowired
    private StaffMapper         staffMapper;
    @Autowired
    private StaffTokenMapper    staffTokenMapper;
    @Autowired
    private RedisComponent      redisComponent;

    @Override
    public StaffDto loginWithEmail(String account, String password) {
        StaffPo staffPo = staffMapper.selectByEmail(account);
        if (staffPo == null) {
            throw new GmhException(ErrorCodeEnum.BUSINESS_EXCEPTION_ENTITY_NOT_FOUND.getCode(), "未找到用户");
        }
        String encryptPassword = EncryptUtils.MD5(account + staffPo.getSalt() + password);
        if (!staffPo.getPassword().equals(encryptPassword)) {
            throw new GmhException(ErrorCodeEnum.BUSINESS_EXCEPTION_INVALID_PARAMETERS.getCode(), "密码输入错误");
        }
        boolean isCacheValid = redisComponent.isValid();
        if (isCacheValid) {
            StaffTokenPo staffTokenPo = staffTokenMapper.selectByStaffId(staffPo.getId());
            if (staffTokenPo != null) {
                String cacheKey = String.format(CACHE_KEY_TOKEN_PREFIX, staffTokenPo.getToken());
                redisComponent.delete(cacheKey);
            }
        }
        String token = UUID.randomUUID().toString().replaceAll("\\-", "");
        staffTokenMapper.insertOrUpdateToken(staffPo.getId(), token, new Date());
        StaffDto staffDto = CommonConverter.map(staffPo, StaffDto.class);
        staffDto.setPassword(null);
        staffDto.setSalt(null);
        staffDto.setToken(token);
        return staffDto;
    }

    @Override
    public int insert(StaffDto dto) {
        String salt = UUID.randomUUID().toString().replaceAll("\\-", "");
        String password = EncryptUtils.MD5(dto.getEmail() + salt + "111111");
        dto.setSalt(salt);
        dto.setPassword(password);
        StaffPo po = CommonConverter.map(dto, StaffPo.class);
        int i = staffMapper.insert(po);
        return i;
    }

    @Override
    public StaffDto queryStaffByToken(String token) {
        boolean isCacheValid = redisComponent.isValid();
        StaffDto staff = null;
        if (!isCacheValid) {
            staff = queryStaffWithToken(token);
            return staff;
        }
        String cacheKey = String.format(CACHE_KEY_TOKEN_PREFIX, token);
        staff = redisComponent.get(cacheKey, StaffDto.class);
        if (staff != null) {
            return staff;
        }
        staff = queryStaffWithToken(token);
        redisComponent.put(cacheKey, staff, RedisConsts.THIRTY_MINUTE);
        return staff;
    }

    @Override
    public void changePassword(Long id, String originalPassword, String newPassword) {
        StaffPo staffPo = staffMapper.selectById(id);
        if (staffPo == null) {
            throw new GmhException(ErrorCodeEnum.BUSINESS_EXCEPTION_ENTITY_NOT_FOUND, "未找到用户");
        }
        String oldEncryptPassword = EncryptUtils.MD5(staffPo.getEmail() + staffPo.getSalt() + originalPassword);
        if (!staffPo.getPassword().equals(oldEncryptPassword)) {
            throw new GmhException(ErrorCodeEnum.BUSINESS_EXCEPTION_INVALID_PARAMETERS, "原密码输入错误");
        }
        String newEncryptPassword = EncryptUtils.MD5(staffPo.getEmail() + staffPo.getSalt() + newPassword);
        staffMapper.updatePassword(id, newEncryptPassword);
    }

    @Override
    public void logout(Long id) {
        StaffTokenPo staffTokenPo = staffTokenMapper.selectByStaffId(id);
        if (staffTokenPo == null) {
            throw new GmhException(ErrorCodeEnum.BUSINESS_EXCEPTION_ENTITY_NOT_FOUND, "未找到用户");
        }
        String token = staffTokenPo.getToken();
        //清除redis
        String cacheKey = String.format(CACHE_KEY_TOKEN_PREFIX, token);
        redisComponent.delete(cacheKey);
        //清除数据库token
        staffTokenMapper.deleteByToken(token);
    }

    @Override
    public void sendAuthCode(String email) {
        StaffPo staffPo = staffMapper.selectByEmail(email);
        if (staffPo == null) {
            throw new GmhException(ErrorCodeEnum.BUSINESS_EXCEPTION_ENTITY_NOT_FOUND, "未找到用户");
        }
        String authCode = generateRandomAuthCode(6);
        boolean isCacheValid = redisComponent.isValid();
        if (!isCacheValid) {
            throw new GmhException(ErrorCodeEnum.CACHE_EXCEPTION, "忘记密码服务暂时不可用");
        }
        MailParams mailParams = new MailParams();
        mailParams.setReceiversTO(new String[] { email });
        mailParams.setSubject("光美焕系统密码重置");
        mailParams.setContentType("text/plain; charset=utf-8");
        mailParams.setContent("尊敬的用户:\r\n您好! 您当前正在使用光美焕忘记密码服务, 请将收到的验证码填写到系统指定位置, 验证码有效期10分钟.\r\n验证码: " + authCode);
        MailHelper.sendTextEmail(mailParams);
        redisComponent.put(String.format(CACHE_KEY_AUTH_CODE_PREFIX, email), authCode, RedisConsts.TEN_MINUTE);
    }

    @Override
    public void validateAuthCode(String email, String authCode) {
        StaffPo staffPo = staffMapper.selectByEmail(email);
        if (staffPo == null) {
            throw new GmhException(ErrorCodeEnum.BUSINESS_EXCEPTION_ENTITY_NOT_FOUND, "获取用户信息失败");
        }
        boolean isCacheValid = redisComponent.isValid();
        if (!isCacheValid) {
            throw new GmhException(ErrorCodeEnum.CACHE_EXCEPTION, "验证码验证失败, 服务暂时不可用");
        }
        String cacheKey = String.format(CACHE_KEY_AUTH_CODE_PREFIX, email);
        String redisAuthCode = redisComponent.get(cacheKey, String.class);
        if (Strings.isNullOrEmpty(redisAuthCode)) {
            throw new GmhException(ErrorCodeEnum.CACHE_EXCEPTION, "验证码已过期, 请重新获取验证码");
        }
        if (!redisAuthCode.equals(authCode)) {
            throw new GmhException(ErrorCodeEnum.CACHE_EXCEPTION, "验证码错误, 请重新输入验证码");
        }
        redisComponent.delete(cacheKey);
    }

    private String generateRandomAuthCode(int length) {
        Preconditions.checkArgument(length > 0);
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < length; i++) {
            builder.append(String.valueOf((int) (Math.random() * 10)));
        }
        return builder.toString();
    }

    private StaffDto queryStaffWithToken(String token) {
        StaffTokenPo staffTokenPo = staffTokenMapper.selectByToken(token);
        if (staffTokenPo == null) {
            throw new GmhException(ErrorCodeEnum.WEB_EXCEPTION_AUTH_FAIL, "登录已过期, 请重新登录");
        }
        StaffPo staffPo = staffMapper.selectById(staffTokenPo.getStaffId());
        if (staffPo == null) {
            throw new GmhException(ErrorCodeEnum.BUSINESS_EXCEPTION_ENTITY_NOT_FOUND,
                    "根据id[" + staffTokenPo.getStaffId() + "]获取用户信息失败");
        }
        StaffDto staffDto = CommonConverter.map(staffPo, StaffDto.class);
        staffDto.setPassword(null);
        staffDto.setSalt(null);
        staffDto.setToken(token);
        return staffDto;
    }

	@Override
	public PagedList<StaffVo> search(Integer pageNum, Integer pageSize, String searchString) {
		List<StaffVo> voList = staffMapper.search(searchString);
		 PageInfo<StaffVo> info = new PageInfo<>(voList);
	        PagedList<StaffVo> pagedList = CommonConverter.mapPageList(
	                PagedList.newMe(info.getPageNum(), info.getPageSize(), info.getTotal(), voList),
	                StaffVo.class);
		return pagedList;
	}

}
