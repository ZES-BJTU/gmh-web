package com.zes.squad.gmh.web.entity.dto;

import java.math.BigDecimal;

import lombok.Data;

@Data
public class ProjectDto {
    private Long       id;
    private Integer    topType;
    private String     typeName;
    private Long       projectTypeId;
    private String     projectTypeName;
    private String     projectName;
    private BigDecimal retailPrice;
}
