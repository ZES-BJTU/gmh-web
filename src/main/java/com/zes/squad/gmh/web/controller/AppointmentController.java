package com.zes.squad.gmh.web.controller;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.google.common.base.Strings;
import com.zes.squad.gmh.common.exception.ErrorCodeEnum;
import com.zes.squad.gmh.web.common.JsonResult;
import com.zes.squad.gmh.web.entity.dto.AppointmentDto;
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
        List<AppointmentVo> vos = appointmentService.listAllAppoints();
        return JsonResult.success(vos);
    }

    @RequestMapping("/queryByPhone")
    @ResponseBody
    public JsonResult<AppointmentVo> doQueryByPhone(String phone) {
        if (Strings.isNullOrEmpty(phone)) {
            return JsonResult.fail(ErrorCodeEnum.BUSINESS_EXCEPTION_INVALID_PARAMETERS.getCode(), "请输入会员电话号码");
        }
        AppointmentVo vo = appointmentService.queryByPhone(phone);
        if (vo == null) {
            return JsonResult.fail(ErrorCodeEnum.BUSINESS_EXCEPTION_ENTITY_NOT_FOUND.getCode(), "未查询到该用户");
        }
        return JsonResult.success(vo);

    }

    @RequestMapping("/insert")
    @ResponseBody
    public JsonResult<Integer> insert(AppointmentDto dto) {
        if (appointmentService.isConflict(dto.getStoreId(), dto.getEmployeeId(), dto.getBeginTime(),
                dto.getEndTime())) {
            return JsonResult.fail(ErrorCodeEnum.BUSINESS_EXCEPTION_OPERATION_NOT_ALLOWED.getCode(), "该员工时间冲突");
        }
        int i = appointmentService.insert(dto);
        return JsonResult.success(i);

    }

    @RequestMapping("/update")
    @ResponseBody
    public JsonResult<?> update(AppointmentDto dto) {
        if (appointmentService.isConflict(dto.getStoreId(), dto.getEmployeeId(), dto.getBeginTime(),
                dto.getEndTime())) {
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

}
