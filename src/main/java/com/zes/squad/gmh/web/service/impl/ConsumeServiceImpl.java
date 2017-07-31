package com.zes.squad.gmh.web.service.impl;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.zes.squad.gmh.common.converter.CommonConverter;
import com.zes.squad.gmh.web.entity.dto.ConsumeRecordDto;
import com.zes.squad.gmh.web.entity.po.ConsumeRecordPo;
import com.zes.squad.gmh.web.mapper.ConsumeRecordMapper;
import com.zes.squad.gmh.web.service.ConsumeService;

@Service("consumeRecordService")
public class ConsumeServiceImpl implements ConsumeService {

    @Autowired
    private ConsumeRecordMapper consumeRecordmapper;

    @Override
    public void addConsumeRecord(ConsumeRecordDto dto) {
        ConsumeRecordPo po = CommonConverter.map(dto, ConsumeRecordPo.class);
        po.setConsumeTime(new Date());
        consumeRecordmapper.insert(po);
        // TODO 会员怎么处理
    }

}
