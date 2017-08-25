package com.zes.squad.gmh.web.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.zes.squad.gmh.web.entity.count.po.ConsumeCountPo;
import com.zes.squad.gmh.web.entity.po.ConsumeRecordPo;

public interface ConsumeRecordMapper {

    /**
     * 插入消费记录
     * 
     * @param po
     * @return
     */
    int insert(ConsumeRecordPo po);

    /**
     * 统计消费记录
     * 
     * @param storeId
     * @param month
     * @return
     */
    List<ConsumeCountPo> sumCardCharge(@Param("storeId") Long storeId, @Param("month") Integer month);

    /**
     * 统计消费记录
     * 
     * @param storeId
     * @param month
     * @return
     */
    List<ConsumeCountPo> sumOtherCharge(@Param("storeId") Long storeId, @Param("month") Integer month);

}
