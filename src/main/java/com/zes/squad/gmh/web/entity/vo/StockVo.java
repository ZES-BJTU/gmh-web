package com.zes.squad.gmh.web.entity.vo;

import lombok.Data;

@Data
public class StockVo {
	private Long id;
	private Long typeId;
	private String stockName;
	private String unit;
	private Integer amount;

}
