package com.zes.squad.gmh.web.mapper;

import java.util.List;
import java.util.Map;

import com.zes.squad.gmh.web.entity.po.ProjectTypePo;

public interface ProjectTypeMapper {

	List<ProjectTypePo> getAll(Long storeId);
	List<ProjectTypePo> getByTopType(Map map);
	int insert(ProjectTypePo po);
	int update(ProjectTypePo po);
	int delById(Long id);
}
