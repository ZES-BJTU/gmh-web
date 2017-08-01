package com.zes.squad.gmh.web.entity.vo;

import java.math.BigDecimal;

import lombok.Data;

@Data
public class ProjectVo {
    private Long       id;
    private Integer    topType;
    private String     topTypeName;
    private String     typeName;
    private Long       projectTypeId;
    private String     projectTypeName;
    private String     projectName;
    private BigDecimal retailPrice;
}
