package com.zes.squad.gmh.web.mapper;

import java.util.List;

import com.zes.squad.gmh.web.entity.po.AppointmentPo;
import com.zes.squad.gmh.web.entity.vo.AppointmentVo;

public interface AppointmentMapper {
	List<AppointmentVo> getAll(Long storeId);
	AppointmentVo getByPhone(String phone);
	int insert(AppointmentPo po);
	int update(AppointmentPo po);
	int cancel(Long id);
	int finish(Long id);
}
