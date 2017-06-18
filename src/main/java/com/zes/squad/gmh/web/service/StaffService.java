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

}
