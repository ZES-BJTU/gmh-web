package com.zes.squad.gmh.web.entity.vo;

import lombok.Data;

@Data
public class StockVo {
    private Long    id;
    private Long    storeId;
    private Long    typeId;
    private String  stockNumber;
    private String  typeName;
    private String  stockName;
    private String  unit;
    private Integer amount;

}
