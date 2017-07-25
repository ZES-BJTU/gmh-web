package com.zes.squad.gmh.web.service;

import java.util.List;

import com.zes.squad.gmh.common.entity.PagedList;
import com.zes.squad.gmh.web.entity.dto.ProjectTypeDto;
import com.zes.squad.gmh.web.entity.dto.StockTypeDto;
import com.zes.squad.gmh.web.entity.vo.ProjectTypeVo;

public interface ProjectTypeService {

	List<ProjectTypeVo> getAll();
	List<ProjectTypeVo> getByTopType(int topType);
	int insert(ProjectTypeDto dto);
	int update(ProjectTypeDto dto);
	int delByIds(Long[] Id);
	PagedList<ProjectTypeDto> searchListByPage(Integer pageNum, Integer pageSize,String searchString);
}
