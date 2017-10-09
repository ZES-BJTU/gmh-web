package com.zes.squad.gmh.web.controller;

import static com.zes.squad.gmh.web.helper.CheckHelper.isValidMobile;
import static com.zes.squad.gmh.web.helper.CheckHelper.isValidPageNum;
import static com.zes.squad.gmh.web.helper.CheckHelper.isValidPageSize;
import static com.zes.squad.gmh.web.helper.LogicHelper.ensureEntityExist;
import static com.zes.squad.gmh.web.helper.LogicHelper.ensureParameterExist;
import static com.zes.squad.gmh.web.helper.LogicHelper.ensureParameterValid;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.google.common.base.Strings;
import com.google.common.collect.Lists;
import com.zes.squad.gmh.common.converter.CommonConverter;
import com.zes.squad.gmh.common.entity.PagedList;
import com.zes.squad.gmh.common.enums.ChargeWayEnum;
import com.zes.squad.gmh.common.exception.ErrorCodeEnum;
import com.zes.squad.gmh.common.exception.ErrorMessage;
import com.zes.squad.gmh.common.util.EnumUtils;
import com.zes.squad.gmh.web.common.JsonResult;
import com.zes.squad.gmh.web.entity.condition.AppointmentQueryCondition;
import com.zes.squad.gmh.web.entity.dto.AppointmentDto;
import com.zes.squad.gmh.web.entity.dto.AppointmentProjectDto;
import com.zes.squad.gmh.web.entity.param.AppointmentParams;
import com.zes.squad.gmh.web.entity.param.AppointmentQueryParams;
import com.zes.squad.gmh.web.entity.vo.AppointmentVo;
import com.zes.squad.gmh.web.entity.vo.EmployeeItemVo;
import com.zes.squad.gmh.web.entity.vo.TimeVo;
import com.zes.squad.gmh.web.helper.LogicHelper;
import com.zes.squad.gmh.web.service.AppointmentService;

@RequestMapping("/appointment")
@Controller
public class AppointmentController {

    @Autowired
    private AppointmentService appointmentService;

    @RequestMapping("/queryTime")
    @ResponseBody
    public JsonResult<List<TimeVo>> doQueryTime(Date time, Long employeeId) {
        LogicHelper.ensureParameterExist(time, "查询时间不能为空");
        LogicHelper.ensureParameterExist(employeeId, "操作员标识不能为空");
        List<TimeVo> vos = appointmentService.queryTime(time, employeeId);
        return JsonResult.success(vos);
    }

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

    @RequestMapping("/listEmployeesByProject")
    @ResponseBody
    public JsonResult<List<EmployeeItemVo>> doListEmployeesByProject(Long projectId) {
        ensureParameterExist(projectId, ErrorMessage.projectIdIsNull);
        List<EmployeeItemVo> vos = appointmentService.listEmployeesByProject(projectId);
        return JsonResult.success(vos);
    }

