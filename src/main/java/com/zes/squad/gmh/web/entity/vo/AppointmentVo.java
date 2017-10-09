package com.zes.squad.gmh.web.entity.vo;

import java.math.BigDecimal;
import java.util.Date;

import lombok.Data;

@Data
public class AppointmentVo {
    private Long         id;
    private Long         storeId;
    private String       storeName;
    private Long         memberId;
    private String       memberName;
    private String       phone;
    private String       name;
    private String       sex;
    private Integer[]    topTypes;
    private String[]     topTypeNames;
    private Long[]       typeIds;
    private String[]     typeNames;
    private Long[]       projectIds;
    private String[]     projectNames;
    private BigDecimal[] projectCharges;
    private Long[]       employeeIds;
    private String[]     employeeNames;
    private Date[]       beginTimes;
    private Date[]       endTimes;
    private String       status;
    private String       line;
    private String       remark;
}
