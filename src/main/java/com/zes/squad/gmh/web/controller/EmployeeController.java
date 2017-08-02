package com.zes.squad.gmh.web.controller;

import static com.zes.squad.gmh.web.helper.CheckHelper.isValidPageNum;
import static com.zes.squad.gmh.web.helper.CheckHelper.isValidPageSize;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.google.common.base.Strings;
import com.google.common.collect.Lists;
import com.zes.squad.gmh.common.converter.CommonConverter;
import com.zes.squad.gmh.common.entity.PagedList;
import com.zes.squad.gmh.common.entity.PagedLists;
import com.zes.squad.gmh.common.enums.JobEnum;
import com.zes.squad.gmh.common.enums.SexEnum;
import com.zes.squad.gmh.common.exception.ErrorCodeEnum;
import com.zes.squad.gmh.common.exception.ErrorMessage;
import com.zes.squad.gmh.common.util.EnumUtils;
import com.zes.squad.gmh.web.common.JsonResult;
import com.zes.squad.gmh.web.context.ThreadContext;
import com.zes.squad.gmh.web.entity.dto.EmployeeDto;
import com.zes.squad.gmh.web.entity.dto.JobDto;
import com.zes.squad.gmh.web.entity.param.EmployeeParams;
import com.zes.squad.gmh.web.entity.vo.EmployeeVo;
import com.zes.squad.gmh.web.entity.vo.JobVo;
import com.zes.squad.gmh.web.helper.CheckHelper;
import com.zes.squad.gmh.web.service.EmployeeService;

@RequestMapping("/employee")
@Controller
public class EmployeeController {

    @Autowired
    private EmployeeService employeeService;

    @Deprecated
    @RequestMapping("/listByPage")
    @ResponseBody
    public JsonResult<PagedList<EmployeeVo>> doListByPage(Integer pageNum, Integer pageSize) {
        if (!isValidPageNum(pageNum)) {
            return JsonResult.fail(ErrorCodeEnum.BUSINESS_EXCEPTION_INVALID_PARAMETERS.getCode(),
                    ErrorMessage.pageNumIsError);
        }
        if (isValidPageSize(pageSize)) {
            return JsonResult.fail(ErrorCodeEnum.BUSINESS_EXCEPTION_INVALID_PARAMETERS.getCode(),
                    ErrorMessage.pageSizeIsError);
        }
        PagedList<EmployeeDto> pagedDtos = employeeService.listByPage(pageNum, pageSize);
        PagedList<EmployeeVo> pagedVos = buildPagedEmployeeVosByDtos(pagedDtos);

        return JsonResult.success(pagedVos);
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
        return PagedLists.newPagedList(pagedDtos.getPageNum(), pagedDtos.getPageSize(), pagedDtos.getTotalCount(), vos);
    }

    @RequestMapping("/insert")
    @ResponseBody
    public JsonResult<EmployeeVo> insert(EmployeeParams params, Integer[] jobId) {
        String error = checkEmployeeParam(params);
        if (!Strings.isNullOrEmpty(error)) {
            return JsonResult.fail(ErrorCodeEnum.BUSINESS_EXCEPTION_INVALID_PARAMETERS.getCode(), error);
        }
        error = checkJobType(jobId);
        if (!Strings.isNullOrEmpty(error)) {
            return JsonResult.fail(ErrorCodeEnum.BUSINESS_EXCEPTION_INVALID_PARAMETERS.getCode(), error);
        }
        EmployeeDto dto = CommonConverter.map(params, EmployeeDto.class);
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
        dto.setShopId(ThreadContext.getStaffStoreId());
        EmployeeDto employeeDto = employeeService.insert(dto);
        EmployeeVo vo = buildEmployeeVoByDto(employeeDto);
        return JsonResult.success(vo);
    }

    private EmployeeVo buildEmployeeVoByDto(EmployeeDto dto) {
        EmployeeVo vo = CommonConverter.map(dto, EmployeeVo.class);
        vo.setIsWork(dto.getIsWork().booleanValue() ? "是" : "否");
        Integer sex = Integer.valueOf(String.valueOf(dto.getSex()));
        vo.setSex(EnumUtils.getDescByKey(SexEnum.class, sex));
        List<JobVo> jobVos = CommonConverter.mapList(dto.getJobDtos(), JobVo.class);
        vo.setJobVos(jobVos);
        return vo;
    }

    @RequestMapping("/leave")
    @ResponseBody
    public JsonResult<Integer> leave(Long[] id) {
        if (id == null || id.length == 0) {
            return JsonResult.fail(ErrorCodeEnum.BUSINESS_EXCEPTION_INVALID_PARAMETERS.getCode(), "请选择离职员工");
        }
        int i = employeeService.leave(id);
        return JsonResult.success(i);
    }

    @RequestMapping("/update")
    @ResponseBody
    public JsonResult<Integer> update(EmployeeParams params, Integer[] jobId) {
        String error = checkEmployeeParam(params);
        if (!Strings.isNullOrEmpty(error)) {
            return JsonResult.fail(ErrorCodeEnum.BUSINESS_EXCEPTION_INVALID_PARAMETERS.getCode(), error);
        }
        error = checkJobType(jobId);
        if (!Strings.isNullOrEmpty(error)) {
            return JsonResult.fail(ErrorCodeEnum.BUSINESS_EXCEPTION_INVALID_PARAMETERS.getCode(), error);
        }
        if (params.getId() == null) {
            return JsonResult.fail(ErrorCodeEnum.BUSINESS_EXCEPTION_INVALID_PARAMETERS.getCode(), "员工标识不能为空");
        }
        if (params.getEntryDate() == null) {
            return JsonResult.fail(ErrorCodeEnum.BUSINESS_EXCEPTION_INVALID_PARAMETERS.getCode(), "员工入职日期不能为空");
        }
        EmployeeDto dto = CommonConverter.map(params, EmployeeDto.class);
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
        dto.setShopId(ThreadContext.getStaffStoreId());
        int i = employeeService.update(dto);
        return JsonResult.success(i);
    }

    @Deprecated
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

    private String checkEmployeeParam(EmployeeParams param) {
        if (param == null) {
            return "员工信息不能为空";
        }
        if (Strings.isNullOrEmpty(param.getEmName())) {
            return "员工姓名不能为空";
        }
        if (param.getSex() == null) {
            return "员工性别不能为空";
        }
        String desc = null;
        if (param.getSex() != null) {
            desc = EnumUtils.getDescByKey(SexEnum.class, Integer.valueOf(String.valueOf(param.getSex())));
            if (Strings.isNullOrEmpty(desc)) {
                return "员工性别错误";
            }
        }
        if (Strings.isNullOrEmpty(param.getPhone())) {
            return "员工手机号不能为空";
        }
        if (!CheckHelper.isValidMobile(param.getPhone())) {
            return "手机号格式错误";
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
