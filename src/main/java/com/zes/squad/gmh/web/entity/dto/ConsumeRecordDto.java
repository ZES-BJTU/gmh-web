package com.zes.squad.gmh.web.entity.dto;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import lombok.Data;

@Data
public class ConsumeRecordDto {

    private Long                          id;
    private Long                          storeId;
    private String                        storeName;
    private List<ConsumeRecordProjectDto> consumeRecordProjectDtos;
    private Boolean                       member;
    private Long                          memberId;
    private String                        memberName;
    private Long                          memberLevelId;
    private String                        memberLevelName;
    private String                        mobile;
    private Integer                       age;
    private Integer                       sex;
    private String                        consumerName;
    private BigDecimal                    charge;
    private Integer                       chargeWay;
    private String                        source;
    private Date                          consumeTime;
    private String                        remark;

}
