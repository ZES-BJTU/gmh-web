package com.zes.squad.gmh.web.entity.vo;

import java.util.Date;

import lombok.Data;
@Data
public class MemberVo {
	Long id;
	Long storeId;
	Long levelId;
	String levelName;
	String phone;
	String memberName;
	int sex;
	Date birthday;
	Date joinDate;
	Date validDate;
	float nailMoney;
	float beautyMoney;
	String remark;
}
