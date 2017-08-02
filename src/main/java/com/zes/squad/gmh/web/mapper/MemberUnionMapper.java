package com.zes.squad.gmh.web.mapper;

import java.util.List;

import com.zes.squad.gmh.web.entity.condition.MemberQueryCondition;
import com.zes.squad.gmh.web.entity.union.MemberUnion;

public interface MemberUnionMapper {

    /**
     * 关联查询
     * 
     * @param condition
     * @return
     */
    List<MemberUnion> listMemberUnionsByCondition(MemberQueryCondition condition);

}
