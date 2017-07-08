package com.zes.squad.gmh.web.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.zes.squad.gmh.common.converter.CommonConverter;
import com.zes.squad.gmh.web.context.ThreadContext;
import com.zes.squad.gmh.web.entity.dto.StaffDto;
import com.zes.squad.gmh.web.entity.po.MemberLevelPo;
import com.zes.squad.gmh.web.entity.vo.MemberLevelVo;
import com.zes.squad.gmh.web.entity.vo.MemberVo;
import com.zes.squad.gmh.web.mapper.MemberMapper;
import com.zes.squad.gmh.web.service.MemberService;

@Service("memberService")
public class MemberServiceImpl implements MemberService{
	@Autowired
	private MemberMapper memberMapper;
	
	public List<MemberVo> getAll(){
		List<MemberVo> voList = new ArrayList<MemberVo>();
		StaffDto staffDto = ThreadContext.getCurrentStaff();
		voList = memberMapper.getAll(staffDto.getStoreId());
		return voList;
	}
}
