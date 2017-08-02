package com.zes.squad.gmh.web.service;

import java.util.List;

import com.zes.squad.gmh.common.entity.PagedList;
import com.zes.squad.gmh.web.entity.dto.MemberDto;
import com.zes.squad.gmh.web.entity.vo.MemberVo;

public interface MemberService {

    List<MemberVo> listMembers();

    PagedList<MemberVo> listByPage(Integer pageNum, Integer pageSize);

    int insert(MemberDto dto);

    int update(MemberDto dto);

    int deleteByIds(Long[] Id);

    MemberVo queryByPhone(String phone);

}
