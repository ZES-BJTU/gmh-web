package com.zes.squad.gmh.web.service;

import java.util.List;

import com.zes.squad.gmh.web.entity.dto.EmployeeDto;
import com.zes.squad.gmh.web.entity.vo.EmployeeVo;

public interface EmployeeService {
	
	List<EmployeeVo> getAll();
	EmployeeDto insert(EmployeeDto dto);
}
