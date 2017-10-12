package com.zes.squad.gmh.web.entity.dto;

import java.util.Date;
import java.util.List;

import lombok.Data;

@Data
public class ConsumeRecordDto {

    private Long                  id;
    private Long                  storeId;
    private String                storeName;
    List<ConsumeRecordProjectDto> consumeRecordProjectDtos;
    private Boolean               member;
    private Long                  memberId;
    private String                mobile;
    private Integer               age;
    private Integer               sex;
    private String                consumerName;
    private Integer               chargeWay;
    private String                source;
    private Date                  consumeTime;
    private String                remark;

}
