package com.zes.squad.gmh.web.entity.dto;

import java.util.Date;
import java.util.List;

import lombok.Data;

@Data
public class EmployeeDto {
    private Long         id;
    private String       emName;
    private Integer      sex;
    private String       phone;
    private Long         shopId;
    private Date         entryDate;
    private Date         quitDate;
    private Boolean      isWork;
    private List<JobDto> jobDtos;
}
