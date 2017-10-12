package com.zes.squad.gmh.web.mapper;

import java.util.List;

import com.zes.squad.gmh.web.entity.po.ConsumeRecordProjectPo;

public interface ConsumeRecordProjectMapper {

    /**
     * 批量插入
     * 
     * @param pos
     * @return
     */
    int batchInsert(List<ConsumeRecordProjectPo> pos);

    /**
     * 根据消费记录id删除
     * 
     * @param consumeRecordId
     * @return
     */
    int deleteByConsumeRecordId(Long consumeRecordId);

}
