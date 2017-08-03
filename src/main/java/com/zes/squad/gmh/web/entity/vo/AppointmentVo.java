package com.zes.squad.gmh.web.entity.vo;

import java.util.Date;

import lombok.Data;

@Data
public class AppointmentVo {
    private Long   id;
    private Long   storeId;
    private String storeName;
    private Long   memberId;
    private String memberName;
    private String phone;
    private String topTypeName;
    private String typeName;
    private Long   projectId;
    private String projectName;
    private Long   employeeId;
    private String employeeName;
    private Date   beginTime;
    private Date   endTime;
    private String status;
    private String line;
    private String remark;
}
