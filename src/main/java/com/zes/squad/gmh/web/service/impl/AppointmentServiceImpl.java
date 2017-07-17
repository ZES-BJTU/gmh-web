package com.zes.squad.gmh.web.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.zes.squad.gmh.common.converter.CommonConverter;
import com.zes.squad.gmh.web.context.ThreadContext;
import com.zes.squad.gmh.web.entity.dto.AppointmentDto;
import com.zes.squad.gmh.web.entity.dto.StaffDto;
import com.zes.squad.gmh.web.entity.po.AppointmentPo;
import com.zes.squad.gmh.web.entity.vo.AppointmentVo;
import com.zes.squad.gmh.web.mapper.AppointmentMapper;
import com.zes.squad.gmh.web.service.AppointmentService;
@Service("appointmentJobService")
public class AppointmentServiceImpl implements AppointmentService{

	@Autowired
	private AppointmentMapper appointmentMapper;
	
	@Override
	public List<AppointmentVo> getAll() {
		StaffDto staffDto = ThreadContext.getCurrentStaff();
		List<AppointmentVo> voList = new ArrayList<AppointmentVo>();
		voList = appointmentMapper.getAll(staffDto.getStoreId());
		return voList;
	}

	@Override
	public AppointmentVo getByPhone(String phone) {
		AppointmentVo  vo = appointmentMapper.getByPhone(phone);
		return vo;
	}

	@Override
	public int insert(AppointmentDto dto) {
		AppointmentPo po = CommonConverter.map(dto, AppointmentPo.class);
		
		int i = appointmentMapper.insert(po);	
		return i;
	}

	@Override
	public int update(AppointmentDto dto) {
		AppointmentPo po = CommonConverter.map(dto, AppointmentPo.class);
		int i = appointmentMapper.update(po);
		return i;
	}

	@Override
	public int cancel(Long id) {
		int i = appointmentMapper.cancel(id);
		return i;
	}

	@Override
	public int finish(Long id) {
		int i = appointmentMapper.finish(id);
		return i;
	}

	@Override
	public boolean isConflict() {
		// TODO Auto-generated method stub
		return false;
	}

}
