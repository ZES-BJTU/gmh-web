package com.zes.squad.gmh.web.entity.po;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class StockTypePo extends Po{
	private static final long serialVersionUID = 1L;
	private Long storeId;
	private String typeName;
}
