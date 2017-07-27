package com.zes.squad.gmh.web.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.zes.squad.gmh.web.entity.po.StaffPo;
import com.zes.squad.gmh.web.entity.po.StockTypePo;
import com.zes.squad.gmh.web.entity.vo.StaffVo;

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

    List<StaffVo> search(String searchString);
    
    int update(StaffPo po);
}
