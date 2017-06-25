package com.zes.squad.gmh.web.entity.vo;

import java.util.Date;

import lombok.Data;
@Data
public class EmployeeVo {
	private Long id;
	private String emName;
	private int sex;
	private String phone;
	private Long shopId;
	private Date entryDate;
	private Date quitDate;
	private int isWork;
}
