package com.zes.squad.gmh.web.service;

import java.util.List;

import com.zes.squad.gmh.web.entity.dto.StockDto;
import com.zes.squad.gmh.web.entity.vo.StockVo;


public interface StockService {

	List<StockVo> getAll();
	List<StockVo> getBytype(Long stockType);
	int insert(StockDto dto);
	int update(StockDto dto);
	int delByIds(Long[] Id);
}
