package com.zes.squad.gmh.web.service;

import java.util.List;

import com.zes.squad.gmh.web.entity.dto.MemberDto;
import com.zes.squad.gmh.web.entity.vo.MemberVo;

public interface MemberService {
	List<MemberVo> getAll();
	int insert(MemberDto dto);
	int update(MemberDto dto);
	int delByIds(Long[] Id);
	MemberVo getByPhone(String phone);
}
