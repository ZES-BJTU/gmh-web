package com.zes.squad.gmh.web.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.google.common.base.Strings;
import com.zes.squad.gmh.common.converter.CommonConverter;
import com.zes.squad.gmh.common.exception.ErrorCodeEnum;
import com.zes.squad.gmh.web.common.JsonResult;
import com.zes.squad.gmh.web.entity.dto.StaffDto;
import com.zes.squad.gmh.web.entity.param.StaffParam;
import com.zes.squad.gmh.web.entity.vo.StaffVo;
import com.zes.squad.gmh.web.service.StaffService;

@RequestMapping("/staff")
@Controller
public class StaffController extends BaseController {

    @Autowired
    private StaffService staffService;

    @RequestMapping("/login")
    @ResponseBody
    public JsonResult<StaffVo> doLoginWithEmail(String account, String password) {
        if (Strings.isNullOrEmpty(account)) {
            return JsonResult.fail(ErrorCodeEnum.BUSINESS_EXCEPTION_INVALID_PARAMETERS.getCode(), "用户名不能为空");
        }
        if (Strings.isNullOrEmpty(password)) {
            return JsonResult.fail(ErrorCodeEnum.BUSINESS_EXCEPTION_INVALID_PARAMETERS.getCode(), "密码不能为空");
        }
        
        StaffDto staffDto = staffService.loginWithEmail(account, password);
        StaffVo staffVo = CommonConverter.map(staffDto, StaffVo.class);
        return JsonResult.success(staffVo);
    }
    @RequestMapping("/insert")
    @ResponseBody
    public JsonResult<Integer> insert(StaffParam param){
    	if (Strings.isNullOrEmpty(param.getEmail())) {
            return JsonResult.fail(ErrorCodeEnum.BUSINESS_EXCEPTION_INVALID_PARAMETERS.getCode(), "用户名不能为空");
        }
        if (Strings.isNullOrEmpty(param.getPassword())) {
            return JsonResult.fail(ErrorCodeEnum.BUSINESS_EXCEPTION_INVALID_PARAMETERS.getCode(), "密码不能为空");
        }
        StaffDto dto = CommonConverter.map(param, StaffDto.class);
        int i = staffService.insert(dto);
    	return JsonResult.success(i);
    }

    @RequestMapping(path = "/changePassword", method = RequestMethod.POST)
    @ResponseBody
    public JsonResult<Void> doChangePassword(String originalPassword, String newPassword) {
        if (Strings.isNullOrEmpty(originalPassword)) {
            return JsonResult.fail(ErrorCodeEnum.BUSINESS_EXCEPTION_INVALID_PARAMETERS.getCode(), "原密码不能为空");
        }
        if (Strings.isNullOrEmpty(newPassword)) {
            return JsonResult.fail(ErrorCodeEnum.BUSINESS_EXCEPTION_INVALID_PARAMETERS.getCode(), "新密码不能为空");
        }
        if (originalPassword.equals(newPassword)) {
            return JsonResult.fail(ErrorCodeEnum.BUSINESS_EXCEPTION_INVALID_PARAMETERS.getCode(), "新密码不能和原密码相同");
        }
        StaffDto staff = getStaff();
        staffService.changePassword(staff.getId(), originalPassword, newPassword);
        return JsonResult.success();
    }

    @RequestMapping("/logout")
    @ResponseBody
    public JsonResult<Void> doLogout() {
        return null;
    }

    @RequestMapping("/getAuthCode")
    @ResponseBody
    public JsonResult<Void> doSendAuthCode(String email) {
        return null;
    }

    @RequestMapping(path = "/validateAuthCode", method = RequestMethod.POST)
    @ResponseBody
    public JsonResult<Void> doValidateAuthCode(String email, String authCode) {
        return null;
    }

}
