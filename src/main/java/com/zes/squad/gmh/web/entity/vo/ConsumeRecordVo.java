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
    private String     memberDesc;
    private Long       memberId;
    private String     consumerName; //可以是会员也可以是非会员
    private BigDecimal charge;
    private String     chargeWay;
    private Date       consumeTime;

}
