package com.zes.squad.gmh.web.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.zes.squad.gmh.web.entity.union.EmployeeJobUnion;

public interface EmployeeJobUnionMapper {

    /**
     * 全表分页查询
     * 
     * @param ids
     * @return
     */
    List<EmployeeJobUnion> listEmployeeJobUnionsByCondition(@Param("ids") List<Long> ids);

}
