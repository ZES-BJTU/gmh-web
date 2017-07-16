package com.zes.squad.gmh.web.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.zes.squad.gmh.web.common.JsonResult;
import com.zes.squad.gmh.web.context.ThreadContext;
import com.zes.squad.gmh.web.entity.dto.MemberLevelDto;
import com.zes.squad.gmh.web.entity.dto.StaffDto;
import com.zes.squad.gmh.web.entity.vo.MemberLevelVo;
import com.zes.squad.gmh.web.service.MemberLevelService;

@RequestMapping("/memberLevel")
@Controller
public class MemberLevelController {
	@Autowired
	private MemberLevelService ptService;
	
	@RequestMapping("/getAll")
	@ResponseBody
	public JsonResult<?> getAll(){
		List<MemberLevelVo> voList = new ArrayList<MemberLevelVo>();
		voList = ptService.getAll();		
		return JsonResult.success(voList);
	}
	@RequestMapping("/insert")
	@ResponseBody
	public JsonResult<?> insert(MemberLevelDto dto){
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
	public JsonResult<?> update(MemberLevelDto dto){
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
