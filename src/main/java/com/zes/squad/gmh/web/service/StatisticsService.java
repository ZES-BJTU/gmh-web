package com.zes.squad.gmh.web.service;

import java.util.List;

import com.zes.squad.gmh.web.entity.vo.StatisticsVo;

public interface StatisticsService {

    /**
     * 统计消费记录
     * 
     * @return
     */
    List<StatisticsVo> countConsumeRecord();

}
