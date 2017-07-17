package com.zes.squad.gmh.web.entity.param;

import java.util.Date;

import lombok.Data;

@Data
public class EmployeeParam {

	private Long id;
	private String emName;
	private Integer sex;
	private String phone;
	private Long shopId;
	private Date entryDate;
	private Date quitDate;
	private int isWork;
}
