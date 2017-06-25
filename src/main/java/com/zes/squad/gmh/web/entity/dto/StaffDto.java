package com.zes.squad.gmh.web.entity.dto;

import lombok.Data;

@Data
public class StaffDto {

    private Long   id;
    private String email;
    private String password;
    private String salt;
    private Long   storeId;
    private String principalName;
    private String principalMobile;
    private String token;

}
