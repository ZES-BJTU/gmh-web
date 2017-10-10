package com.zes.squad.gmh.web.entity.dto;

import java.util.Date;

import lombok.Data;

@Data
public class AppointmentProjectDto {
    
    private Long    projectId;
    private Long    employeeId;
    private Date    beginTime;
    private Date    endTime;

}
