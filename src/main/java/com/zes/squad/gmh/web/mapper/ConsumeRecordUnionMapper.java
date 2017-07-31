package com.zes.squad.gmh.web.mapper;

import java.util.List;

import com.zes.squad.gmh.web.entity.condition.ConsumeRecordQueryCondition;
import com.zes.squad.gmh.web.entity.union.ConsumeRecordUnion;

public interface ConsumeRecordUnionMapper {

    /**
     * 条件查询
     * 
     * @param condition
     * @return
     */
    List<ConsumeRecordUnion> listConsumeRecordsByCondition(ConsumeRecordQueryCondition condition);

}
