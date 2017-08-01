package com.zes.squad.gmh.web.entity.condition;

import lombok.Data;

@Data
public class StockQueryCondition {

    private Long   storeId;
    private Long   typeId;
    private String searchString;

}
