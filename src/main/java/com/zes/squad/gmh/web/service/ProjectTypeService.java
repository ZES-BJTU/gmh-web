package com.zes.squad.gmh.web.service;

import java.util.List;

import com.zes.squad.gmh.web.entity.dto.ProjectTypeDto;
import com.zes.squad.gmh.web.entity.vo.ProjectTypeVo;

public interface ProjectTypeService {

	List<ProjectTypeVo> getAll();
	int insert(ProjectTypeDto dto);
	int update(ProjectTypeDto dto);
	int delByIds(Long[] Id);
}
