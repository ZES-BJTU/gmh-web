package com.zes.squad.gmh.web.entity.po;

import java.util.Date;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class AppointmentPo extends Po {
    private static final long serialVersionUID = 1L;
    private Long              storeId;
    private Long              memberId;
    private String            phone;
    private Long              projectId;
    private Long              employeeId;
    private Date              beginTime;
    private Date              endTime;
    private Integer           status;
    private int               isLine;
    private String            remark;
}
