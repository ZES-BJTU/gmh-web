package com.zes.squad.gmh.web.mapper;

import java.util.Date;
import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.zes.squad.gmh.web.entity.po.AppointmentPo;
import com.zes.squad.gmh.web.entity.vo.AppointmentVo;

public interface AppointmentMapper {

    List<AppointmentVo> getAll(Long storeId);

    AppointmentVo getByPhone(String phone);

    int insert(AppointmentPo po);

    int update(AppointmentPo po);

    int cancel(@Param("id") Long id, @Param("status") Integer status);

    int finish(@Param("id") Long id, @Param("status") Integer status);

    AppointmentPo selectByCondition(@Param("storeId") Long storeId, @Param("EmployeeId") Long EmployeeId,
                                    @Param("status") Integer status, @Param("beginTime") Date beginTime,
                                    @Param("endTime") Date endTime);
    
    AppointmentPo selectById(Long id);

}
