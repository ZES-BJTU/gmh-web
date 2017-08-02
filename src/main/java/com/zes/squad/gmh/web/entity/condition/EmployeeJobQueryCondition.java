package com.zes.squad.gmh.web.entity.condition;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class EmployeeJobQueryCondition extends QueryCondition {

    private Long    storeId;
    private Boolean work;
    private Integer jobId;
    private String  searchString;

}
