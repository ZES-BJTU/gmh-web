package com.zes.squad.gmh.web.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.zes.squad.gmh.common.converter.CommonConverter;
import com.zes.squad.gmh.common.exception.ErrorCodeEnum;
import com.zes.squad.gmh.common.exception.GmhException;
import com.zes.squad.gmh.common.util.EncryptUtils;
import com.zes.squad.gmh.web.entity.dto.StaffDto;
import com.zes.squad.gmh.web.entity.po.StaffPo;
import com.zes.squad.gmh.web.mapper.StaffMapper;
import com.zes.squad.gmh.web.service.StaffService;

@Service("staffService")
public class StaffServiceImpl implements StaffService {

    @Autowired
    private StaffMapper staffMapper;

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
        StaffDto staffDto = CommonConverter.map(staffPo, StaffDto.class);
        return staffDto;
    }

    @Override
    public StaffDto queryStaffByTokeb(String token) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public void changePassword(String originalPassword, String newPassword) {
        // TODO Auto-generated method stub
        
    }

    @Override
    public void logout(Long id) {
        // TODO Auto-generated method stub
        
    }

    @Override
    public void sendAuthCode(String email) {
        // TODO Auto-generated method stub
        
    }

    @Override
    public void validateAuthCode(String email, String authCode) {
        // TODO Auto-generated method stub
        
    }

}
