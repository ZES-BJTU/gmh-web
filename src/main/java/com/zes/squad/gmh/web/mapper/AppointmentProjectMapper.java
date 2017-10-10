package com.zes.squad.gmh.web.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.zes.squad.gmh.web.entity.po.AppointmentProjectPo;

public interface AppointmentProjectMapper {

    /**
     * 批量插入
     * 
     * @param pos
     * @return
     */
    int batchInsert(List<AppointmentProjectPo> pos);

    /**
     * 删除
     * 
     * @param appointmentId
     * @return
     */
    int deleteByAppointmentId(@Param("appointmentId") Long appointmentId);

}
