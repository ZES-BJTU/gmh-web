package com.zes.squad.gmh.web.entity.condition;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class MemberQueryCondition extends QueryCondition {

    private Long   storeId;
    private String phone;
    private String searchString;
    private Long   memberLevelId;

}
