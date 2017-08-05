package com.zes.squad.gmh.web.entity.vo;

import java.math.BigDecimal;

import lombok.Data;

@Data
public class ProjectVo {
    private Long       id;
    private Integer    topType;
    private String     topTypeName;
    private Long       typeId;
    private String     typeName;
    private String     projectName;
    private BigDecimal retailPrice;
}
