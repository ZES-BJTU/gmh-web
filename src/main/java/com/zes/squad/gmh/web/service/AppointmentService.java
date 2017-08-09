package com.zes.squad.gmh.web.service;

import java.math.BigDecimal;
import java.util.List;

import com.zes.squad.gmh.common.entity.PagedList;
import com.zes.squad.gmh.web.entity.condition.AppointmentQueryCondition;
import com.zes.squad.gmh.web.entity.dto.AppointmentDto;
import com.zes.squad.gmh.web.entity.union.AppointmentUnion;
import com.zes.squad.gmh.web.entity.vo.AppointmentVo;
import com.zes.squad.gmh.web.entity.vo.EmployeeItemVo;

public interface AppointmentService {

    List<AppointmentVo> listAllAppointments();

    PagedList<AppointmentVo> searchPagedAppointments(AppointmentQueryCondition condition);

    List<AppointmentVo> queryByPhone(String phone);

    AppointmentUnion queryById(Long id);

    int insert(AppointmentDto dto);

    int update(AppointmentDto dto);

    int cancel(Long id);
    
    int start(Long id);

    int finish(Long id, BigDecimal charge, Integer chargeWay,Long counselorId, String source, String remark);

    List<AppointmentVo> remind();

    List<EmployeeItemVo> listEmployeesByProject(Long projectId);

    List<AppointmentVo> listAppointmentsByEmployee(Long employeeId);
    
}
