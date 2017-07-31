package com.zes.squad.gmh.web.entity.param;

import java.math.BigDecimal;

import lombok.Data;

@Data
public class ConsumeRecordParams {

    private Long       projectId;
    private Long       employeeId;
    private String     mobile;
    private Long       consumerName;
    private BigDecimal charge;
    private Integer    chargeWay;

}
