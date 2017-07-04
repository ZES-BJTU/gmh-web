package com.zes.squad.gmh.web.mapper;

import java.util.List;

import com.zes.squad.gmh.web.entity.po.StockPo;


public interface StockMapper {

	List<StockPo> getAll(Long storeId);
	List<StockPo> getByType(Long typeId);
	int insert(StockPo po);
	int update(StockPo po);
	int delById(Long id);
}
