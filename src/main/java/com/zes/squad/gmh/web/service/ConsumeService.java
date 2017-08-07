package com.zes.squad.gmh.web.service;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;

import com.zes.squad.gmh.common.entity.PagedList;
import com.zes.squad.gmh.web.entity.condition.ConsumeRecordQueryCondition;
import com.zes.squad.gmh.web.entity.dto.ConsumeRecordDto;

public interface ConsumeService {

    /**
     * 新增消费记录
     * 
     * @param dto
     */
    void createConsumeRecord(ConsumeRecordDto dto);

    /**
     * 分页查询
     * 
     * @param condition
     * @return
     */
    PagedList<ConsumeRecordDto> listPagedConsumeRecords(ConsumeRecordQueryCondition condition);

    /**
     * 查询并导出
     * 
     * @param condition
     */
    HSSFWorkbook exportToExcel(ConsumeRecordQueryCondition condition);

}
