package com.zes.squad.gmh.web.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.zes.squad.gmh.common.converter.CommonConverter;
import com.zes.squad.gmh.web.common.JsonResult;
import com.zes.squad.gmh.web.context.ThreadContext;
import com.zes.squad.gmh.web.entity.dto.EmployeeDto;
import com.zes.squad.gmh.web.entity.dto.StaffDto;
import com.zes.squad.gmh.web.entity.param.EmployeeParam;
import com.zes.squad.gmh.web.entity.vo.EmployeeVo;
import com.zes.squad.gmh.web.service.EmployeeJobService;
import com.zes.squad.gmh.web.service.EmployeeService;

@RequestMapping("/employee")
@Controller
public class EmployeeController {
	@Autowired
    private EmployeeService employeeService;
	@Autowired
    private EmployeeJobService employeeJobService;
	@RequestMapping("/getAll")
	@ResponseBody
	public JsonResult<?> getAll(){
		StaffDto staffDto = ThreadContext.getCurrentStaff();
		
		List<EmployeeVo> voList = new ArrayList<EmployeeVo>();
		voList = employeeService.getAll();
		return JsonResult.success(voList);
	}
	
	@RequestMapping("/insert")
	@ResponseBody
	public JsonResult<?> insert(EmployeeParam param,Long[] jobId){
		StaffDto staffDto = ThreadContext.getCurrentStaff();
		EmployeeDto dto = CommonConverter.map(param, EmployeeDto.class);
		dto.setShopId(staffDto.getStoreId());
		EmployeeDto newDto = employeeService.insert(dto);
		if(newDto.getId()!=null)
			employeeJobService.insert(newDto.getId(), jobId);
		EmployeeVo vo = CommonConverter.map(newDto, EmployeeVo.class);
		return JsonResult.success(vo);
	}
	@RequestMapping("/leave")
	@ResponseBody
	public JsonResult<?> leave(Long[] id){
		int i = 0;
		i = employeeService.leave(id);
		return JsonResult.success(i);		
	}
	@RequestMapping("/update")
	@ResponseBody
	public JsonResult<?> update(EmployeeParam param,Long[] jobId){
		EmployeeDto dto = CommonConverter.map(param, EmployeeDto.class);
		int i = employeeService.update(dto, jobId);
		if(i>0)
			return JsonResult.success(i);
		else
			return JsonResult.fail(i, "error");
		
	}
}
