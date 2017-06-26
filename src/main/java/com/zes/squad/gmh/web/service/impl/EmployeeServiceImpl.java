package com.zes.squad.gmh.web.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.zes.squad.gmh.common.converter.CommonConverter;
import com.zes.squad.gmh.web.entity.dto.EmployeeDto;
import com.zes.squad.gmh.web.entity.po.EmployeePo;
import com.zes.squad.gmh.web.entity.vo.EmployeeVo;
import com.zes.squad.gmh.web.mapper.EmployeeMapper;
import com.zes.squad.gmh.web.service.EmployeeService;

@Service("employeeService")
public class EmployeeServiceImpl implements EmployeeService{

	@Autowired
	private EmployeeMapper emMapper;
	
	public List<EmployeeVo> getAll(){
		List<EmployeePo> poList = new ArrayList<EmployeePo>();
		poList = emMapper.getAll();
		List<EmployeeVo> voList = new ArrayList<EmployeeVo>();
		if(poList.size()==0)
			return null;
		for(int i=0;i<poList.size();i++){
			voList.add(CommonConverter.map(poList.get(i),EmployeeVo.class));
		}
		return voList;
	}
	public EmployeeDto insert(EmployeeDto dto){
		EmployeePo po = CommonConverter.map(dto, EmployeePo.class);
		Long i = emMapper.insert(po);
		EmployeeDto newDto = CommonConverter.map(po, EmployeeDto.class);
		return newDto;
	}
	
}
