package com.zes.squad.gmh.web.entity.vo;

import java.util.Date;
import java.util.List;

import lombok.Data;

@Data
public class EmployeeVo {
    private Long        id;
    private String      emName;
    private String      sex;
    private String      phone;
    private Long        shopId;
    private String      shopName;
    private Date        entryDate;
    private Date        quitDate;
    private String      isWork;
    private List<JobVo> jobVos;
}
