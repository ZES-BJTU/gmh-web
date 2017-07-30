package com.zes.squad.gmh.web.entity.vo;

import java.math.BigDecimal;
import java.util.Date;

import lombok.Data;

@Data
public class MemberVo {
    private Long       id;
    private Long       storeId;
    private Long       levelId;
    private String     levelName;
    private String     phone;
    private String     memberName;
    private String     sex;
    private Date       birthday;
    private Date       joinDate;
    private Date       validDate;
    private BigDecimal nailMoney;
    private BigDecimal beautyMoney;
    private String     remark;
}
