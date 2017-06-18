package com.zes.squad.gmh.web.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.google.common.base.Strings;
import com.zes.squad.gmh.common.exception.ErrorCodeEnum;
import com.zes.squad.gmh.web.common.JsonResult;
import com.zes.squad.gmh.web.service.StaffService;

@RequestMapping("/staff")
@Controller
public class StaffController extends BaseController {

    @Autowired
    private StaffService staffService;

    @RequestMapping("/login")
    @ResponseBody
    public JsonResult<?> doLoginWithEmail(String account, String password) {
        if (Strings.isNullOrEmpty(account)) {
            return JsonResult.fail(ErrorCodeEnum.BUSINESS_EXCEPTION_INVALID_PARAMETERS.getCode(), "用户名不能为空");
        }
        if (Strings.isNullOrEmpty(password)) {
            return JsonResult.fail(ErrorCodeEnum.BUSINESS_EXCEPTION_INVALID_PARAMETERS.getCode(), "密码不能为空");
        }
        return null;
    }

}
