package com.zes.squad.gmh.web.mapper;

public interface EmployeeJobMapper {

	int insert(Long emId,Long jobId);
	int delByEmId(Long emId);
}
