package com.zes.squad.gmh.web.entity.condition;

import java.util.Date;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class ConsumeRecordQueryCondition extends QueryCondition {

    private Long   storeId;
    private Date   startTime;
    private Date   endTime;
    private String searchString;

}
