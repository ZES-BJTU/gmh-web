package com.zes.squad.gmh.web.service;

import java.util.List;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;

import com.zes.squad.gmh.common.entity.PagedList;
import com.zes.squad.gmh.web.entity.condition.ConsumeRecordQueryCondition;
import com.zes.squad.gmh.web.entity.dto.ConsumeRecordDto;
import com.zes.squad.gmh.web.entity.vo.MemberVo;

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

    /**
     * 根据手机号查询会员卡信息
     * 
     * @param phone
     * @return
     */
    List<MemberVo> listMemberCardsByPhone(String phone);

    /**
     * 修改消费记录
     * 
     * @param dto
     */
    void modifyConsumeRecord(ConsumeRecordDto dto);

}
