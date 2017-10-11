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

}
