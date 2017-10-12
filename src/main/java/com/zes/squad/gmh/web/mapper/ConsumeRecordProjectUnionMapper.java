package com.zes.squad.gmh.web.mapper;

import java.util.List;

import com.zes.squad.gmh.web.entity.union.ConsumeRecordProjectUnion;

public interface ConsumeRecordProjectUnionMapper {
    
    /**
     * 根据消费记录id查询
     * 
     * @param consumeRecordId
     * @return
     */
    List<ConsumeRecordProjectUnion> selectByConsumeRecordId(Long consumeRecordId);

}
