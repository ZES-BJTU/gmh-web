package com.zes.squad.gmh.web.controller;

import static com.zes.squad.gmh.web.helper.CheckHelper.isValidMobile;
import static com.zes.squad.gmh.web.helper.CheckHelper.isValidPageNum;
import static com.zes.squad.gmh.web.helper.CheckHelper.isValidPageSize;
import static com.zes.squad.gmh.web.helper.LogicHelper.ensureEntityExist;
import static com.zes.squad.gmh.web.helper.LogicHelper.ensureParameterExist;
import static com.zes.squad.gmh.web.helper.LogicHelper.ensureParameterValid;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.zes.squad.gmh.common.converter.CommonConverter;
import com.zes.squad.gmh.common.entity.PagedList;
import com.zes.squad.gmh.common.exception.ErrorCodeEnum;
import com.zes.squad.gmh.common.exception.ErrorMessage;
import com.zes.squad.gmh.web.common.JsonResult;
import com.zes.squad.gmh.web.entity.condition.AppointmentQueryCondition;
import com.zes.squad.gmh.web.entity.condition.ConflictQueryCondition;
import com.zes.squad.gmh.web.entity.dto.AppointmentDto;
import com.zes.squad.gmh.web.entity.param.AppointmentQueryParams;
import com.zes.squad.gmh.web.entity.vo.AppointmentVo;
import com.zes.squad.gmh.web.service.AppointmentService;

@RequestMapping("/appointment")
@Controller
public class AppointmentController {
    @Autowired
    private AppointmentService appointmentService;

    @RequestMapping("/listAll")
    @ResponseBody
    public JsonResult<List<AppointmentVo>> doListAllAppointments() {
        List<AppointmentVo> vos = appointmentService.listAllAppointments();
        return JsonResult.success(vos);
    }

    @RequestMapping("/search")
    @ResponseBody
    public JsonResult<PagedList<AppointmentVo>> doListPagedAppointments(AppointmentQueryParams params) {
        checkAppointmentQueryParams(params);
        AppointmentQueryCondition condition = CommonConverter.map(params, AppointmentQueryCondition.class);
        PagedList<AppointmentVo> pagedVos = appointmentService.searchPagedAppointments(condition);
        return JsonResult.success(pagedVos);
    }

    @RequestMapping("/queryByPhone")
    @ResponseBody
    public JsonResult<List<AppointmentVo>> doQueryByPhone(String phone) {
        ensureParameterExist(phone, ErrorMessage.memberMobileIsNull);
        List<AppointmentVo> vos = appointmentService.queryByPhone(phone);
        return JsonResult.success(vos);

    }

    @RequestMapping("/insert")
    @ResponseBody
    public JsonResult<Integer> insert(AppointmentDto dto) {
        checkAppointmentDto(dto);
        ConflictQueryCondition condition = CommonConverter.map(dto, ConflictQueryCondition.class);
        if (appointmentService.isConflict(condition)) {
            return JsonResult.fail(ErrorCodeEnum.BUSINESS_EXCEPTION_OPERATION_NOT_ALLOWED.getCode(), "该员工时间冲突");
        }
        int i = appointmentService.insert(dto);
        return JsonResult.success(i);

    }

    @RequestMapping("/update")
    @ResponseBody
    public JsonResult<Integer> update(AppointmentDto dto) {
        checkAppointmentDto(dto);
        ensureParameterExist(dto.getId(), ErrorMessage.appointmentIdIsNull);
        ConflictQueryCondition condition = CommonConverter.map(dto, ConflictQueryCondition.class);
        if (appointmentService.isConflict(condition)) {
            return JsonResult.fail(ErrorCodeEnum.BUSINESS_EXCEPTION_OPERATION_NOT_ALLOWED.getCode(), "该员工时间冲突");
        }
        int i = appointmentService.update(dto);
        return JsonResult.success(i);
    }

    @RequestMapping("/cancel")
    @ResponseBody
    public JsonResult<Integer> cancel(Long id) {
        if (id == null || id == 0L) {
            return JsonResult.fail(ErrorCodeEnum.BUSINESS_EXCEPTION_INVALID_PARAMETERS.getCode(), "请选择预约");
        }
        int i = appointmentService.cancel(id);
        return JsonResult.success(i);
    }

    @RequestMapping("/finish")
    @ResponseBody
    public JsonResult<Integer> finish(Long id, BigDecimal charge, Integer chargeWay) {
        if (id == null || id == 0L) {
            return JsonResult.fail(ErrorCodeEnum.BUSINESS_EXCEPTION_INVALID_PARAMETERS.getCode(), "请选择预约");
        }
        int i = appointmentService.finish(id, charge, chargeWay);
        return JsonResult.success(i);
    }

    @RequestMapping("/remind")
    @ResponseBody
    public JsonResult<String> doRemindAppointmentTime() {
        return JsonResult.success();
    }

    private void checkAppointmentQueryParams(AppointmentQueryParams params) {
        ensureParameterExist(params, ErrorMessage.paramIsNull);
        ensureParameterValid(isValidPageNum(params.getPageNum()), ErrorMessage.pageNumIsError);
        ensureParameterValid(isValidPageSize(params.getPageSize()), ErrorMessage.pageSizeIsError);
    }

    private void checkAppointmentDto(AppointmentDto dto) {
        ensureEntityExist(dto, ErrorMessage.paramIsNull);
        ensureParameterExist(dto.getMemberId(), ErrorMessage.memberIdIsNull);
        ensureParameterExist(dto.getPhone(), ErrorMessage.memberMobileIsNull);
        ensureParameterValid(isValidMobile(dto.getPhone()), ErrorMessage.memberMobileIsError);
        ensureParameterExist(dto.getProjectId(), ErrorMessage.projectIdIsNull);
        ensureParameterExist(dto.getEmployeeId(), ErrorMessage.employeeNotSelected);
        ensureParameterExist(dto.getBeginTime(), ErrorMessage.appointmentBeginingTimeIsNull);
        ensureParameterExist(dto.getEndTime(), ErrorMessage.appointmentBeginingTimeIsNull);
        ensureParameterValid(dto.getBeginTime().before(dto.getEndTime()),
                ErrorMessage.appointmentBeginningTimeIsAfterEndingTime);
        ensureParameterExist(dto.getLine(), ErrorMessage.appointmentLineIsNull);
    }

}
