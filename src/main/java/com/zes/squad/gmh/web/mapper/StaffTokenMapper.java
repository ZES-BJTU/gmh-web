package com.zes.squad.gmh.web.mapper;

import java.util.Date;

import org.apache.ibatis.annotations.Param;

import com.zes.squad.gmh.web.entity.po.StaffTokenPo;

public interface StaffTokenMapper {

    /**
     * 根据token查询用户信息
     * 
     * @param token
     * @return
     */
    StaffTokenPo selectByToken(String token);

    /**
     * 插入或者更新token
     * 
     * @param staffId
     * @param token
     * @param loginTime
     * @return
     */
    int insertOrUpdateToken(@Param("staffId") Long staffId, @Param("token") String token,
                       @Param("loginTime") Date loginTime);

}
