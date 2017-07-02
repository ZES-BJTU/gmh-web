package com.zes.squad.gmh.web.service;

import java.util.List;

import com.zes.squad.gmh.web.entity.dto.ProjectDto;
import com.zes.squad.gmh.web.entity.vo.ProjectVo;


public interface ProjectService {

	List<ProjectVo> getAll();
	List<ProjectVo> getBytype(Long projectType);
	int insert(ProjectDto dto);
	int update(ProjectDto dto);
	int delByIds(Long[] Id);
}
