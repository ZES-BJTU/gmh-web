package com.zes.squad.gmh.web.entity.dto;

import java.math.BigDecimal;
import java.util.Date;

import lombok.Data;

@Data
public class MemberDto {
    private Long       id;
    private Long       storeId;
    private Long       memberLevelId;
    private String     phone;
    private String     name;
    private Byte       sex;
    private Date       birthday;
    private Date       joinDate;
    private Date       validDate;
    private BigDecimal nailMoney;
    private BigDecimal beautyMoney;
    private String     remark;
}
