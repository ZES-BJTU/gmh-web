package com.zes.squad.gmh.web.entity.condition;

import lombok.Data;

@Data
public class AppointmentUnionQueryCondition {

    private Long   storeId;
    private String phone;
    private String searchString;

}
