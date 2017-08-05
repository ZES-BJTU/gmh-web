package com.zes.squad.gmh.web.entity.vo;

import java.math.BigDecimal;
import java.util.Date;

import lombok.Data;

@Data
public class MemberVo {
    private Long       id;
    private Long       storeId;
    private Long       memberLevelId;
    private String     memberLevelName;
    private String     phone;
    private String     name;
    private String     sex;
    private Integer    age;
    private Date       birthday;
    private Date       joinDate;
    private Date       validDate;
    private BigDecimal nailMoney;
    private BigDecimal beautyMoney;
    private String     remark;
}
