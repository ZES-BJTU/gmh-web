package com.zes.squad.gmh.web.entity.param;

import java.math.BigDecimal;

import lombok.Data;

@Data
public class ConsumeRecordParams {

    private Long       projectId;
    private Long       employeeId;
    private String     mobile;
    private Integer    sex;
    private String     consumerName;
    private BigDecimal charge;
    private BigDecimal discount;
    private Integer    chargeWay;
    private String     source;
    private String     remark;

}
