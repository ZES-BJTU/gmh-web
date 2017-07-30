package com.zes.squad.gmh.web.entity.vo;

import java.math.BigDecimal;

import lombok.Data;

@Data
public class ProjectVo {
    private Long       id;
    private Long       typeId;
    private String     projectName;
    private String     unit;
    private BigDecimal retailPrice;
    private BigDecimal charge;
}
