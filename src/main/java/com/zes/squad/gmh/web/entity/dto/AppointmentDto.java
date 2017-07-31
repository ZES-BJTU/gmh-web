package com.zes.squad.gmh.web.entity.dto;

import java.util.Date;

import lombok.Data;

@Data
public class AppointmentDto {
    private Long    id;
    private Long    storeId;
    private Long    memberId;
    private String  phone;
    private Long    projectId;
    private Long    employeeId;
    private Date    beginTime;
    private Date    endTime;
    private Integer status;
    private Integer isLine;
    private String  remark;
}
