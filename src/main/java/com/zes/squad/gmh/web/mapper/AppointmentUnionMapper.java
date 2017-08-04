package com.zes.squad.gmh.web.mapper;

import java.util.List;

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

}
