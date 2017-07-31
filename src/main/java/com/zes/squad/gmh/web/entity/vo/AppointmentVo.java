package com.zes.squad.gmh.web.entity.vo;

import java.util.Date;

import lombok.Data;

@Data
public class AppointmentVo {
    private Long    id;
    private Long    storeId;
    private Long    memberId;
    private String  memberName;
    private String  phone;
    private Long    projectId;
    private String  projectName;
    private Long    employeeId;
    private String  employeeName;
    private Date    beginTime;
    private Date    endTime;
    private Integer status;
    private String  isLine;
    private String  remark;
}
