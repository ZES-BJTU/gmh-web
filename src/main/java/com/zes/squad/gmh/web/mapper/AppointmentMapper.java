package com.zes.squad.gmh.web.mapper;

import java.util.Date;
import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.zes.squad.gmh.web.entity.po.AppointmentPo;

public interface AppointmentMapper {

    int insert(AppointmentPo po);

    int updateSelective(AppointmentPo po);

    int updateForStart(@Param("id") Long id, @Param("status") Integer status);

    int updateForCancel(@Param("id") Long id, @Param("status") Integer status);

    int updateForFinish(@Param("id") Long id, @Param("status") Integer status);

    int selectByCondition(@Param("storeId") Long storeId, @Param("phone") String phone,
                          @Param("employeeId") Long employeeId, @Param("status") List<Integer> status,
                          @Param("beginTime") Date beginTime, @Param("endTime") Date endTime);

    AppointmentPo selectById(Long id);

    int deleteById(Long id);

    List<AppointmentPo> selectByEmployeeAndTime(@Param("time") Date time, @Param("employeeId") Long employeeId,
                                                @Param("startTime") Date startTime, @Param("endTime") Date endTime);

}
