package com.zes.squad.gmh.web.entity.param;

import lombok.Data;

@Data
public class AppointmentParams {

    private Long    id;
    private Long    storeId;
    private Long    memberId;
    private String  phone;
    private String  name;
    private Integer sex;
    private String  projects;
    private Integer status;
    private Boolean line;
    private String  remark;

}
