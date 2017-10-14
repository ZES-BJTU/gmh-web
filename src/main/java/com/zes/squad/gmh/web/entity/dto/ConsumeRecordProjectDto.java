package com.zes.squad.gmh.web.entity.dto;

import java.math.BigDecimal;

import lombok.Data;

@Data
public class ConsumeRecordProjectDto {

    private Long       projectId;
    private String     projectName;
    private Long       employeeId;
    private String     employeeName;
    private BigDecimal charge;
    private Long       counselorId;
    private String     counselorName;
    private BigDecimal retailPrice;

}
