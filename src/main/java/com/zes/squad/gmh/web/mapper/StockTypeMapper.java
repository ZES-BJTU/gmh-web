package com.zes.squad.gmh.web.mapper;

import java.util.List;

import com.zes.squad.gmh.web.entity.po.StockTypePo;

public interface StockTypeMapper {

	List<StockTypePo> getAll(Long storeId);
	int insert(StockTypePo po);
	int update(StockTypePo po);
	int delById(Long id);
}
