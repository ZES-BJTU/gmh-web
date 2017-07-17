package com.zes.squad.gmh.web.entity.po;

import java.util.Date;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class MemberPo extends Po{
	private static final long serialVersionUID = 1L;
	Long storeId;
	Long levelId;
	String phone;
	String memberName;
	Integer sex;
	Date birthday;
	Date joinDate;
	Date validDate;
	float nailMoney;
	float beautyMoney;
	String remark;
}
