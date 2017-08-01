package com.zes.squad.gmh.web.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.zes.squad.gmh.common.converter.CommonConverter;
import com.zes.squad.gmh.common.entity.PagedList;
import com.zes.squad.gmh.common.exception.ErrorCodeEnum;
import com.zes.squad.gmh.web.common.JsonResult;
import com.zes.squad.gmh.web.entity.dto.ProjectDto;
import com.zes.squad.gmh.web.entity.vo.ProjectVo;
import com.zes.squad.gmh.web.service.ProjectService;

@RequestMapping("/project")
@Controller
public class ProjectController {
    @Autowired
    private ProjectService projectService;

    @RequestMapping("/getAll")
    @ResponseBody
    public JsonResult<List<ProjectVo>> getAll() {
        List<ProjectVo> vos = projectService.getAll();
        return JsonResult.success(vos);
    }

    @RequestMapping("/listByPage")
    @ResponseBody
    public JsonResult<PagedList<ProjectVo>> doListByPage(Integer pageNum, Integer pageSize) {
        if (pageNum == null || pageNum < 0) {
            return JsonResult.fail(ErrorCodeEnum.BUSINESS_EXCEPTION_INVALID_PARAMETERS.getCode(), "分页页码错误");
        }
        if (pageSize == null || pageSize < 0) {
            return JsonResult.fail(ErrorCodeEnum.BUSINESS_EXCEPTION_INVALID_PARAMETERS.getCode(), "分页大小错误");
        }
        PagedList<ProjectDto> pagedListDto = projectService.listByPage(pageNum, pageSize);
        PagedList<ProjectVo> pagedListVo = CommonConverter.mapPageList(pagedListDto, ProjectVo.class);

        return JsonResult.success(pagedListVo);
    }

    @RequestMapping("/getByType")
    @ResponseBody
    public JsonResult<List<ProjectVo>> getBytype(Long typeId) {
        if (typeId == null) {
            return JsonResult.fail(ErrorCodeEnum.BUSINESS_EXCEPTION_INVALID_PARAMETERS.getCode(), "美容项分类标识不能为空");
        }
        List<ProjectVo> vos = projectService.getByType(typeId);
        return JsonResult.success(vos);
    }

    @RequestMapping("/insert")
    @ResponseBody
    public JsonResult<?> insert(ProjectDto dto) {
        if (dto == null) {
            return JsonResult.fail(ErrorCodeEnum.BUSINESS_EXCEPTION_INVALID_PARAMETERS.getCode(), "美容项目信息不能为空");
        }
        int i = projectService.insert(dto);
        if (i > 0) {
            return JsonResult.success(i);
        } else {
            return JsonResult.fail(ErrorCodeEnum.BUSINESS_EXCEPTION_INVALID_PARAMETERS.getCode(), "添加美容项目失败");
        }
    }

    @RequestMapping("/update")
    @ResponseBody
    public JsonResult<?> update(ProjectDto dto) {
        if (dto == null || dto.getId() == null) {
            return JsonResult.fail(ErrorCodeEnum.BUSINESS_EXCEPTION_INVALID_PARAMETERS.getCode(), "美容项目信息不能为空");
        }
        int i = projectService.update(dto);
        if (i > 0) {
            return JsonResult.success(i);
        } else {
            return JsonResult.fail(ErrorCodeEnum.BUSINESS_EXCEPTION_INVALID_PARAMETERS.getCode(), "修改美容项目失败");
        }
    }

    @RequestMapping("/delete")
    @ResponseBody
    public JsonResult<?> delete(Long[] id) {
        if (id == null || id.length == 0) {
            return JsonResult.fail(ErrorCodeEnum.BUSINESS_EXCEPTION_INVALID_PARAMETERS.getCode(), "请选择要删除的美容项目");
        }
        int i = 0;
        i = projectService.delByIds(id);
        if (i > 0) {
            return JsonResult.success(i);
        } else {
            return JsonResult.fail(ErrorCodeEnum.BUSINESS_EXCEPTION_INVALID_PARAMETERS.getCode(), "删除美容项目失败");
        }
    }

}
