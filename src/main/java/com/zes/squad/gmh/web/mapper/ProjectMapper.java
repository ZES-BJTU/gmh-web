package com.zes.squad.gmh.web.mapper;

import java.util.List;
import java.util.Map;

import com.zes.squad.gmh.web.entity.po.ProjectPo;
import com.zes.squad.gmh.web.entity.po.ProjectTypePo;

public interface ProjectMapper {
	List<ProjectPo> getAll(Long storeId);
	List<ProjectPo> getByType(Long typeId);
	int insert(ProjectPo po);
	int update(ProjectPo po);
	int delById(Long id);
	List<ProjectPo> search(Map map);
	List<ProjectPo> searchWtihTop(Map map);
	List<ProjectPo> searchWtihType(Map map);
	
}
