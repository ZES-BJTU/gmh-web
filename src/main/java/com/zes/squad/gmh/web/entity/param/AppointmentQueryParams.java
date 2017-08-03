package com.zes.squad.gmh.web.entity.param;

import lombok.Data;

@Data
public class AppointmentQueryParams {

    private Integer pageNum;
    private Integer pageSize;
    private String  searchString;

}
