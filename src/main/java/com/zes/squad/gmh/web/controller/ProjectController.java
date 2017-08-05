package com.zes.squad.gmh.web.controller;

import static com.zes.squad.gmh.web.helper.CheckHelper.isValidPageNum;
import static com.zes.squad.gmh.web.helper.CheckHelper.isValidPageSize;

import java.util.List;

import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.google.common.base.Strings;
import com.google.common.collect.Lists;
import com.zes.squad.gmh.common.converter.CommonConverter;
import com.zes.squad.gmh.common.entity.PagedList;
import com.zes.squad.gmh.common.entity.PagedLists;
import com.zes.squad.gmh.common.enums.ProjectTypeEnum;
import com.zes.squad.gmh.common.exception.ErrorCodeEnum;
import com.zes.squad.gmh.common.exception.ErrorMessage;
import com.zes.squad.gmh.common.util.EnumUtils;
import com.zes.squad.gmh.web.common.JsonResult;
import com.zes.squad.gmh.web.entity.dto.ProjectDto;
import com.zes.squad.gmh.web.entity.vo.ProjectVo;
import com.zes.squad.gmh.web.helper.LogicHelper;
import com.zes.squad.gmh.web.service.ProjectService;

@RequestMapping("/project")
@Controller
public class ProjectController {
    @Autowired
    private ProjectService projectService;

    @RequestMapping("/listAll")
    @ResponseBody
    public JsonResult<List<ProjectVo>> doListProjects() {
        List<ProjectVo> vos = projectService.listProjects();
        return JsonResult.success(vos);
    }

    @RequestMapping("/search")
    @ResponseBody
    public JsonResult<PagedList<ProjectVo>> doListByPage(Integer pageNum, Integer pageSize, Integer topType,
                                                         Long projectType, String searchString) {
        if (!isValidPageNum(pageNum)) {
            return JsonResult.fail(ErrorCodeEnum.BUSINESS_EXCEPTION_INVALID_PARAMETERS.getCode(),
                    ErrorMessage.pageNumIsError);
        }
        if (!isValidPageSize(pageSize)) {
            return JsonResult.fail(ErrorCodeEnum.BUSINESS_EXCEPTION_INVALID_PARAMETERS.getCode(),
                    ErrorMessage.pageSizeIsError);
        }
        LogicHelper.ensureParameterExist(topType, ErrorMessage.projectTopTypeIsNull);
        if (topType != 0) {
            LogicHelper.ensureParameterValid(
                    !Strings.isNullOrEmpty(EnumUtils.getDescByKey(ProjectTypeEnum.class, topType)),
                    ErrorMessage.projectTopTypeIsError);
        }
        PagedList<ProjectDto> pagedDtos = projectService.search(pageNum, pageSize, topType, projectType, searchString);
        if (pagedDtos == null || CollectionUtils.isEmpty(pagedDtos.getData())) {
            return JsonResult.success(PagedLists.newPagedList(pageNum, pageSize, 0L, Lists.newArrayList()));
        }
        List<ProjectVo> vos = buildProjectVosByDtos(pagedDtos.getData());
        PagedList<ProjectVo> pagedVos = PagedLists.newPagedList(pagedDtos.getPageNum(), pagedDtos.getPageSize(),
                pagedDtos.getTotalCount(), vos);

        return JsonResult.success(pagedVos);
    }

    @RequestMapping("/getByType")
    @ResponseBody
    public JsonResult<List<ProjectVo>> doListByType(Long typeId) {
        if (typeId == null) {
            return JsonResult.fail(ErrorCodeEnum.BUSINESS_EXCEPTION_INVALID_PARAMETERS.getCode(),
                    ErrorMessage.projectTypeIdIsNull);
        }
        List<ProjectVo> vos = projectService.listByType(typeId);
        return JsonResult.success(vos);
    }

    @RequestMapping("/insert")
    @ResponseBody
    public JsonResult<Integer> insert(ProjectDto dto) {
        String error = checkProjectDto(dto);
        if (!Strings.isNullOrEmpty(error)) {
            return JsonResult.fail(ErrorCodeEnum.BUSINESS_EXCEPTION_INVALID_PARAMETERS.getCode(), error);
        }
        int i = projectService.insert(dto);
        return JsonResult.success(i);
    }

    @RequestMapping("/update")
    @ResponseBody
    public JsonResult<Integer> update(ProjectDto dto) {
        String error = checkProjectDto(dto);
        if (!Strings.isNullOrEmpty(error)) {
            return JsonResult.fail(ErrorCodeEnum.BUSINESS_EXCEPTION_INVALID_PARAMETERS.getCode(), error);
        }
        if (dto.getId() == null) {
            return JsonResult.fail(ErrorCodeEnum.BUSINESS_EXCEPTION_INVALID_PARAMETERS.getCode(),
                    ErrorMessage.projectIdIsNull);
        }
        int i = projectService.update(dto);
        return JsonResult.success(i);
    }

    @RequestMapping("/delete")
    @ResponseBody
    public JsonResult<Integer> delete(Long[] id) {
        if (id == null || id.length == 0) {
            return JsonResult.fail(ErrorCodeEnum.BUSINESS_EXCEPTION_INVALID_PARAMETERS.getCode(),
                    ErrorMessage.projectNotSelectedForDelete);
        }
        int i = projectService.deleteByIds(id);
        return JsonResult.success(i);
    }

    private List<ProjectVo> buildProjectVosByDtos(List<ProjectDto> dtos) {
        List<ProjectVo> vos = Lists.newArrayList();
        for (ProjectDto dto : dtos) {
            ProjectVo vo = CommonConverter.map(dto, ProjectVo.class);
            vo.setTopTypeName(EnumUtils.getDescByKey(ProjectTypeEnum.class, dto.getTopType()));
            vo.setTypeId(dto.getProjectTypeId());
            vos.add(vo);
        }
        return vos;
    }

    private String checkProjectDto(ProjectDto dto) {
        if (dto == null) {
            return ErrorMessage.paramIsNull;
        }
        if (dto.getProjectTypeId() == null) {
            return ErrorMessage.projectTypeIdIsNull;
        }
        if (Strings.isNullOrEmpty(dto.getProjectName())) {
            return ErrorMessage.projectNameIsNull;
        }
        if (dto.getRetailPrice() == null) {
            return ErrorMessage.projectRetailPriceIsNull;
        }
        return null;
    }

}
