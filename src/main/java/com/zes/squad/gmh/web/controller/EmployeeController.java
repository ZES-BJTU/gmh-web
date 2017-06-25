package com.zes.squad.gmh.web.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.zes.squad.gmh.web.common.JsonResult;
import com.zes.squad.gmh.web.entity.vo.EmployeeVo;
import com.zes.squad.gmh.web.service.EmployeeService;

@RequestMapping("/employee")
@Controller
public class EmployeeController {
	@Autowired
    private EmployeeService employeeService;
	
	@RequestMapping("/getAll")
	@ResponseBody
	public JsonResult<?> getAll(){
		List<EmployeeVo> voList = new ArrayList<EmployeeVo>();
		voList = employeeService.getAll();
		return JsonResult.success(voList);
	}
}
