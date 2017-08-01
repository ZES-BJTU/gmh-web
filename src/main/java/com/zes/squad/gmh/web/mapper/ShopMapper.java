package com.zes.squad.gmh.web.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.zes.squad.gmh.web.entity.po.ShopPo;

public interface ShopMapper {

    int insert(ShopPo po);

    int updateSelective(ShopPo po);

    @Deprecated
    int delById(Long id);

    /**
     * 批量删除
     * 
     * @param ids
     * @return
     */
    int batchDelete(Long[] ids);

    ShopPo selectById(Long id);

    /**
     * 查询所有门店
     * 
     * @return
     */
    List<ShopPo> selectAll();

    List<ShopPo> search(@Param("searchString") String searchString);
}
