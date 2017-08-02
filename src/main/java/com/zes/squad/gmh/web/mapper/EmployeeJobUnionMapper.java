package com.zes.squad.gmh.web.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.zes.squad.gmh.web.entity.condition.EmployeeJobQueryCondition;
import com.zes.squad.gmh.web.entity.union.EmployeeJobUnion;

public interface EmployeeJobUnionMapper {

    /**
     * 查询id集合
     * 
     * @param condition
     * @return
     */
    List<Long> selectIdsByCondition(EmployeeJobQueryCondition condition);

    /**
     * 全表分页查询
     * 
     * @param ids
     * @return
     */
    List<EmployeeJobUnion> listEmployeeJobUnionsByCondition(@Param("ids") List<Long> ids);

}
