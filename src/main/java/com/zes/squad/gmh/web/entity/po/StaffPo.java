package com.zes.squad.gmh.web.entity.po;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class StaffPo extends Po {

    private static final long serialVersionUID = 1L;

    /**
     * 邮箱
     */
    private String            email;
    /**
     * 密码
     */
    private String            password;
    /**
     * 盐值
     */
    private String            salt;
    /**
     * 门店id
     */
    private Long              storeId;
    /**
     * 负责人姓名
     */
    private String            principalName;
    /**
     * 负责人手机号
     */
    private String            principalMobile;

}
