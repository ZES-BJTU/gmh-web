package com.zes.squad.gmh.web.mapper;

import java.util.List;

import com.zes.squad.gmh.web.entity.po.ProjectPo;

public interface ProjectMapper {
	List<ProjectPo> getAll();
	List<ProjectPo> getBytype(Long projectType);
	int insert(ProjectPo po);
	int update(ProjectPo po);
	int delById(Long id);
}
