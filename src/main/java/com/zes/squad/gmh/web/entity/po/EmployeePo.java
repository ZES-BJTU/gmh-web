package com.zes.squad.gmh.web.entity.po;

import java.util.Date;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class EmployeePo extends Po {

    private static final long serialVersionUID = 1L;
    private String            name;
    private Byte              sex;
    private String            phone;
    private Long              shopId;
    private Date              entryDate;
    private Date              quitDate;
    private Boolean           work;
}
