package com.zes.squad.gmh.web.service;

import java.util.List;

import com.zes.squad.gmh.common.entity.PagedList;
import com.zes.squad.gmh.web.entity.dto.ProjectDto;
import com.zes.squad.gmh.web.entity.vo.ProjectVo;

public interface ProjectService {

    List<ProjectVo> listProjects();

    PagedList<ProjectDto> listByPage(Integer pageNum, Integer pageSize);

    List<ProjectVo> listByType(Long projectType);

    int insert(ProjectDto dto);

    int update(ProjectDto dto);

    int deleteByIds(Long[] Id);
}
