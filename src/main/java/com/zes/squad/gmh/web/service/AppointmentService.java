package com.zes.squad.gmh.web.service;

import java.util.List;

import com.zes.squad.gmh.web.entity.dto.AppointmentDto;
import com.zes.squad.gmh.web.entity.vo.AppointmentVo;

public interface AppointmentService {

	List<AppointmentVo> getAll();
	AppointmentVo getByPhone(String phone);
	int insert(AppointmentDto dto);
	int update(AppointmentDto dto);
	int cancel(Long id);
	int finish(Long id);
	boolean isConflict();
}
