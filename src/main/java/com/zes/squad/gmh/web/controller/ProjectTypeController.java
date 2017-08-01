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
import com.zes.squad.gmh.common.enums.ProjectTypeEnum;
import com.zes.squad.gmh.common.exception.ErrorCodeEnum;
import com.zes.squad.gmh.common.exception.ErrorMessage;
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
    public JsonResult<List<ProjectTypeVo>> doListProjectTypes() {
        List<ProjectTypeVo> vos = projectTypeService.listProjectTypes();
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
    public JsonResult<PagedList<ProjectTypeVo>> search(Integer pageNum, Integer pageSize, Integer topType,
                                                       String searchString) {
        if (!isValidPageNum(pageNum)) {
            return JsonResult.fail(ErrorCodeEnum.BUSINESS_EXCEPTION_INVALID_PARAMETERS.getCode(),
                    ErrorMessage.pageNumIsError);
        }
        if (!isValidPageSize(pageSize)) {
            return JsonResult.fail(ErrorCodeEnum.BUSINESS_EXCEPTION_INVALID_PARAMETERS.getCode(),
                    ErrorMessage.pageSizeIsError);
        }
        PagedList<ProjectTypeDto> pagedListDto = projectTypeService.searchPagedProjectTypes(pageNum, pageSize, topType,
                searchString);
        PagedList<ProjectTypeVo> pagedListVo = CommonConverter.mapPageList(pagedListDto, ProjectTypeVo.class);

        return JsonResult.success(pagedListVo);
    }

    @RequestMapping("/listByTopType")
    @ResponseBody
    public JsonResult<List<ProjectTypeVo>> doListByTopType(Integer topType) {
        if (topType == null) {
            return JsonResult.fail(ErrorCodeEnum.BUSINESS_EXCEPTION_INVALID_PARAMETERS.getCode(),
                    ErrorMessage.projectTopTypeIsNull);
        }
        List<ProjectTypeVo> vos = projectTypeService.listByTopType(topType);
        return JsonResult.success(vos);
    }

    @RequestMapping("/insert")
    @ResponseBody
    public JsonResult<Integer> insert(ProjectTypeDto dto) {
        String error = checkProjectType(dto);
        if (!Strings.isNullOrEmpty(error)) {
            return JsonResult.fail(ErrorCodeEnum.BUSINESS_EXCEPTION_INVALID_PARAMETERS.getCode(), error);
        }
        dto.setStoreId(ThreadContext.getStaffStoreId());
        int i = projectTypeService.insert(dto);
        return JsonResult.success(i);
    }

    private String checkProjectType(ProjectTypeDto dto) {
        if (dto == null) {
            return ErrorMessage.paramIsNull;
        }
        if (dto.getTopType() == null) {
            return ErrorMessage.projectTopTypeIsNull;
        }
        String desc = EnumUtils.getDescByKey(ProjectTypeEnum.class, dto.getTopType());
        if (Strings.isNullOrEmpty(desc)) {
            return ErrorMessage.projectTopTypeIsError;
        }
        if (Strings.isNullOrEmpty(dto.getTypeName())) {
            return ErrorMessage.projectTypeNameIsNull;
        }
        return null;
    }

    @RequestMapping("/update")
    @ResponseBody
    public JsonResult<Integer> update(ProjectTypeDto dto) {
        String error = checkProjectType(dto);
        if (!Strings.isNullOrEmpty(error)) {
            return JsonResult.fail(ErrorCodeEnum.BUSINESS_EXCEPTION_INVALID_PARAMETERS.getCode(), error);
        }
        if (dto.getId() == null) {
            return JsonResult.fail(ErrorCodeEnum.BUSINESS_EXCEPTION_INVALID_PARAMETERS.getCode(),
                    ErrorMessage.projectTypeIdIsNull);
        }
        int i = projectTypeService.update(dto);
        return JsonResult.success(i);
    }

    @RequestMapping("/delete")
    @ResponseBody
    public JsonResult<Integer> delete(Long[] id) {
        if (id == null || id.length == 0) {
            return JsonResult.fail(ErrorCodeEnum.BUSINESS_EXCEPTION_INVALID_PARAMETERS.getCode(),
                    ErrorMessage.projectTypeNotSelectedForDelete);
        }
        int i = projectTypeService.deleteByIds(id);
        return JsonResult.success(i);
    }
}
