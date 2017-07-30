package com.zes.squad.gmh.web.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.zes.squad.gmh.web.entity.po.EmployeeJobPo;

public interface EmployeeJobMapper {

    /**
     * 批量插入
     * 
     * @param pos
     * @return
     */
    int batchInsert(List<EmployeeJobPo> pos);

    int insert(@Param("employeeId") Long employeeId, @Param("jobType") Long jobType);

    int insert(EmployeeJobPo po);

    int delteByEmployeeId(Long employeeId);
}
