package com.zes.squad.gmh.web.entity.dto;

import java.util.Date;

import lombok.Data;

@Data
public class AppointmentDto {
    private Long    id;
    private Long    storeId;
    private Long    memberId;
    private String  phone;
    private String  name;
    private Integer sex;
    private Long    projectId;
    private Long    employeeId;
    private Date    beginTime;
    private Date    endTime;
    private Integer status;
    private Boolean line;
    private String  remark;
}
