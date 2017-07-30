package com.zes.squad.gmh.web.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.zes.squad.gmh.web.entity.union.StockUnion;

public interface StockUnionMapper {

    /**
     * 条件联合查询
     * 
     * @param storeId
     * @param typeId
     * @param searchString
     * @return
     */
    List<StockUnion> listStockUnions(@Param("storeId") Long storeId, @Param("typeId") Long typeId,
                                     @Param("serachString") String searchString);

}
