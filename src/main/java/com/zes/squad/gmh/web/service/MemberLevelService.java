package com.zes.squad.gmh.web.service;

import java.util.List;

import com.zes.squad.gmh.common.entity.PagedList;
import com.zes.squad.gmh.web.entity.dto.MemberLevelDto;
import com.zes.squad.gmh.web.entity.vo.MemberLevelVo;

public interface MemberLevelService {

	List<MemberLevelVo> getAll();
	PagedList<MemberLevelDto> listByPage(Integer pageNum, Integer pageSize);
	int insert(MemberLevelDto dto);
	int update(MemberLevelDto dto);
	int delByIds(Long[] Id);
}
