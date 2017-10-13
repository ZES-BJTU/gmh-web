package com.zes.squad.gmh.web.entity.vo;

import java.math.BigDecimal;

import lombok.Data;

@Data
public class ConsumeRecordProjectVo {

    private Long       projectId;
    private String     projectName;
    private Long       employeeId;
    private String     employeeName;
    private BigDecimal charge;
    private BigDecimal retailPrice;
    private Long       counselorId;
    private String     counselorName;

}
