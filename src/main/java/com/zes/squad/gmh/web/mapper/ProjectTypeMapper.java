package com.zes.squad.gmh.web.mapper;

import java.util.List;

import com.zes.squad.gmh.web.entity.po.ProjectTypePo;

public interface ProjectTypeMapper {

	List<ProjectTypePo> getAll();
	int insert(ProjectTypePo po);
	int update(ProjectTypePo po);
	int delById(Long id);
}
