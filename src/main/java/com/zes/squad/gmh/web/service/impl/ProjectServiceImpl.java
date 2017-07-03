package com.zes.squad.gmh.web.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.zes.squad.gmh.common.converter.CommonConverter;
import com.zes.squad.gmh.web.context.ThreadContext;
import com.zes.squad.gmh.web.entity.dto.ProjectDto;
import com.zes.squad.gmh.web.entity.dto.StaffDto;
import com.zes.squad.gmh.web.entity.po.ProjectPo;
import com.zes.squad.gmh.web.entity.vo.ProjectVo;
import com.zes.squad.gmh.web.mapper.ProjectMapper;
import com.zes.squad.gmh.web.service.ProjectService;
@Service("projectService")
public class ProjectServiceImpl implements ProjectService{

	@Autowired
	private ProjectMapper projectMapper;
	@Override
	public List<ProjectVo> getAll() {
		List<ProjectPo> poList = new ArrayList<ProjectPo>();
		List<ProjectVo> voList = new ArrayList<ProjectVo>();
		StaffDto staffDto = ThreadContext.getCurrentStaff();
		poList = projectMapper.getAll(staffDto.getStoreId());
		for(int i=0;i<poList.size();i++){
			voList.add(CommonConverter.map(poList.get(i),ProjectVo.class));
		}
		return voList;
	}

	@Override
	public List<ProjectVo> getBytype(Long typeId) {
		List<ProjectPo> poList = new ArrayList<ProjectPo>();
		List<ProjectVo> voList = new ArrayList<ProjectVo>();
		poList = projectMapper.getByType(typeId);
		for(int i=0;i<poList.size();i++){
			voList.add(CommonConverter.map(poList.get(i),ProjectVo.class));
		}
		return voList;
	}

	@Override
	public int insert(ProjectDto dto) {
		int i = 0;
		ProjectPo po = CommonConverter.map(dto, ProjectPo.class);
		i = projectMapper.insert(po);
		return i;
	}

	@Override
	public int update(ProjectDto dto) {
		int i = 0;
		ProjectPo po = CommonConverter.map(dto, ProjectPo.class);
		i = projectMapper.update(po);
		return i;
	}

	@Override
	public int delByIds(Long[] id) {
		int i = 0;
		for(int j=0;j<id.length;j++){
			i = i + projectMapper.delById(id[j]);
		}
		return 0;
	}
	
}
