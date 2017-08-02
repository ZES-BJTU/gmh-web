package com.zes.squad.gmh.web.mapper;

import java.math.BigDecimal;

import org.apache.ibatis.annotations.Param;

import com.zes.squad.gmh.web.entity.condition.MemberQueryCondition;
import com.zes.squad.gmh.web.entity.po.MemberPo;

public interface MemberMapper {

    MemberPo selectById(Long id);

    MemberPo selectByCondition(MemberQueryCondition condition);

    int insert(MemberPo po);

    int updateSelective(MemberPo po);

    /**
     * 更新美甲美睫储值
     * 
     * @param id
     * @param nailMoney
     * @return
     */
    int updateNailMoney(@Param("id") Long id, @Param("nailMoney") BigDecimal nailMoney);

    /**
     * 更新美容储值
     * 
     * @param id
     * @param beautyMoney
     * @return
     */
    int updateBeautyMoney(@Param("id") Long id, @Param("beautyMoney") BigDecimal beautyMoney);

    @Deprecated
    int delById(Long id);

    int batchDelete(Long[] ids);

}
