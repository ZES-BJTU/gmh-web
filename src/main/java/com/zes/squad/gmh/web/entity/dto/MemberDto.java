package com.zes.squad.gmh.web.entity.dto;

import java.util.Date;

import lombok.Data;

@Data
public class MemberDto {
    Long    id;
    Long    storeId;
    Long    levelId;
    String  phone;
    String  memberName;
    Integer sex;
    Date    birthday;
    Date    joinDate;
    Date    validDate;
    float   nailMoney;
    float   beautyMoney;
    String  remark;
}
