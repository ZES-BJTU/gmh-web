package com.zes.squad.gmh.web.service.impl;

import java.util.List;

import org.joda.time.DateTime;
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
        DateTime dateTime = DateTime.now().minusMonths(DEFAULT_MONTH - 1);
        List<ConsumeCountPo> countPos = Lists.newArrayList();
        for (int i = 0; i < DEFAULT_MONTH; i++) {
            ConsumeCountPo countPo = new ConsumeCountPo();
            String month = dateTime.toString("yyyy年M月");
            countPo.setMonth(month);
            for (int j = 0; j < countCardPos.size(); j++) {
                if (month.equals(countCardPos.get(j).getMonth())) {
                    countPo.setCardAmountCount(countCardPos.get(j).getCardAmountCount());
                }
            }
            for (int j = 0; j < countOtherPos.size(); j++) {
                if (month.equals(countOtherPos.get(j).getMonth())) {
                    countPo.setOtherAmountCount(countOtherPos.get(j).getOtherAmountCount());
                }
            }
            countPos.add(countPo);
            dateTime = dateTime.plusMonths(1);
        }
        return CommonConverter.mapList(countPos, StatisticsVo.class);
    }

}
