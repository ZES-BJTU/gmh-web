package com.zes.squad.gmh.web.entity.condition;

import lombok.Data;

@Data
public class ConsumeRecordQueryCondition {

    private Integer pageNum;
    private Integer pageSize;
    private Long    storeId;
    private String  startTime;
    private String  endTime;

}
