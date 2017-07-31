package com.zes.squad.gmh.web.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.zes.squad.gmh.web.entity.union.AppointmentUnion;

public interface AppointmentUnionMapper {

    /**
     * 门店id
     * 
     * @param storeId
     * @return
     */
    List<AppointmentUnion> listAppointmentUnionsByCondition(@Param("storeId") Long storeId,
                                                            @Param("phone") String phone);

}
