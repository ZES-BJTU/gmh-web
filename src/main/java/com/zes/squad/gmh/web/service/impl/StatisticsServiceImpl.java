package com.zes.squad.gmh.web.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.google.common.collect.Lists;
import com.zes.squad.gmh.common.converter.CommonConverter;
import com.zes.squad.gmh.web.context.ThreadContext;
import com.zes.squad.gmh.web.entity.count.po.ConsumeCountPo;
import com.zes.squad.gmh.web.entity.vo.StatisticsVo;
import com.zes.squad.gmh.web.mapper.ConsumeRecordMapper;
import com.zes.squad.gmh.web.service.StatisticsService;

@Service("statisticsService")
public class StatisticsServiceImpl implements StatisticsService {

    private static final int    DEFAULT_MONTH = 6;

    @Autowired
    private ConsumeRecordMapper consumeRecordMapper;

    @Override
    public List<StatisticsVo> countConsumeRecord() {
        List<ConsumeCountPo> countCardPos = consumeRecordMapper.sumCardCharge(ThreadContext.getStaffStoreId(),
                DEFAULT_MONTH);
        List<ConsumeCountPo> countOtherPos = consumeRecordMapper.sumOtherCharge(ThreadContext.getStaffStoreId(),
                DEFAULT_MONTH);
        List<ConsumeCountPo> countPos = Lists.newArrayList();
        for (int i = 0; i < countCardPos.size(); i++) {
            ConsumeCountPo countPo = new ConsumeCountPo();
            countPo.setMonth(countCardPos.get(i).getMonth());
            countPo.setCardAmountCount(countCardPos.get(i).getCardAmountCount());
            countPo.setOtherAmountCount(countOtherPos.get(i).getOtherAmountCount());
        }
        return CommonConverter.mapList(countPos, StatisticsVo.class);
    }

}
