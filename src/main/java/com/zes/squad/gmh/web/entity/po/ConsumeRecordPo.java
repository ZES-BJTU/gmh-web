package com.zes.squad.gmh.web.entity.po;

import java.math.BigDecimal;
import java.util.Date;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class ConsumeRecordPo extends Po {

    private static final long serialVersionUID = 1L;

    private Long              storeId;
    private Long              projectId;
    private Long              employeeId;
    private Boolean           member;
    private Long              memberId;
    private String            mobile;
    private Integer           age;
    private Integer           sex;
    private String            consumerName;         //可以是会员也可以是非会员
    private BigDecimal        charge;
    private Integer           chargeWay;
    private Integer           counselor;
    private String            source;
    private Date              consumeTime;
    private String            remark;

}
