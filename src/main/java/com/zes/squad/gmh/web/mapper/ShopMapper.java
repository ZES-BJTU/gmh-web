package com.zes.squad.gmh.web.mapper;

import java.util.List;

import com.zes.squad.gmh.web.entity.po.ShopPo;

public interface ShopMapper {

	int insert(ShopPo po);
	
	int update(ShopPo po);
	
	int delById(Long id);
	
	List<ShopPo> getAll();
	
	List<ShopPo> search(String searchString);
}
