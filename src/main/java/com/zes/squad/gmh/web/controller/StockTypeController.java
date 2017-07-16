package com.zes.squad.gmh.web.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.zes.squad.gmh.web.common.JsonResult;
import com.zes.squad.gmh.web.context.ThreadContext;
import com.zes.squad.gmh.web.entity.dto.StaffDto;
import com.zes.squad.gmh.web.entity.dto.StockTypeDto;
import com.zes.squad.gmh.web.entity.vo.StockTypeVo;
import com.zes.squad.gmh.web.service.StockTypeService;

@RequestMapping("/stockType")
@Controller
public class StockTypeController {

	@Autowired
	private StockTypeService stService;
	
	@RequestMapping("/getAll")
	@ResponseBody
	public JsonResult<?> getAll(){
		List<StockTypeVo> voList = new ArrayList<StockTypeVo>();
		voList = stService.getAll();		
		return JsonResult.success(voList);
	}
	@RequestMapping("/insert")
	@ResponseBody
	public JsonResult<?> insert(StockTypeDto dto){
		StaffDto staffDto = ThreadContext.getCurrentStaff();
		dto.setStoreId(staffDto.getStoreId());
		int i = stService.insert(dto);
		if(i>0)
			return JsonResult.success(i);
		else
			return JsonResult.fail(10006, "新增失败");	
	}
	@RequestMapping("/update")
	@ResponseBody
	public JsonResult<?> update(StockTypeDto dto){
		int i = stService.update(dto);
		if(i>0)
			return JsonResult.success(i);
		else
			return JsonResult.fail(10006, "修改失败");
	}
	@RequestMapping("/delete")
	@ResponseBody
	public JsonResult<?> delete(Long[] id){
		int i = 0;
		i = stService.delByIds(id);
		if(i>0)
			return JsonResult.success(i);
		else 
			return JsonResult.fail(10006, "发生错误，没有数据被修改");
	}
}
