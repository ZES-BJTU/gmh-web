package com.zes.squad.gmh.web.controller;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;

import com.google.common.base.Strings;
import com.zes.squad.gmh.common.constant.RequestConsts;
import com.zes.squad.gmh.common.exception.ErrorCodeEnum;
import com.zes.squad.gmh.common.exception.GmhException;
import com.zes.squad.gmh.web.context.ThreadContext;
import com.zes.squad.gmh.web.entity.dto.StaffDto;
import com.zes.squad.gmh.web.service.StaffService;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class BaseController {

    @Autowired
    private HttpServletRequest request;
    @Autowired
    private StaffService       staffService;

    public StaffDto getStaff() {
        String token = request.getHeader(RequestConsts.X_TOKEN);
        if (Strings.isNullOrEmpty(token)) {
            log.error("获取用户token失败, token is {}.", token);
            throw new GmhException(ErrorCodeEnum.WEB_EXCEPTION.getCode(), "获取用户信息失败");
        }
        StaffDto staff = staffService.queryStaffByToken(token);
        return staff;
    }

    public void unBind() {
        ThreadContext.threadLocal.remove();
    }

}
