package com.zes.squad.gmh.web.entity.po;

import java.util.Date;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class AppointmentPo extends Po{
	private static final long serialVersionUID = 1L;
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
