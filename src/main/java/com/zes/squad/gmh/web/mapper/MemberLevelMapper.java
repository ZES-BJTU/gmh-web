package com.zes.squad.gmh.web.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.zes.squad.gmh.web.entity.po.MemberLevelPo;

public interface MemberLevelMapper {

    MemberLevelPo selectById(Long id);

    List<MemberLevelPo> selectByCondition(@Param("storeId") Long storeId, @Param("searchString") String searchString);

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
