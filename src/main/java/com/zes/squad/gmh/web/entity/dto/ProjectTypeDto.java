package com.zes.squad.gmh.web.entity.dto;

import lombok.Data;

@Data
public class ProjectTypeDto {
    private Long    id;
    private Long    storeId;
    private Integer topType;
    private String  typeName;
}
