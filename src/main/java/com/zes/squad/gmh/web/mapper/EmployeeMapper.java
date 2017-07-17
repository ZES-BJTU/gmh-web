package com.zes.squad.gmh.web.mapper;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import com.zes.squad.gmh.web.entity.po.EmployeePo;

public interface EmployeeMapper {

    List<EmployeePo> listByPage(@Param("shopId") Long shopId);

    Long insert(EmployeePo po);

    int leave(Map map);

    int update(EmployeePo po);
}