    @RequestMapping("/listAppointmentsByEmployee")
    @ResponseBody
    public JsonResult<List<AppointmentVo>> doListAppointmentsByEmployee(Long employeeId) {
        ensureParameterExist(employeeId, ErrorMessage.employeeIdIsNull);
        List<AppointmentVo> vos = appointmentService.listAppointmentsByEmployee(employeeId);
        return JsonResult.success(vos);
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
    public JsonResult<Integer> insert(AppointmentParams params) {
        checkAppointmentParams(params);
        AppointmentDto dto = buildAppointmentDtoByParams(params);
        int i = appointmentService.insert(dto);
        return JsonResult.success(i);

    }

    private AppointmentDto buildAppointmentDtoByParams(AppointmentParams params) {
        AppointmentDto dto = CommonConverter.map(params, AppointmentDto.class);
        List<AppointmentProjectDto> projectDtos = Lists.newArrayList();
        String[] projects = params.getProjects().split(";");
        for (String project : projects) {
            String[] projectDetail = project.split(",");
            AppointmentProjectDto projectDto = new AppointmentProjectDto();
            projectDto.setProjectId(Long.valueOf(projectDetail[0]));
            projectDto.setEmployeeId(Long.valueOf(projectDetail[1]));
            projectDto.setBeginTime(new Date(Long.valueOf(projectDetail[2])));
            projectDto.setEndTime(new Date(Long.valueOf(projectDetail[3])));
            projectDtos.add(projectDto);
        }
        dto.setAppointmentProjectDtos(projectDtos);
        return dto;
    }

    @RequestMapping("/update")
    @ResponseBody
    public JsonResult<Integer> update(AppointmentDto dto) {
        ensureParameterExist(dto, ErrorMessage.paramIsNull);
        ensureParameterExist(dto.getId(), ErrorMessage.appointmentIdIsNull);
        ensureParameterExist(dto.getName(), "预约人姓名为空");
        ensureParameterExist(dto.getPhone(), "预约人手机号为空");
        ensureParameterExist(dto.getSex(), "预约人性别为空");
        int i = appointmentService.update(dto);
        return JsonResult.success(i);
    }

    @RequestMapping("/start")
    @ResponseBody
    public JsonResult<Integer> start(Long id) {
        if (id == null || id == 0L) {
            return JsonResult.fail(ErrorCodeEnum.BUSINESS_EXCEPTION_INVALID_PARAMETERS.getCode(),
                    ErrorMessage.appointmentNotSelected);
        }
        int i = appointmentService.start(id);
        return JsonResult.success(i);
    }

    @RequestMapping("/cancel")
    @ResponseBody
    public JsonResult<Integer> cancel(Long id) {
        if (id == null || id == 0L) {
            return JsonResult.fail(ErrorCodeEnum.BUSINESS_EXCEPTION_INVALID_PARAMETERS.getCode(),
                    ErrorMessage.appointmentNotSelected);
        }
        int i = appointmentService.cancel(id);
        return JsonResult.success(i);
    }

    @RequestMapping("/finish")
    @ResponseBody
    public JsonResult<Integer> finish(Long id, BigDecimal charge, Integer chargeWay, Long counselorId, String source,
                                      String remark) {
        if (id == null || id == 0L) {
            return JsonResult.fail(ErrorCodeEnum.BUSINESS_EXCEPTION_INVALID_PARAMETERS.getCode(),
                    ErrorMessage.appointmentNotSelected);
        }
        ensureParameterExist(charge, ErrorMessage.consumeRecordChargeIsNull);
        ensureParameterValid(charge.compareTo(BigDecimal.ZERO) == 1, ErrorMessage.consumeRecordChargeIsError);
        ensureParameterExist(chargeWay, ErrorMessage.consumeRecordChargeWayIsNull);
        ensureParameterValid(!Strings.isNullOrEmpty(EnumUtils.getDescByKey(ChargeWayEnum.class, chargeWay)),
                ErrorMessage.consumeRecordChargeWayIsError);
        int i = appointmentService.finish(id, charge, chargeWay, counselorId, source, remark);
        return JsonResult.success(i);
    }

    @RequestMapping("/remind")
    @ResponseBody
    public JsonResult<List<AppointmentVo>> doRemindAppointmentTime() {
        List<AppointmentVo> vos = appointmentService.remind();
        return JsonResult.success(vos);
    }

    private void checkAppointmentQueryParams(AppointmentQueryParams params) {
        ensureParameterExist(params, ErrorMessage.paramIsNull);
        ensureParameterValid(isValidPageNum(params.getPageNum()), ErrorMessage.pageNumIsError);
        ensureParameterValid(isValidPageSize(params.getPageSize()), ErrorMessage.pageSizeIsError);
    }

    private void checkAppointmentParams(AppointmentParams params) {
        ensureEntityExist(params, ErrorMessage.paramIsNull);
        ensureParameterExist(params.getName(), "预约人姓名为空");
        ensureParameterExist(params.getPhone(), "预约人手机号为空");
        ensureParameterExist(params.getSex(), "预约人性别为空");
        ensureParameterValid(isValidMobile(params.getPhone()), ErrorMessage.memberMobileIsError);
        ensureParameterExist(params.getProjects(), "预约项目为空");
        ensureParameterExist(params.getLine(), ErrorMessage.appointmentLineIsNull);
    }

}
