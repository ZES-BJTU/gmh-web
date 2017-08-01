package com.zes.squad.gmh.web.entity.param;

import java.util.Date;

import lombok.Data;

@Data
public class EmployeeParams {

    private Long    id;
    private String  emName;
    private Byte    sex;
    private String  phone;
    private Date    entryDate;
}
