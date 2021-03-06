package com.zes.squad.gmh.web.service;

import java.util.List;

import com.zes.squad.gmh.common.entity.PagedList;
import com.zes.squad.gmh.web.entity.dto.MemberLevelDto;
import com.zes.squad.gmh.web.entity.vo.MemberLevelVo;

public interface MemberLevelService {

    List<MemberLevelVo> listAllMemberLevels();

    PagedList<MemberLevelDto> search(Integer pageNum, Integer pageSize, String searchString);

    int insert(MemberLevelDto dto);

    int update(MemberLevelDto dto);

    int deleteByIds(Long[] Ids);
}
