package com.zes.squad.gmh.web.service;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import com.zes.squad.gmh.web.entity.dto.AppointmentDto;
import com.zes.squad.gmh.web.entity.vo.AppointmentVo;

public interface AppointmentService {

    List<AppointmentVo> listAllAppoints();

    AppointmentVo queryByPhone(String phone);

    int insert(AppointmentDto dto);

    int update(AppointmentDto dto);

    int cancel(Long id);

    int finish(Long id, BigDecimal charge, Integer chargeWay);

    boolean isConflict(Long storeId, Long EmployeeId, Date beginTime, Date endTime);
}
