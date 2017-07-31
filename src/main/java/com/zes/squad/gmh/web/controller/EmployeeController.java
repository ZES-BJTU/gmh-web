package com.zes.squad.gmh.web.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.google.common.base.Strings;
import com.google.common.collect.Lists;
import com.zes.squad.gmh.common.converter.CommonConverter;
import com.zes.squad.gmh.common.entity.PagedList;
import com.zes.squad.gmh.common.enums.JobEnum;
import com.zes.squad.gmh.common.enums.SexEnum;
import com.zes.squad.gmh.common.exception.ErrorCodeEnum;
import com.zes.squad.gmh.common.exception.ErrorMessage;
import com.zes.squad.gmh.common.util.EnumUtils;
import com.zes.squad.gmh.web.common.JsonResult;
import com.zes.squad.gmh.web.context.ThreadContext;
import com.zes.squad.gmh.web.entity.dto.EmployeeDto;
import com.zes.squad.gmh.web.entity.dto.JobDto;
import com.zes.squad.gmh.web.entity.dto.StaffDto;
import com.zes.squad.gmh.web.entity.param.EmployeeParam;
import com.zes.squad.gmh.web.entity.vo.EmployeeVo;
import com.zes.squad.gmh.web.entity.vo.JobVo;
import com.zes.squad.gmh.web.service.EmployeeService;

@RequestMapping("/employee")
@Controller
public class EmployeeController {

    @Autowired
    private EmployeeService employeeService;

    @RequestMapping("/listByPage")
    @ResponseBody
    public JsonResult<PagedList<EmployeeVo>> doListByPage(Integer pageNum, Integer pageSize) {
        if (pageNum == null || pageNum < 0) {
            return JsonResult.fail(ErrorCodeEnum.BUSINESS_EXCEPTION_INVALID_PARAMETERS.getCode(),
                    ErrorMessage.pageNumIsError);
        }
        if (pageSize == null || pageSize < 0) {
            return JsonResult.fail(ErrorCodeEnum.BUSINESS_EXCEPTION_INVALID_PARAMETERS.getCode(),
                    ErrorMessage.pageSizeIsError);
        }
        PagedList<EmployeeDto> pagedDtos = employeeService.listByPage(pageNum, pageSize);
        PagedList<EmployeeVo> pagedVos = buildPagedEmployeeVosByDtos(pagedDtos);

        return JsonResult.success(pagedVos);
    }

    private PagedList<EmployeeVo> buildPagedEmployeeVosByDtos(PagedList<EmployeeDto> pagedDtos) {
        List<EmployeeVo> vos = Lists.newArrayList();
        for (EmployeeDto dto : pagedDtos.getData()) {
            EmployeeVo vo = CommonConverter.map(dto, EmployeeVo.class);
            vo.setIsWork(dto.getIsWork().booleanValue() ? "是" : "否");
            Integer sex = Integer.valueOf(String.valueOf(dto.getSex()));
            vo.setSex(EnumUtils.getDescByKey(SexEnum.class, sex));
            List<JobVo> jobVos = CommonConverter.mapList(dto.getJobDtos(), JobVo.class);
            vo.setJobVos(jobVos);
            vos.add(vo);
        }
        return PagedList.newMe(pagedDtos.getPageNum(), pagedDtos.getPageSize(), pagedDtos.getTotalCount(), vos);
    }

    @RequestMapping("/search")
    @ResponseBody
    public JsonResult<PagedList<EmployeeVo>> search(Integer pageNum, Integer pageSize, String searchString) {
        if (pageNum == null || pageNum < 0) {
            return JsonResult.fail(ErrorCodeEnum.BUSINESS_EXCEPTION_INVALID_PARAMETERS.getCode(), "分页页码错误");
        }
        if (pageSize == null || pageSize < 0) {
            return JsonResult.fail(ErrorCodeEnum.BUSINESS_EXCEPTION_INVALID_PARAMETERS.getCode(), "分页大小错误");
        }
        PagedList<EmployeeDto> pagedDtos = employeeService.searchListByPage(pageNum, pageSize, searchString);
        PagedList<EmployeeVo> pagedVos = buildPagedEmployeeVosByDtos(pagedDtos);

        return JsonResult.success(pagedVos);
    }

