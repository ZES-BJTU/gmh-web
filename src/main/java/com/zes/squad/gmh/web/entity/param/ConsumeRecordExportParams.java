package com.zes.squad.gmh.web.entity.param;

import java.util.Date;

import lombok.Data;

@Data
public class ConsumeRecordExportParams {

    private String token;
    private Date   startTime;
    private Date   endTime;

}
