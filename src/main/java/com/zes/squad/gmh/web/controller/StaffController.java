package com.zes.squad.gmh.web.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.google.common.base.Strings;
import com.zes.squad.gmh.common.converter.CommonConverter;
import com.zes.squad.gmh.common.entity.PagedList;
import com.zes.squad.gmh.common.exception.ErrorCodeEnum;
import com.zes.squad.gmh.web.common.JsonResult;
import com.zes.squad.gmh.web.entity.dto.StaffDto;
import com.zes.squad.gmh.web.entity.dto.StockTypeDto;
import com.zes.squad.gmh.web.entity.param.StaffParam;
import com.zes.squad.gmh.web.entity.vo.StaffVo;
import com.zes.squad.gmh.web.entity.vo.StockTypeVo;
import com.zes.squad.gmh.web.service.StaffService;

@RequestMapping("/staff")
@Controller
public class StaffController extends BaseController {

    @Autowired
    private StaffService staffService;

    @RequestMapping(path = "/login", method = RequestMethod.POST)
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

    @RequestMapping("/search")
	@ResponseBody
	public JsonResult<?> search(Integer pageNum, Integer pageSize,String searchString){
		PagedList<StaffVo> voList = staffService.search(pageNum, pageSize,searchString);
        
		
		return JsonResult.success(voList);
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

    @RequestMapping(path = "/logout", method = RequestMethod.GET)
    @ResponseBody
    public JsonResult<Void> doLogout() {
        StaffDto staff = getStaff();
        staffService.logout(staff.getId());
        unBind();
        return JsonResult.success();
    }

    @RequestMapping(path = "/info", method = RequestMethod.GET)
    @ResponseBody
    public JsonResult<StaffVo> doGetStaffInfo() {
        StaffDto staff = getStaff();
        StaffVo staffVo = CommonConverter.map(staff, StaffVo.class);
        return JsonResult.success(staffVo);
    }

    @RequestMapping(path = "/getAuthCode", method = RequestMethod.GET)
    @ResponseBody
    public JsonResult<Void> doSendAuthCode(String email) {
        if (Strings.isNullOrEmpty(email)) {
            return JsonResult.fail(ErrorCodeEnum.BUSINESS_EXCEPTION_INVALID_PARAMETERS.getCode(), "邮箱不能为空");
        }
        staffService.sendAuthCode(email);
        return JsonResult.success();
    }

    @RequestMapping(path = "/validateAuthCode", method = RequestMethod.POST)
    @ResponseBody
    public JsonResult<Void> doValidateAuthCode(String email, String authCode) {
        if (Strings.isNullOrEmpty(email)) {
            return JsonResult.fail(ErrorCodeEnum.BUSINESS_EXCEPTION_INVALID_PARAMETERS.getCode(), "邮箱不能为空");
        }
        if (Strings.isNullOrEmpty(authCode)) {
            return JsonResult.fail(ErrorCodeEnum.BUSINESS_EXCEPTION_INVALID_PARAMETERS.getCode(), "验证码不能为空");
        }
        staffService.validateAuthCode(email, authCode);
        return JsonResult.success();
    }

    @RequestMapping("/insert")
    @ResponseBody
    public JsonResult<Integer> insert(StaffParam param) {
        if (Strings.isNullOrEmpty(param.getEmail())) {
            return JsonResult.fail(ErrorCodeEnum.BUSINESS_EXCEPTION_INVALID_PARAMETERS.getCode(), "用户名不能为空");
        }
        StaffDto dto = CommonConverter.map(param, StaffDto.class);
        int i = staffService.insert(dto);
        return JsonResult.success(i);
    }

}
