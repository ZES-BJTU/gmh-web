package com.zes.squad.gmh.web.entity.param;

import lombok.Data;

@Data
public class ConsumeRecordQueryParams {

    private Integer pageNum;
    private Integer pageSize;
    private String  startTime;
    private String  endTime;
    private String  searchString;

}
