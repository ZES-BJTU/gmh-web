package com.zes.squad.gmh.web.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.zes.squad.gmh.web.mapper.EmployeeJobMapper;
import com.zes.squad.gmh.web.service.EmployeeJobService;

@Service("employeeJobService")
public class EmployeeJobServiceImpl implements EmployeeJobService{

	@Autowired
	private EmployeeJobMapper emJobMapper;
	
	public int insert(Long emId,Long[] jobId){
		int job = 0;
		for(int i=0;i<jobId.length;i++){
			job = job + emJobMapper.insert(emId, jobId[i]);
		}
		return job;
	}
	public int delByEmId(Long emId){
		int i = emJobMapper.delByEmId(emId);
		return i;
	}
}
