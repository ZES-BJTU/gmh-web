package com.zes.squad.gmh.web.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.zes.squad.gmh.common.converter.CommonConverter;
import com.zes.squad.gmh.web.context.ThreadContext;
import com.zes.squad.gmh.web.entity.dto.MemberDto;
import com.zes.squad.gmh.web.entity.dto.StaffDto;
import com.zes.squad.gmh.web.entity.po.MemberLevelPo;
import com.zes.squad.gmh.web.entity.po.MemberPo;
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
	
	@Override
	public int insert(MemberDto dto) {
		int i = 0;
		StaffDto staffDto = ThreadContext.getCurrentStaff();
		dto.setStoreId(staffDto.getStoreId());
		MemberPo po = CommonConverter.map(dto, MemberPo.class);
		i = memberMapper.insert(po);
		return i;
	}

	@Override
	public int update(MemberDto dto) {
		int i = 0;
		MemberPo po = CommonConverter.map(dto, MemberPo.class);
		i = memberMapper.update(po);
		return i;
	}

	@Override
	public int delByIds(Long[] id) {
		int i = 0;
		for(int j=0;j<id.length;j++){
			i = i + memberMapper.delById(id[j]);
		}
		return 0;
	}

	@Override
	public MemberVo getByPhone(String phone) {
		MemberVo vo = new MemberVo();
		vo = memberMapper.getByPhone(phone);
		return vo;
	}
}
