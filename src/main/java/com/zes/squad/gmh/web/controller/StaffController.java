package com.zes.squad.gmh.web.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.google.common.base.Strings;
import com.zes.squad.gmh.common.exception.ErrorCodeEnum;
import com.zes.squad.gmh.common.exception.GmhException;
import com.zes.squad.gmh.web.common.CheckResult;
import com.zes.squad.gmh.web.common.JsonResult;

@RequestMapping("/staff")
@Controller
public class StaffController extends BaseController {

    @RequestMapping("/login")
    @ResponseBody
    public JsonResult<?> doLogin(String account, String password) {
        CheckResult result = check(account);
        if (!result.isPassed()) {
            return JsonResult.fail(ErrorCodeEnum.BUSINESS_EXCEPTION_INVALID_PARAMETERS.getCode(), result.getError());
        }
        throw new GmhException(ErrorCodeEnum.BUSINESS_EXCEPTION, "登录出错");
    }

    private CheckResult check(String account) {
        if (Strings.isNullOrEmpty(account)) {
            return CheckResult.fail("1111");
        }
        return CheckResult.success();
    }

}
