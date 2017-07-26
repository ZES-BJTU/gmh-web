package com.zes.squad.gmh.web.mapper;

import java.util.List;
import java.util.Map;

import com.zes.squad.gmh.web.entity.po.ProjectTypePo;
import com.zes.squad.gmh.web.entity.po.StockTypePo;

public interface ProjectTypeMapper {

	List<ProjectTypePo> getAll(Long storeId);
	List<ProjectTypePo> getByTopType(Map map);
	int insert(ProjectTypePo po);
	int update(ProjectTypePo po);
	int delById(Long id);
	List<ProjectTypePo> search(Map map);
	List<ProjectTypePo> searchWithTop(Map map);
}
