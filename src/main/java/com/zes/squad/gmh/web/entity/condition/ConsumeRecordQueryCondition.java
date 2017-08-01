package com.zes.squad.gmh.web.entity.condition;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class ConsumeRecordQueryCondition extends QueryCondition {

    private Long   storeId;
    private String startTime;
    private String endTime;

}
