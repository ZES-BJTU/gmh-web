package com.zes.squad.gmh.web.controller;

import static com.zes.squad.gmh.web.helper.CheckHelper.isValidPageNum;
import static com.zes.squad.gmh.web.helper.CheckHelper.isValidPageSize;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.google.common.base.Strings;
import com.google.common.collect.Lists;
import com.zes.squad.gmh.common.converter.CommonConverter;
import com.zes.squad.gmh.common.entity.PagedList;
import com.zes.squad.gmh.common.enums.StaffLevelEnum;
import com.zes.squad.gmh.common.exception.ErrorCodeEnum;
import com.zes.squad.gmh.common.exception.ErrorMessage;
import com.zes.squad.gmh.common.util.EnumUtils;
import com.zes.squad.gmh.web.common.JsonResult;
import com.zes.squad.gmh.web.entity.dto.StaffDto;
import com.zes.squad.gmh.web.entity.param.StaffParams;
import com.zes.squad.gmh.web.entity.vo.StaffLevelVo;
import com.zes.squad.gmh.web.entity.vo.StaffVo;
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
            return JsonResult.fail(ErrorCodeEnum.BUSINESS_EXCEPTION_INVALID_PARAMETERS.getCode(),
                    ErrorMessage.accountIsNull);
        }
        if (Strings.isNullOrEmpty(password)) {
            return JsonResult.fail(ErrorCodeEnum.BUSINESS_EXCEPTION_INVALID_PARAMETERS.getCode(),
                    ErrorMessage.passwordIsNull);
        }

        StaffDto staffDto = staffService.loginWithEmail(account, password);
        StaffVo staffVo = CommonConverter.map(staffDto, StaffVo.class);
        return JsonResult.success(staffVo);
    }

    @RequestMapping(path = "/changePassword", method = RequestMethod.POST)
    @ResponseBody
    public JsonResult<Void> doChangePassword(String originalPassword, String newPassword) {
        if (Strings.isNullOrEmpty(originalPassword)) {
            return JsonResult.fail(ErrorCodeEnum.BUSINESS_EXCEPTION_INVALID_PARAMETERS.getCode(),
                    ErrorMessage.originalPasswordIsNull);
        }
        if (Strings.isNullOrEmpty(newPassword)) {
            return JsonResult.fail(ErrorCodeEnum.BUSINESS_EXCEPTION_INVALID_PARAMETERS.getCode(),
                    ErrorMessage.newPasswordIsNull);
        }
        if (originalPassword.equals(newPassword)) {
            return JsonResult.fail(ErrorCodeEnum.BUSINESS_EXCEPTION_INVALID_PARAMETERS.getCode(),
                    ErrorMessage.originalPasswordEqualsNewPassword);
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
            return JsonResult.fail(ErrorCodeEnum.BUSINESS_EXCEPTION_INVALID_PARAMETERS.getCode(),
                    ErrorMessage.emailIsNull);
        }
        staffService.sendAuthCode(email);
        return JsonResult.success();
    }

    @RequestMapping(path = "/validateAuthCode", method = RequestMethod.POST)
    @ResponseBody
    public JsonResult<Void> doValidateAuthCode(String email, String authCode) {
        if (Strings.isNullOrEmpty(email)) {
            return JsonResult.fail(ErrorCodeEnum.BUSINESS_EXCEPTION_INVALID_PARAMETERS.getCode(),
                    ErrorMessage.emailIsNull);
        }
        if (Strings.isNullOrEmpty(authCode)) {
            return JsonResult.fail(ErrorCodeEnum.BUSINESS_EXCEPTION_INVALID_PARAMETERS.getCode(),
                    ErrorMessage.authCodeIsNull);
        }
        staffService.validateAuthCode(email, authCode);
        return JsonResult.success();
    }

    @RequestMapping("/insert")
    @ResponseBody
    public JsonResult<Integer> insert(StaffParams params) {
        String error = checkStaffParam(params);
        if (!Strings.isNullOrEmpty(error)) {
            return JsonResult.fail(ErrorCodeEnum.BUSINESS_EXCEPTION_INVALID_PARAMETERS.getCode(), error);
        }
        StaffDto dto = CommonConverter.map(params, StaffDto.class);
        int i = staffService.insert(dto);
        return JsonResult.success(i);
    }

    @RequestMapping("/search")
    @ResponseBody
    public JsonResult<PagedList<StaffVo>> search(Integer pageNum, Integer pageSize, String searchString) {
        if (!isValidPageNum(pageNum)) {
            return JsonResult.fail(ErrorCodeEnum.BUSINESS_EXCEPTION_INVALID_PARAMETERS.getCode(),
                    ErrorMessage.pageNumIsError);
        }
        if (!isValidPageSize(pageSize)) {
            return JsonResult.fail(ErrorCodeEnum.BUSINESS_EXCEPTION_INVALID_PARAMETERS.getCode(),
                    ErrorMessage.pageSizeIsError);
        }
        PagedList<StaffVo> pagedVos = staffService.search(pageNum, pageSize, searchString);
        return JsonResult.success(pagedVos);
    }

    @RequestMapping("/update")
    @ResponseBody
    public JsonResult<Integer> update(StaffParams params) {
        String error = checkStaffParam(params);
        if (!Strings.isNullOrEmpty(error)) {
            return JsonResult.fail(ErrorCodeEnum.BUSINESS_EXCEPTION_INVALID_PARAMETERS.getCode(), error);
        }
        if (params.getId() == null) {
            return JsonResult.fail(ErrorCodeEnum.BUSINESS_EXCEPTION_INVALID_PARAMETERS.getCode(),
                    ErrorMessage.staffIdIsNull);
        }
        if (Strings.isNullOrEmpty(params.getPrincipalName())) {
            return JsonResult.fail(ErrorCodeEnum.BUSINESS_EXCEPTION_INVALID_PARAMETERS.getCode(),
                    ErrorMessage.storePrincipalNameIsNull);
        }
        if (Strings.isNullOrEmpty(params.getPrincipalMobile())) {
            return JsonResult.fail(ErrorCodeEnum.BUSINESS_EXCEPTION_INVALID_PARAMETERS.getCode(),
                    ErrorMessage.storePrincipalMobileIsNull);
        }
        StaffDto dto = CommonConverter.map(params, StaffDto.class);
        int i = staffService.update(dto);
        return JsonResult.success(i);
    }

    @RequestMapping("/delete")
    @ResponseBody
    public JsonResult<Integer> delete(Long[] id) {
        if (id == null || id.length == 0) {
            return JsonResult.fail(ErrorCodeEnum.BUSINESS_EXCEPTION_INVALID_PARAMETERS.getCode(),
                    ErrorMessage.staffNotSelectedForDelete);
        }
        int i = staffService.deleteByIds(id);
        return JsonResult.success(i);
    }

    @RequestMapping("/listLevels")
    public JsonResult<List<StaffLevelVo>> doListStaffLevels() {
        List<StaffLevelVo> vos = Lists.newArrayList();
        for (StaffLevelEnum levelEnum : StaffLevelEnum.values()) {
            StaffLevelVo vo = new StaffLevelVo();
            vo.setLevelCode(levelEnum.getKey());
            vo.setLevelName(levelEnum.getDesc());
            vos.add(vo);
        }
        return JsonResult.success(vos);
    }

    private String checkStaffParam(StaffParams params) {
        if (params == null) {
            return ErrorMessage.paramIsNull;
        }
        if (Strings.isNullOrEmpty(params.getEmail())) {
            return ErrorMessage.emailIsNull;
        }
        if (params.getStoreId() == null) {
            return ErrorMessage.storeIsNull;
        }
        if (params.getStaffLevel() == null) {
            return ErrorMessage.staffLevelIsNull;
        }
        String desc = EnumUtils.getDescByKey(StaffLevelEnum.class, params.getStaffLevel());
        if (Strings.isNullOrEmpty(desc)) {
            return ErrorMessage.staffLevelIsError;
        }
        return null;
    }

}
