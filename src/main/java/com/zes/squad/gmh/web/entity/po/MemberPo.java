package com.zes.squad.gmh.web.entity.po;

import java.math.BigDecimal;
import java.util.Date;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class MemberPo extends Po {
    private static final long serialVersionUID = 1L;
    private Long              storeId;
    private Long              memberLevelId;
    private String            phone;
    private String            name;
    private Byte              sex;
    private Integer           age;
    private Date              birthday;
    private Date              joinDate;
    private Date              validDate;
    private BigDecimal        nailMoney;
    private BigDecimal        beautyMoney;
    private String            remark;
}
