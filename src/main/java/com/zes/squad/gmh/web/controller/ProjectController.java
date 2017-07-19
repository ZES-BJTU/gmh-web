package com.zes.squad.gmh.web.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.zes.squad.gmh.common.converter.CommonConverter;
import com.zes.squad.gmh.common.entity.PagedList;
import com.zes.squad.gmh.web.common.JsonResult;
import com.zes.squad.gmh.web.context.ThreadContext;
import com.zes.squad.gmh.web.entity.dto.EmployeeDto;
import com.zes.squad.gmh.web.entity.dto.ProjectDto;
import com.zes.squad.gmh.web.entity.dto.ProjectTypeDto;
import com.zes.squad.gmh.web.entity.dto.StaffDto;
import com.zes.squad.gmh.web.entity.vo.EmployeeVo;
import com.zes.squad.gmh.web.entity.vo.ProjectVo;
import com.zes.squad.gmh.web.service.ProjectService;

@RequestMapping("/project")
@Controller
public class ProjectController {
	@Autowired
	private ProjectService projectService;
	

	@RequestMapping("/getAll")
	@ResponseBody
	public JsonResult<?> getAll(){
		List<ProjectVo> voList = new ArrayList<ProjectVo>();
		voList = projectService.getAll();		
		return JsonResult.success(voList);
	}
	@RequestMapping("/listByPage")
    @ResponseBody
    public JsonResult<PagedList<ProjectVo>> doListByPage(Integer pageNum, Integer pageSize) {

        PagedList<ProjectDto> pagedListDto = projectService.listByPage(pageNum, pageSize);
        PagedList<ProjectVo> pagedListVo = CommonConverter.mapPageList(pagedListDto, ProjectVo.class);

        return JsonResult.success(pagedListVo);
    }

	@RequestMapping("/getByType")
	@ResponseBody
	public JsonResult<?> getBytype(Long typeId){
		List<ProjectVo> voList = new ArrayList<ProjectVo>();
		voList = projectService.getBytype(typeId);	
		return JsonResult.success(voList);
	}
	@RequestMapping("/insert")
	@ResponseBody
	public JsonResult<?> insert(ProjectDto dto){
		
		int i = projectService.insert(dto);
		if(i>0)
			return JsonResult.success(i);
		else
			return JsonResult.fail(10006, "新增失败");	
	}
	@RequestMapping("/update")
	@ResponseBody
	public JsonResult<?> update(ProjectDto dto){
		int i = projectService.update(dto);
		if(i>0)
			return JsonResult.success(i);
		else
			return JsonResult.fail(10006, "修改失败");
	}
	@RequestMapping("/delete")
	@ResponseBody
	public JsonResult<?> delete(Long[] id){
		int i = 0;
		i = projectService.delByIds(id);
		if(i>0)
			return JsonResult.success(i);
		else 
			return JsonResult.fail(10006, "发生错误，没有数据被修改");
	}
	
	
}
