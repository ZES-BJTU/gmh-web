package com.zes.squad.gmh.web.mapper;

import com.zes.squad.gmh.web.entity.po.ConsumeRecordPo;

public interface ConsumeRecordMapper {

    /**
     * 插入消费记录
     * 
     * @param po
     * @return
     */
    int insert(ConsumeRecordPo po);

}
