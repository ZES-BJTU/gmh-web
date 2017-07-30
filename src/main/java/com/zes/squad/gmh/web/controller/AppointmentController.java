package com.zes.squad.gmh.web.controller;

import java.util.ArrayList;
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

    @RequestMapping("/getAll")
    @ResponseBody
    public JsonResult<?> getAll() {
        List<AppointmentVo> voList = new ArrayList<AppointmentVo>();
        voList = appointmentService.getAll();
        return JsonResult.success(voList);
    }

    @RequestMapping("getByPhone")
    @ResponseBody
    public JsonResult<?> getByPhone(String phone) {
        if (Strings.isNullOrEmpty(phone)) {
            return JsonResult.fail(ErrorCodeEnum.BUSINESS_EXCEPTION_INVALID_PARAMETERS.getCode(), "请输入会员电话号码");
        }
        AppointmentVo vo = appointmentService.getByPhone(phone);
        if (vo == null) {
            return JsonResult.fail(ErrorCodeEnum.BUSINESS_EXCEPTION_COLLECTION_IS_EMPTY.getCode(), "未查询到该用户");
        }
        return JsonResult.success(vo);

    }

    @RequestMapping("insert")
    @ResponseBody
    public JsonResult<?> insert(AppointmentDto dto) {

        if (appointmentService.isConflict()) {
            return JsonResult.fail(ErrorCodeEnum.BUSINESS_EXCEPTION_OPERATION_NOT_ALLOWED.getCode(), "该员工时间冲突");
        }
        int i = appointmentService.insert(dto);
        return JsonResult.success(i);

    }

    @RequestMapping("update")
    @ResponseBody
    public JsonResult<?> update(AppointmentDto dto) {
        if (appointmentService.isConflict()) {
            return JsonResult.fail(ErrorCodeEnum.BUSINESS_EXCEPTION_OPERATION_NOT_ALLOWED.getCode(), "该员工时间冲突");
        }
        int i = appointmentService.update(dto);
        return JsonResult.success(i);
    }

    @RequestMapping("cancel")
    @ResponseBody
    public JsonResult<?> cancel(Long id) {
        if (id == null || id == 0L) {
            return JsonResult.fail(ErrorCodeEnum.BUSINESS_EXCEPTION_INVALID_PARAMETERS.getCode(), "请选择预约");
        }
        int i = appointmentService.cancel(id);
        if (i == 1)
            return JsonResult.success();
        else
            return JsonResult.fail(ErrorCodeEnum.BUSINESS_EXCEPTION_OPERATION_FAILED.getCode(), "没有记录被修改");
    }

    @RequestMapping("finish")
    @ResponseBody
    public JsonResult<?> finish(Long id) {
        if (id == null || id == 0L) {
            return JsonResult.fail(ErrorCodeEnum.BUSINESS_EXCEPTION_INVALID_PARAMETERS.getCode(), "请选择预约");
        }
        int i = appointmentService.finish(id);
        if (i == 1)
            return JsonResult.success();
        else
            return JsonResult.fail(ErrorCodeEnum.BUSINESS_EXCEPTION_OPERATION_FAILED.getCode(), "没有记录被修改");
    }
}
