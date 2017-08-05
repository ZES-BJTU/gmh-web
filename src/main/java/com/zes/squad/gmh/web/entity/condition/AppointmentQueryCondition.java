package com.zes.squad.gmh.web.entity.condition;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class AppointmentQueryCondition extends QueryCondition {

    private String        searchString;

}
