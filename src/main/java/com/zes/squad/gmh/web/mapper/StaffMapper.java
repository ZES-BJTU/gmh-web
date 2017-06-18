package com.zes.squad.gmh.web.mapper;

import com.zes.squad.gmh.web.entity.po.StaffPo;

public interface StaffMapper {

    /**
     * 根据邮箱查询
     * 
     * @return
     */
    StaffPo selectByEmail(String email);

    /**
     * @param po
     * @return
     */
    int insert(StaffPo po);

}
