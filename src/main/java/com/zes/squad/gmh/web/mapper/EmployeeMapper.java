package com.zes.squad.gmh.web.mapper;

import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import com.zes.squad.gmh.web.entity.condition.EmployeeJobQueryCondition;
import com.zes.squad.gmh.web.entity.po.EmployeePo;

public interface EmployeeMapper {

    /**
     * 查询id集合
     * 
     * @param condition
     * @return
     */
    List<Long> selectIdsByCondition(EmployeeJobQueryCondition condition);

    /**
     * 插入
     * 
     * @param po
     * @return
     */
    int insert(EmployeePo po);

    @Deprecated
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

    /**
     * 更新非空字段
     * 
     * @param po
     * @return
     */
    int updateSelective(EmployeePo po);

    List<EmployeePo> search(Map<String, Object> map);

    EmployeePo selectById(@Param("storeId") Long storeId, @Param("id") Long id);
}
