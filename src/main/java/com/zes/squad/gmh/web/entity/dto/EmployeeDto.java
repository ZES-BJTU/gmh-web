package com.zes.squad.gmh.web.entity.dto;

import java.util.Date;

import lombok.Data;
@Data
public class EmployeeDto {
	private Long id;
	private String emName;
	private int sex;
	private String phone;
	private Long shopId;
	private Date entryDate;
	private Date quitDate;
	private int isWork;
}
