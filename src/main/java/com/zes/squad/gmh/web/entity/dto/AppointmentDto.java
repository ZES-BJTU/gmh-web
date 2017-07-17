package com.zes.squad.gmh.web.entity.dto;

import java.util.Date;

import lombok.Data;
@Data
public class AppointmentDto {
	Long id;
	Long storeId;
	Long memberId;
	String phone;
	Long projectId;
	Long employeeId;
	Date beginTime;
	Date endTime;
	Integer status;
	int isLine;
	String remark;
}
