package com.zes.squad.gmh.web.entity.vo;

import java.math.BigDecimal;
import java.util.Date;

import lombok.Data;

@Data
public class ConsumeRecordVo {

    private Long       id;
    private Long       storeId;
    private String     storeName;
    private Long       projectId;
    private String     projectName;
    private Long       employeeId;
    private String     employeeName;
    private String     consumerDesc;
    private Long       memberId;
    private String     mobile;
    private Integer    age;
    private String     sex;
    private String     consumerName;
    private BigDecimal charge;
    private String     chargeWay;
    private Long       counselorId;
    private String     counselorName;
    private String     source;
    private Date       consumeTime;
    private String     remark;

}
