package com.zes.squad.gmh.web.service;

import com.zes.squad.gmh.web.entity.dto.ConsumeRecordDto;

public interface ConsumeService {

    /**
     * 新增消费记录
     * 
     * @param dto
     */
    void addConsumeRecord(ConsumeRecordDto dto);

}
