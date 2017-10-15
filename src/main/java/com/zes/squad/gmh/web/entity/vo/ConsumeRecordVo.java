package com.zes.squad.gmh.web.entity.vo;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import lombok.Data;

@Data
public class ConsumeRecordVo {

    private Long                         id;
    private Long                         storeId;
    private String                       storeName;
    private String                       consumerDesc;
    private Long                         memberId;
    private String                       memberName;
    private Long                         memberLevelId;
    private String                       memberLevelName;
    private String                       mobile;
    private Integer                      age;
    private String                       sex;
    private String                       consumerName;
    private Integer                      chargeWayId;
    private String                       chargeWay;
    private BigDecimal                   charge;
    private String                       source;
    private Date                         consumeTime;
    private String                       remark;
    private List<ConsumeRecordProjectVo> consumeRecordProjectVos;

}
