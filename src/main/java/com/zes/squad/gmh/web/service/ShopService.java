package com.zes.squad.gmh.web.service;

import java.util.List;

import com.zes.squad.gmh.common.entity.PagedList;
import com.zes.squad.gmh.web.entity.dto.EmployeeDto;
import com.zes.squad.gmh.web.entity.dto.ShopDto;
import com.zes.squad.gmh.web.entity.vo.ShopVo;

public interface ShopService {

	int insert(ShopDto dto);
	
	int update(ShopDto dto);
	
	int delByIds(Long[] id);
	
	List<ShopVo> getAll();
	
	PagedList<ShopDto> listByPage(Integer pageNum, Integer pageSize);
	
	PagedList<ShopDto> searchListByPage(Integer pageNum, Integer pageSize,String searchString);
}
