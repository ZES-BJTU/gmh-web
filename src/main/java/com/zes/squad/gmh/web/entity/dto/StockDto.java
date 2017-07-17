package com.zes.squad.gmh.web.entity.dto;

import lombok.Data;

@Data
public class StockDto {
	private Long id;
	private Long typeId;
	private String stockName;
	private String unit;
	private Integer amount;
}
