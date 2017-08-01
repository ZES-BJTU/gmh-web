package com.zes.squad.gmh.web.mapper;

import java.util.List;

import com.zes.squad.gmh.web.entity.condition.StockQueryCondition;
import com.zes.squad.gmh.web.entity.union.StockUnion;

public interface StockUnionMapper {

    /**
     * 条件联合查询
     * 
     * @param condition
     * @return
     */
    List<StockUnion> listStockUnionsByCondition(StockQueryCondition condition);

}
