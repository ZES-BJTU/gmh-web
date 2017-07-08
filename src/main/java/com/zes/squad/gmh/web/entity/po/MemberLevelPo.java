package com.zes.squad.gmh.web.entity.po;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class MemberLevelPo extends Po{
	private static final long serialVersionUID = 1L;
	Long storeId;
	String levelName;
}
