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
import com.zes.squad.gmh.web.entity.dto.StockDto;
import com.zes.squad.gmh.web.entity.dto.StockTypeDto;
import com.zes.squad.gmh.web.entity.vo.StockTypeVo;
import com.zes.squad.gmh.web.entity.vo.StockVo;
import com.zes.squad.gmh.web.service.StockService;

@RequestMapping("/stock")
@Controller
public class StockController {
	@Autowired
	private StockService stockService;
	

	@RequestMapping("/search")
	@ResponseBody
	public JsonResult<?> search(Integer pageNum, Integer pageSize,Long typeId, String searchString){
		PagedList<StockDto> pagedListDto = stockService.searchListByPage(pageNum, pageSize,typeId,searchString);
        PagedList<StockVo> pagedListVo = CommonConverter.mapPageList(pagedListDto, StockVo.class);
		

		return JsonResult.success(pagedListVo);
	}
	@RequestMapping("/getByType")
	@ResponseBody
	public JsonResult<?> getBytype(Long typeId){
		List<StockVo> voList = new ArrayList<StockVo>();
		voList = stockService.getBytype(typeId);	
		return JsonResult.success(voList);
	}
	@RequestMapping("/insert")
	@ResponseBody
	public JsonResult<?> insert(StockDto dto){
		
		int i = stockService.insert(dto);
		if(i>0)
			return JsonResult.success(i);
		else
			return JsonResult.fail(10006, "新增失败");	
	}
	@RequestMapping("/update")
	@ResponseBody
	public JsonResult<?> update(StockDto dto){
		int i = stockService.update(dto);
		if(i>0)
			return JsonResult.success(i);
		else
			return JsonResult.fail(10006, "修改失败");
	}
	@RequestMapping("/delete")
	@ResponseBody
	public JsonResult<?> delete(Long[] id){
		int i = 0;
		i = stockService.delByIds(id);
		if(i>0)
			return JsonResult.success(i);
		else 
			return JsonResult.fail(10006, "发生错误，没有数据被修改");
	}
	
}
