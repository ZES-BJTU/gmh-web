package com.zes.squad.gmh.web.entity.vo;

import java.math.BigDecimal;

import lombok.Data;

@Data
public class StatisticsVo {

    private String     month;
    private Integer    timesCount;
    private BigDecimal amountCount;

}
