package com.zes.squad.gmh.web.mapper;

import org.apache.ibatis.annotations.Param;

import com.zes.squad.gmh.web.entity.po.StaffPo;

public interface StaffMapper {

    /**
     * 根据id查询用户
     * 
     * @param id
     * @return
     */
    StaffPo selectById(Long id);

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

    /**
     * 更新密码
     * 
     * @param id
     * @param password
     * @return
     */
    int updatePassword(@Param("id") Long id, @Param("password") String password);

    /**
     * 更新非空
     * 
     * @param po
     * @return
     */
    int updateSelective(StaffPo po);

    int deleteById(Long id);

    /**
     * 批量删除
     * 
     * @param ids
     * @return
     */
    int batchDeleteByIds(Long[] ids);
}
