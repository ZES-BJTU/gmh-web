package com.zes.squad.gmh.web.entity.dto;

import java.math.BigDecimal;
import java.util.Date;

import lombok.Data;

@Data
public class ConsumeRecordDto {

    private Long       id;
    private Long       storeId;
    private String     storeName;
    private Long       projectId;
    private String     projectName;
    private Long       employeeId;
    private String     employeeName;
    private Boolean    member;
    private Long       memberId;
    private String     mobile;
    private Integer    age;
    private Integer    sex;
    private String     consumerName;
    private BigDecimal charge;
    private Integer    chargeWay;
    private String     source;
    private Date       consumeTime;
    private String     remark;

}
