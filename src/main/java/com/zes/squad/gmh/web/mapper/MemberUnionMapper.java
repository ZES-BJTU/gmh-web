package com.zes.squad.gmh.web.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.zes.squad.gmh.web.entity.union.MemberUnion;

public interface MemberUnionMapper {

    /**
     * 关联查询
     * 
     * @param storeId
     * @param phone
     * @return
     */
    List<MemberUnion> listMemberUnionsByCondition(@Param("storeId") Long storeId, @Param("phone") String phone);

}
