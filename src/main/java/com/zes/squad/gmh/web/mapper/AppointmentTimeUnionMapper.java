package com.zes.squad.gmh.web.mapper;

import java.util.Date;
import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.zes.squad.gmh.web.entity.union.AppointmentTimeUnion;

public interface AppointmentTimeUnionMapper {

    /**
     * 查询空闲时间
     * 
     * @param time
     * @param employeeId
     * @param startTime
     * @param endTime
     * @return
     */
    List<AppointmentTimeUnion> selectByEmployeeAndTime(@Param("time") Date time, @Param("employeeId") Long employeeId,
                                                       @Param("startTime") Date startTime,
                                                       @Param("endTime") Date endTime);

}
