package com.zes.squad.gmh.web.entity.dto;

import java.math.BigDecimal;

import lombok.Data;

@Data
public class ProjectDto {
    private Long       id;
    private Long       typeId;
    private String     projectName;
    private String     unit;
    private BigDecimal retailPrice;
    private BigDecimal charge;
}
