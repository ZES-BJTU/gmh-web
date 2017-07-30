package com.zes.squad.gmh.web.mapper;

import java.util.List;

import com.zes.squad.gmh.web.entity.po.MemberPo;
import com.zes.squad.gmh.web.entity.vo.MemberVo;

public interface MemberMapper {
    List<MemberVo> getAll(Long storeId);

    MemberVo getByPhone(String phone);

    int insert(MemberPo po);

    int update(MemberPo po);

    int delById(Long id);
    
    int batchDelete(Long[] ids);
}
