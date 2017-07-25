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
import com.zes.squad.gmh.web.entity.dto.ProjectTypeDto;
import com.zes.squad.gmh.web.entity.dto.StaffDto;
import com.zes.squad.gmh.web.entity.dto.StockTypeDto;
import com.zes.squad.gmh.web.entity.vo.ProjectTypeVo;
import com.zes.squad.gmh.web.entity.vo.StockTypeVo;
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
	@RequestMapping("/search")
	@ResponseBody
	public JsonResult<?> search(Integer pageNum, Integer pageSize,String searchString){
		PagedList<ProjectTypeDto> pagedListDto = ptService.searchListByPage(pageNum, pageSize,searchString);
        PagedList<ProjectTypeVo> pagedListVo = CommonConverter.mapPageList(pagedListDto, ProjectTypeVo.class);
		
		return JsonResult.success(pagedListVo);
	}
	@RequestMapping("/getByTopType")
	@ResponseBody
	public JsonResult<?> getByTopType(int topType){
		List<ProjectTypeVo> voList = new ArrayList<ProjectTypeVo>();
		voList = ptService.getByTopType(topType);		
		return JsonResult.success(voList);
	}
	@RequestMapping("/insert")
	@ResponseBody
	public JsonResult<?> insert(ProjectTypeDto dto){
		StaffDto staffDto = ThreadContext.getCurrentStaff();
		dto.setStoreId(staffDto.getStoreId());
		int i = ptService.insert(dto);
		if(i>0)
			return JsonResult.success(i);
		else
			return JsonResult.fail(10006, "新增失败");	
	}
	@RequestMapping("/update")
	@ResponseBody
	public JsonResult<?> update(ProjectTypeDto dto){
		int i = ptService.update(dto);
		if(i>0)
			return JsonResult.success(i);
		else
			return JsonResult.fail(10006, "修改失败");
	}
	@RequestMapping("/delete")
	@ResponseBody
	public JsonResult<?> delete(Long[] id){
		int i = 0;
		i = ptService.delByIds(id);
		if(i>0)
			return JsonResult.success(i);
		else 
			return JsonResult.fail(10006, "发生错误，没有数据被修改");
	}
}
