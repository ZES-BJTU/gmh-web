package com.zes.squad.gmh.web.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.google.common.base.Strings;
import com.zes.squad.gmh.common.converter.CommonConverter;
import com.zes.squad.gmh.common.exception.ErrorCodeEnum;
import com.zes.squad.gmh.web.common.JsonResult;
import com.zes.squad.gmh.web.entity.dto.StaffDto;
import com.zes.squad.gmh.web.entity.param.StaffParam;
import com.zes.squad.gmh.web.entity.po.StaffPo;
import com.zes.squad.gmh.web.entity.vo.StaffVo;
import com.zes.squad.gmh.web.service.StaffService;

@RequestMapping("/staff")
@Controller
public class StaffController extends BaseController {

    @Autowired
    private StaffService staffService;

    @RequestMapping("/login")
    @ResponseBody
    public JsonResult<?> doLoginWithEmail(String account, String password) {
  //  	account = "test";
  //      password = "test";
        if (Strings.isNullOrEmpty(account)) {
            return JsonResult.fail(ErrorCodeEnum.BUSINESS_EXCEPTION_INVALID_PARAMETERS.getCode(), "用户名不能为空");
        }
        if (Strings.isNullOrEmpty(password)) {
            return JsonResult.fail(ErrorCodeEnum.BUSINESS_EXCEPTION_INVALID_PARAMETERS.getCode(), "密码不能为空");
        }
        
        StaffDto staffDto = staffService.loginWithEmail(account, password);
        if (staffDto == null) {
            return JsonResult.fail(ErrorCodeEnum.BUSINESS_EXCEPTION.getCode(), "登录未知错误");
        }
        StaffVo staffVo = CommonConverter.map(staffDto, StaffVo.class);
        return JsonResult.success(staffVo);
    }
    @RequestMapping("/insert")
    @ResponseBody
    public JsonResult<?> insert(StaffParam param){
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

}
