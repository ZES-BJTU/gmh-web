package com.zes.squad.gmh.web.entity.po;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class ShopPo extends Po {

    private static final long serialVersionUID = 1L;

    private String            name;

    private String            manager;

    private String            phone;

    private String            address;
}
