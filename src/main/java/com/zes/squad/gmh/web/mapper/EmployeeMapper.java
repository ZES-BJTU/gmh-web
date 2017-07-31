package com.zes.squad.gmh.web.mapper;

import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import com.zes.squad.gmh.web.entity.po.EmployeePo;

public interface EmployeeMapper {

    List<EmployeePo> listByPage(@Param("shopId") Long shopId);

    Long insert(EmployeePo po);

    int leave(Map<String, Object> map);

    /**
     * 批量更新离职
     * 
     * @param leaveDate
     * @param work
     * @param ids
     * @return
     */
    int batchUpdateWork(@Param("leaveDate") Date leaveDate, @Param("work") Boolean work, @Param("ids") Long[] ids);

    int update(EmployeePo po);

    List<EmployeePo> search(Map<String, Object> map);

    EmployeePo selectById(@Param("storeId") Long storeId, @Param("id") Long id);
}
