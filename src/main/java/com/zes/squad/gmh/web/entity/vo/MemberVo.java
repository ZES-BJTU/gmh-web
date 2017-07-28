package com.zes.squad.gmh.web.entity.vo;

import java.util.Date;

import lombok.Data;

@Data
public class MemberVo {
    private Long    id;
    private Long    storeId;
    private Long    levelId;
    private String  levelName;
    private String  phone;
    private String  memberName;
    private Integer sex;
    private Date    birthday;
    private Date    joinDate;
    private Date    validDate;
    private float   nailMoney;
    private float   beautyMoney;
    private String  remark;
}
