package com.zes.squad.gmh.web.entity.po;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class ProjectTypePo extends Po{
	private static final long serialVersionUID = 1L;
	private int topType;
	private String typeName;
}
