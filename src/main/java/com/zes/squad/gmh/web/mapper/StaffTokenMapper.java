package com.zes.squad.gmh.web.mapper;

import java.util.Date;
import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.zes.squad.gmh.web.entity.po.StaffTokenPo;

public interface StaffTokenMapper {

    /**
     * 根据用户id查询token信息
     * 
     * @param staffId
     * @return
     */
    StaffTokenPo selectByStaffId(Long staffId);

    /**
     * 查询token列表
     * 
     * @param staffIds
     * @return
     */
    List<String> selectByStaffIds(Long[] staffIds);

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

    /**
     * 删除token信息
     * 
     * @param token
     * @return
     */
    int deleteByToken(String token);

    /**
     * 根据staff id集合批量删除
     * 
     * @param ids
     * @return
     */
    int batchDeleteByStaffIds(@Param("ids") Long[] ids);

}
