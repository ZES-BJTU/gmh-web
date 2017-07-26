package com.zes.squad.gmh.web.mapper;

import java.util.List;
import java.util.Map;

import com.zes.squad.gmh.web.entity.po.StockPo;
import com.zes.squad.gmh.web.entity.vo.StockVo;


public interface StockMapper {

	List<StockPo> getAll(Long storeId);
	List<StockPo> getByType(Long typeId);
	int insert(StockPo po);
	int update(StockPo po);
	int delById(Long id);
	List<StockVo> search(Map map);
	List<StockVo> searchWithoutType(Map map);
}
