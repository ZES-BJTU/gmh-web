package com.zes.squad.gmh.web.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.zes.squad.gmh.common.converter.CommonConverter;
import com.zes.squad.gmh.web.common.JsonResult;
import com.zes.squad.gmh.web.entity.dto.ShopDto;
import com.zes.squad.gmh.web.entity.param.ShopParam;
import com.zes.squad.gmh.web.entity.po.ShopPo;
import com.zes.squad.gmh.web.service.ShopService;

@RequestMapping("/shop")
@Controller
public class ShopController {

	@Autowired
    private ShopService shopService;
	
	@RequestMapping("/getAll")
    @ResponseBody
    public JsonResult<?> getAll(){
		
        List<ShopPo> poList = new ArrayList<ShopPo>();
        poList = shopService.getAll();
    	return JsonResult.success(poList);
    }
	
	@RequestMapping("/insert")
    @ResponseBody
    public JsonResult<?> insert(ShopParam param){
  
        ShopDto dto = CommonConverter.map(param, ShopDto.class);
        int i = shopService.insert(dto);
    	return JsonResult.success(i);
    }
	@RequestMapping("/update")
    @ResponseBody
    public JsonResult<?> update(ShopParam param){
  
        ShopDto dto = CommonConverter.map(param, ShopDto.class);
        int i = shopService.update(dto);
    	return JsonResult.success(i);
    }
	
	@RequestMapping("/delByIds")
	@ResponseBody
	public JsonResult<?> delByIds(Long[] id){
		int i = shopService.delByIds(id);
		return JsonResult.success(i);
	}
	
	
}
