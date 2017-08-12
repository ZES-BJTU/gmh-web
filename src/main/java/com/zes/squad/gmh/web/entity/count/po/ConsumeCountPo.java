package com.zes.squad.gmh.web.entity.count.po;

import java.io.Serializable;
import java.math.BigDecimal;

import lombok.Data;

@Data
public class ConsumeCountPo implements Serializable {

    private static final long serialVersionUID = 1L;

    private String            month;
    private Integer           timesCount;
    private BigDecimal        amountCount;

}
