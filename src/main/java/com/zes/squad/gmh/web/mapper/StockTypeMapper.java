package com.zes.squad.gmh.web.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.zes.squad.gmh.web.entity.po.StockTypePo;

public interface StockTypeMapper {

    StockTypePo selectById(Long id);
    
    List<StockTypePo> selectByStoreId(Long storeId);

    int insert(StockTypePo po);

    int updateSelective(StockTypePo po);

    @Deprecated
    int delById(Long id);

    /**
     * 批量删除
     * 
     * @param ids
     * @return
     */
    int batchDelete(Long[] ids);

    List<StockTypePo> search(@Param("storeId") Long storeId, @Param("searchString") String searchString);
}
