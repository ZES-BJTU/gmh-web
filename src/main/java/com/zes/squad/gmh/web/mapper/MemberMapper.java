package com.zes.squad.gmh.web.mapper;

import java.util.List;

import com.zes.squad.gmh.web.entity.vo.MemberVo;

public interface MemberMapper {
	List<MemberVo> getAll(Long storeId);
}
