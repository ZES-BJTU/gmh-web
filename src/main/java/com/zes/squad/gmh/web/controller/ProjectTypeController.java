package com.zes.squad.gmh.web.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.zes.squad.gmh.web.common.JsonResult;
import com.zes.squad.gmh.web.entity.dto.ProjectTypeDto;
import com.zes.squad.gmh.web.entity.vo.ProjectTypeVo;
import com.zes.squad.gmh.web.service.ProjectTypeService;

@RequestMapping("/projectType")
@Controller
public class ProjectTypeController {

	@Autowired
	private ProjectTypeService ptService;
	
	@RequestMapping("/getAll")
	@ResponseBody
	public JsonResult<?> getAll(){
		List<ProjectTypeVo> voList = new ArrayList<ProjectTypeVo>();
		voList = ptService.getAll();		
		return JsonResult.success(voList);
	}
	@RequestMapping("/insert")
	@ResponseBody
	public JsonResult<?> insert(ProjectTypeDto dto){
		int i = ptService.insert(dto);
		if(i>0)
			return JsonResult.success(i);
		else
			return JsonResult.fail(0, "新增失败");	
	}
	@RequestMapping("/update")
	@ResponseBody
	public JsonResult<?> update(ProjectTypeDto dto){
		int i = ptService.update(dto);
		if(i>0)
			return JsonResult.success(i);
		else
			return JsonResult.fail(0, "修改失败");
	}
	@RequestMapping("/delete")
	@ResponseBody
	public JsonResult<?> delete(Long[] id){
		int i = 0;
		i = ptService.delByIds(id);
		if(i>0)
			return JsonResult.success(i);
		else 
			return JsonResult.fail(0, "发生错误，没有数据被修改");
	}
}
