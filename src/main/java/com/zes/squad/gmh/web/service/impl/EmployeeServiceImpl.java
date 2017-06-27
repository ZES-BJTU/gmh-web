package com.zes.squad.gmh.web.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.zes.squad.gmh.common.converter.CommonConverter;
import com.zes.squad.gmh.web.entity.dto.EmployeeDto;
import com.zes.squad.gmh.web.entity.po.EmployeePo;
import com.zes.squad.gmh.web.entity.vo.EmployeeVo;
import com.zes.squad.gmh.web.mapper.EmployeeJobMapper;
import com.zes.squad.gmh.web.mapper.EmployeeMapper;
import com.zes.squad.gmh.web.service.EmployeeService;

@Service("employeeService")
public class EmployeeServiceImpl implements EmployeeService{

	@Autowired
	private EmployeeMapper emMapper;
	@Autowired
	private EmployeeJobMapper emJobMapper;
	
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
		Date entry = new Date();
		po.setEntryDate(entry);
		emMapper.insert(po);	
		EmployeeDto newDto = CommonConverter.map(po, EmployeeDto.class);
		return newDto;
	}
	public int leave(Long[] id){
		int i = 0;
		Date leave = new Date();
		Map map = new HashMap();
		
		for(int j=0;j<id.length;j++){
			map.put("id", id[j]);
			map.put("leave", leave);
			i = i + emMapper.leave(map);
		}
		return i;		
	}
	public int update(EmployeeDto dto,Long[] jobId){
		EmployeePo po = CommonConverter.map(dto, EmployeePo.class);
		int i = emMapper.update(po);
		if(i>0){
			int j = emJobMapper.delByEmId(dto.getId());
			if(j>0){
				for(int m=0;m<jobId.length;m++){
					emJobMapper.insert(dto.getId(), jobId[m]);
				}
			}
		}
		return i;
	}
}
