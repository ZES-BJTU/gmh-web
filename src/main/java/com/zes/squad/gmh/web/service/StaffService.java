package com.zes.squad.gmh.web.service;

import com.zes.squad.gmh.common.entity.PagedList;
import com.zes.squad.gmh.web.entity.dto.StaffDto;
import com.zes.squad.gmh.web.entity.dto.StockTypeDto;
import com.zes.squad.gmh.web.entity.vo.StaffVo;

public interface StaffService {

    /**
     * 登录
     * 
     * @param account
     * @param password
     * @return
     */
    StaffDto loginWithEmail(String account, String password);

    /**
     * 新增
     * 
     * @param dto
     * @return
     */
	int insert(StaffDto dto);
    

    /**
     * 根据token查询用户信息
     * 
     * @param token
     * @return
     */
    StaffDto queryStaffByToken(String token);

    /**
     * 修改密码
     * 
     * @param id
     * @param originalPassword
     * @param newPassword
     */
    void changePassword(Long id, String originalPassword, String newPassword);

    /**
     * 登出
     * 
     * @param id
     */
    void logout(Long id);

    /**
     * 发送验证码
     * 
     * @param email
     */
    void sendAuthCode(String email);

    /**
     * 检验验证码
     * 
     * @param email
     * @param authCode
     */
    void validateAuthCode(String email, String authCode);

    PagedList<StaffVo> search(Integer pageNum, Integer pageSize, String searchString);
    
    int update(StaffDto dto);
}