    @RequestMapping("/insert")
    @ResponseBody
    public JsonResult<?> insert(EmployeeParam param, Integer[] jobId) {
        String error = checkEmployeeParam(param);
        if (!Strings.isNullOrEmpty(error)) {
            return JsonResult.fail(ErrorCodeEnum.BUSINESS_EXCEPTION_INVALID_PARAMETERS.getCode(), error);
        }
        error = checkJobType(jobId);
        if (!Strings.isNullOrEmpty(error)) {
            return JsonResult.fail(ErrorCodeEnum.BUSINESS_EXCEPTION_INVALID_PARAMETERS.getCode(), error);
        }
        StaffDto staffDto = ThreadContext.getCurrentStaff();
        EmployeeDto dto = CommonConverter.map(param, EmployeeDto.class);
        List<JobDto> dtos = Lists.newArrayList();
        for (Integer jobType : jobId) {
            if (jobType != null) {
                JobDto jobDto = new JobDto();
                jobDto.setJobType(jobType);
                jobDto.setJobName(EnumUtils.getDescByKey(JobEnum.class, jobType));
                dtos.add(jobDto);
            }
        }
        dto.setJobDtos(dtos);
        dto.setShopId(staffDto.getStoreId());
        EmployeeDto newDto = employeeService.insert(dto);
        EmployeeVo vo = CommonConverter.map(newDto, EmployeeVo.class);
        return JsonResult.success(vo);
    }

    @RequestMapping("/leave")
    @ResponseBody
    public JsonResult<?> leave(Long[] id) {
        int i = employeeService.leave(id);
        return JsonResult.success(i);
    }

    @RequestMapping("/update")
    @ResponseBody
    public JsonResult<?> update(EmployeeParam param, Integer[] jobId) {
        String error = checkEmployeeParam(param);
        if (!Strings.isNullOrEmpty(error)) {
            return JsonResult.fail(ErrorCodeEnum.BUSINESS_EXCEPTION_INVALID_PARAMETERS.getCode(), error);
        }
        error = checkJobType(jobId);
        if (!Strings.isNullOrEmpty(error)) {
            return JsonResult.fail(ErrorCodeEnum.BUSINESS_EXCEPTION_INVALID_PARAMETERS.getCode(), error);
        }
        if (param.getId() == null) {
            return JsonResult.fail(ErrorCodeEnum.BUSINESS_EXCEPTION_INVALID_PARAMETERS.getCode(), "员工标识不能为空");
        }
        StaffDto staffDto = ThreadContext.getCurrentStaff();
        EmployeeDto dto = CommonConverter.map(param, EmployeeDto.class);
        List<JobDto> dtos = Lists.newArrayList();
        for (Integer jobType : jobId) {
            if (jobType != null) {
                JobDto jobDto = new JobDto();
                jobDto.setJobType(jobType);
                jobDto.setJobName(EnumUtils.getDescByKey(JobEnum.class, jobType));
                dtos.add(jobDto);
            }
        }
        dto.setJobDtos(dtos);
        dto.setShopId(staffDto.getStoreId());
        int i = employeeService.update(dto);
        if (i > 0) {
            return JsonResult.success(i);
        } else {
            return JsonResult.fail(ErrorCodeEnum.BUSINESS_EXCEPTION_OPERATION_FAILED.getCode(), "更新员工信息失败");
        }
    }

    @RequestMapping("/listJobs")
    @ResponseBody
    public JsonResult<List<JobVo>> doListJobVos() {
        List<JobVo> vos = Lists.newArrayList();
        for (JobEnum job : JobEnum.values()) {
            JobVo vo = new JobVo();
            vo.setJobType(job.getKey());
            vo.setJobName(job.getDesc());
            vos.add(vo);
        }
        return JsonResult.success(vos);

    }

    private String checkEmployeeParam(EmployeeParam param) {
        if (param == null) {
            return "员工信息不能为空";
        }
        String desc = null;
        if (param.getSex() != null) {
            desc = EnumUtils.getDescByKey(SexEnum.class, param.getSex());
            if (Strings.isNullOrEmpty(desc)) {
                return "员工性别错误";
            }
        }
        return null;
    }

    private String checkJobType(Integer[] jobId) {
        if (jobId == null || jobId.length == 0) {
            return "员工工种不能为空";
        }
        String desc = null;
        for (Integer jobType : jobId) {
            if (jobType == null) {
                return "员工工种不能为空";
            }
            desc = EnumUtils.getDescByKey(JobEnum.class, jobType);
            if (Strings.isNullOrEmpty(desc)) {
                return "员工工种不在可选范围内";
            }
        }
        return null;
    }
}
