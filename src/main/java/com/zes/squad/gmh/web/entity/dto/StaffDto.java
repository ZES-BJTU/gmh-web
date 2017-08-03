package com.zes.squad.gmh.web.entity.dto;

import java.io.Serializable;

import lombok.Data;

@Data
public class StaffDto implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long              id;
    private String            email;
    private String            password;
    private String            salt;
    private Long              storeId;
    private String            storeName;
    private String            name;
    private String            mobile;
    private Long              staffLevel;
    private String            principalName;
    private String            principalMobile;
    private String            token;

}
