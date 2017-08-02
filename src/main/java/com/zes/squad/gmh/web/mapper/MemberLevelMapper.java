package com.zes.squad.gmh.web.mapper;

import java.util.List;

import com.zes.squad.gmh.web.entity.po.MemberLevelPo;

public interface MemberLevelMapper {

    List<MemberLevelPo> selectByStoreId(Long storeId);

    int insert(MemberLevelPo po);

    int updateSelective(MemberLevelPo po);

    @Deprecated
    int delById(Long id);

    /**
     * 批量删除
     * 
     * @param ids
     * @return
     */
    int batchDelete(Long[] ids);
}
