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
import com.zes.squad.gmh.common.enums.ProjectTypeEnum;
import com.zes.squad.gmh.common.exception.ErrorCodeEnum;
import com.zes.squad.gmh.common.util.EnumUtils;
import com.zes.squad.gmh.web.common.JsonResult;
import com.zes.squad.gmh.web.context.ThreadContext;
import com.zes.squad.gmh.web.entity.dto.ProjectTypeDto;
import com.zes.squad.gmh.web.entity.vo.ProjectTopTypeVo;
import com.zes.squad.gmh.web.entity.vo.ProjectTypeVo;
import com.zes.squad.gmh.web.service.ProjectTypeService;

@RequestMapping("/projectType")
@Controller
public class ProjectTypeController {

    @Autowired
    private ProjectTypeService projectTypeService;

    @RequestMapping("/getAll")
    @ResponseBody
    public JsonResult<List<ProjectTypeVo>> getAll() {
        List<ProjectTypeVo> vos = projectTypeService.getAll();
        return JsonResult.success(vos);
    }

    @RequestMapping("/listTops")
    @ResponseBody
    public JsonResult<List<ProjectTopTypeVo>> doListTops() {
        List<ProjectTopTypeVo> vos = Lists.newArrayList();
        for (ProjectTypeEnum type : ProjectTypeEnum.values()) {
            ProjectTopTypeVo vo = new ProjectTopTypeVo();
            vo.setTopTypeId(type.getKey());
            vo.setTopTypeName(type.getDesc());
            vos.add(vo);
        }
        return JsonResult.success(vos);
    }

    @RequestMapping("/search")
    @ResponseBody
    public JsonResult<PagedList<ProjectTypeVo>> search(Integer pageNum, Integer pageSize, Long topType,
                                                       String searchString) {
        if (pageNum == null || pageNum < 0) {
            return JsonResult.fail(ErrorCodeEnum.BUSINESS_EXCEPTION_INVALID_PARAMETERS.getCode(), "分页页码错误");
        }
        if (pageSize == null || pageSize < 0) {
            return JsonResult.fail(ErrorCodeEnum.BUSINESS_EXCEPTION_INVALID_PARAMETERS.getCode(), "分页大小错误");
        }
        PagedList<ProjectTypeDto> pagedListDto = projectTypeService.searchListByPage(pageNum, pageSize, topType,
                searchString);
        PagedList<ProjectTypeVo> pagedListVo = CommonConverter.mapPageList(pagedListDto, ProjectTypeVo.class);

        return JsonResult.success(pagedListVo);
    }

    @RequestMapping("/listByTopType")
    @ResponseBody
    public JsonResult<List<ProjectTypeVo>> doListByTopType(Integer topType) {
        if (topType == null) {
            return JsonResult.fail(ErrorCodeEnum.BUSINESS_EXCEPTION_INVALID_PARAMETERS.getCode(), "请选择美容项顶层分类");
        }
        List<ProjectTypeVo> vos = projectTypeService.listByTopType(topType);
        return JsonResult.success(vos);
    }

    @RequestMapping("/insert")
    @ResponseBody
    public JsonResult<?> insert(ProjectTypeDto dto) {
        String error = checkProjectType(dto);
        if (!Strings.isNullOrEmpty(error)) {
            return JsonResult.fail(ErrorCodeEnum.BUSINESS_EXCEPTION_INVALID_PARAMETERS.getCode(), error);
        }
        dto.setStoreId(ThreadContext.getStaffStoreId());
        int i = projectTypeService.insert(dto);
        if (i > 0) {
            return JsonResult.success(i);
        } else {
            return JsonResult.fail(ErrorCodeEnum.BUSINESS_EXCEPTION_OPERATION_FAILED.getCode(), "添加美容项分类失败");
        }
    }

    private String checkProjectType(ProjectTypeDto dto) {
        if (dto == null) {
            return "请填入美容项分类信息";
        }
        String desc = EnumUtils.getDescByKey(ProjectTypeEnum.class, dto.getTopType());
        if (Strings.isNullOrEmpty(desc)) {
            return "美容项分类不在可选范围内";
        }
        return null;
    }

    @RequestMapping("/update")
    @ResponseBody
    public JsonResult<?> update(ProjectTypeDto dto) {
        String error = checkProjectType(dto);
        if (!Strings.isNullOrEmpty(error)) {
            return JsonResult.fail(ErrorCodeEnum.BUSINESS_EXCEPTION_INVALID_PARAMETERS.getCode(), error);
        }
        if (dto.getId() == null) {
            return JsonResult.fail(ErrorCodeEnum.BUSINESS_EXCEPTION_INVALID_PARAMETERS.getCode(), "美容项分类标识不能为空");
        }
        int i = projectTypeService.update(dto);
        if (i > 0) {
            return JsonResult.success(i);
        } else {
            return JsonResult.fail(ErrorCodeEnum.BUSINESS_EXCEPTION_OPERATION_FAILED.getCode(), "修改美容项分类失败");
        }
    }

    @RequestMapping("/delete")
    @ResponseBody
    public JsonResult<?> delete(Long[] id) {
        if (id == null || id.length == 0) {
            return JsonResult.fail(ErrorCodeEnum.BUSINESS_EXCEPTION_INVALID_PARAMETERS.getCode(), "请选择要删除的美容项分类");
        }
        int i = 0;
        i = projectTypeService.delByIds(id);
        if (i > 0) {
            return JsonResult.success(i);
        } else {
            return JsonResult.fail(ErrorCodeEnum.BUSINESS_EXCEPTION_OPERATION_FAILED.getCode(), "删除美容项分类失败");
        }
    }
}
