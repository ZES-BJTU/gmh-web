package com.zes.squad.gmh.web.mapper;

import java.util.List;

import com.zes.squad.gmh.web.entity.po.MemberLevelPo;

public interface MemberLevelMapper {

    List<MemberLevelPo> getAll(Long storeId);

    int insert(MemberLevelPo po);

    int update(MemberLevelPo po);

    int delById(Long id);

    /**
     * 批量删除
     * 
     * @param ids
     * @return
     */
    int batchDelete(List<Long> ids);
}
