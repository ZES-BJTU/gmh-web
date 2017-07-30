package com.zes.squad.gmh.web.service;

import java.util.List;

import com.zes.squad.gmh.common.entity.PagedList;
import com.zes.squad.gmh.web.entity.dto.StockTypeDto;
import com.zes.squad.gmh.web.entity.vo.StockTypeVo;

public interface StockTypeService {

	List<StockTypeVo> getAll();
	int insert(StockTypeDto dto);
	int update(StockTypeDto dto);
	int delByIds(Long[] Id);
	PagedList<StockTypeDto> searchListByPage(Integer pageNum, Integer pageSize,String searchString);
}
