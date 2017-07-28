package com.zes.squad.gmh.web.entity.vo;

import lombok.Data;

@Data
public class StaffVo {

    private Long   id;

    private String email;

    private Long   storeId;

    private String storeName;

    private String principalName;

    private String principalMobile;

    private String token;

    private Long   staffLevel;

}
