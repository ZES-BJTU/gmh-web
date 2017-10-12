package com.zes.squad.gmh.web.entity.param;

import java.math.BigDecimal;

import lombok.Data;

@Data
public class ConsumeRecordParams {

    private Long       id;
    private String     mobile;
    private Integer    age;
    private Integer    sex;
    private String     consumerName;
    private String     projects;
    private BigDecimal charge;
    private BigDecimal discount;
    private Integer    chargeWay;
    private Long       memberId;
    private Long       counselorId;
    private String     source;
    private String     remark;

}
