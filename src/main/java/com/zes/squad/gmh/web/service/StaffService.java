package com.zes.squad.gmh.web.service;

import com.zes.squad.gmh.web.entity.dto.StaffDto;

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
     * 根据token查询用户信息
     * 
     * @param token
     * @return
     */
    StaffDto queryStaffByTokeb(String token);

    /**
     * 修改密码
     * 
     * @param originalPassword
     * @param newPassword
     */
    void changePassword(String originalPassword, String newPassword);

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

}
