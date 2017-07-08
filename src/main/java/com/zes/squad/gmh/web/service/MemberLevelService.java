package com.zes.squad.gmh.web.service;

import java.util.List;

import com.zes.squad.gmh.web.entity.dto.MemberLevelDto;
import com.zes.squad.gmh.web.entity.vo.MemberLevelVo;

public interface MemberLevelService {

	List<MemberLevelVo> getAll();
	int insert(MemberLevelDto dto);
	int update(MemberLevelDto dto);
	int delByIds(Long[] Id);
}
