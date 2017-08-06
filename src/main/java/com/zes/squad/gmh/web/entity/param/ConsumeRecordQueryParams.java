package com.zes.squad.gmh.web.entity.param;

import java.util.Date;

import lombok.Data;

@Data
public class ConsumeRecordQueryParams {

    private Integer pageNum;
    private Integer pageSize;
    private Date    startTime;
    private Date    endTime;
    private String  searchString;

}
