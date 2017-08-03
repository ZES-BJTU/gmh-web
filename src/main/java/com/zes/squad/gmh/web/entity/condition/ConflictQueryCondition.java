package com.zes.squad.gmh.web.entity.condition;

import java.util.Date;

import lombok.Data;

@Data
public class ConflictQueryCondition {

    private Long memberId;
    private Long employeeId;
    private Date beginTime;
    private Date endTime;

}
