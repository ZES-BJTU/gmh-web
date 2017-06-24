package com.zes.squad.gmh.web.service;

import java.util.List;

import com.zes.squad.gmh.web.entity.dto.ShopDto;
import com.zes.squad.gmh.web.entity.po.ShopPo;

public interface ShopService {

	int insert(ShopDto dto);
	
	int update(ShopDto dto);
	
	int delByIds(Long[] id);
	
	List<ShopPo> getAll();
}
