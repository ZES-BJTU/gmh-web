package com.zes.squad.gmh.web.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.zes.squad.gmh.web.entity.condition.AppointmentUnionQueryCondition;
import com.zes.squad.gmh.web.entity.union.AppointmentUnion;

public interface AppointmentUnionMapper {

    /**
     * 条件查询预约记录
     * 
     * @param condition
     * @return
     */
    List<AppointmentUnion> listAppointmentUnionsByCondition(AppointmentUnionQueryCondition condition);

    /**
     * 根据id查询
     * 
     * @param id
     * @return
     */
    AppointmentUnion selectById(Long id);

    /**
     * 查询minute时间范围内的待进行的预约信息
     * 
     * @param minute
     * @return
     */
    List<AppointmentUnion> selectByTime(@Param("minute") Integer minute, @Param("status") Integer status,
                                        @Param("storeId") Long storeId);

}
