package com.zes.squad.gmh.web.service;

public interface EmployeeJobService {

    int insert(Long emId, Long[] jobId);

    int delByEmId(Long emId);
}
