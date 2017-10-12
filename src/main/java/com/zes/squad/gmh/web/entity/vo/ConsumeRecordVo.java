package com.zes.squad.gmh.web.entity.vo;

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
    private String                       mobile;
    private Integer                      age;
    private String                       sex;
    private String                       consumerName;
    private String                       chargeWay;
    private String                       source;
    private Date                         consumeTime;
    private String                       remark;
    private List<ConsumeRecordProjectVo> consumeRecordProjectVos;

}
