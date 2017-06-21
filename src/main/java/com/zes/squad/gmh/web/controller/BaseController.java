package com.zes.squad.gmh.web.controller;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;

import com.google.common.base.Strings;
import com.zes.squad.gmh.common.constant.RequestConsts;
import com.zes.squad.gmh.common.exception.ErrorCodeEnum;
import com.zes.squad.gmh.common.exception.GmhException;
import com.zes.squad.gmh.web.entity.dto.StaffDto;
import com.zes.squad.gmh.web.service.StaffService;

public class BaseController {

    @Autowired
    private StaffService       staffService;
    @Autowired
    private HttpServletRequest request;

    public StaffDto getStaff() {
        String token = request.getHeader(RequestConsts.X_TOKEN);
        if (Strings.isNullOrEmpty(token)) {
            throw new GmhException(ErrorCodeEnum.BUSINESS_EXCEPTION, "获取token失败");
        }
        StaffDto staff = staffService.queryStaffByTokeb(token);
        return staff;
    }

}
