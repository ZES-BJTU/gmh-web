package com.zes.squad.gmh.web.entity.po;

import java.math.BigDecimal;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class ProjectPo extends Po {

    private static final long serialVersionUID = 1L;
    private Long              projectTypeId;
    private String            name;
    private BigDecimal        retailPrice;
}
