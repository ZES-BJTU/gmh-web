package com.zes.squad.gmh.web.entity.po;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class EmployeeJobPo extends Po {
    private static final long serialVersionUID = 1L;
    private Long              emId;
    private Long              jobId;
}
