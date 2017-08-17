package com.zes.squad.gmh.web.service;

import java.util.List;

import com.zes.squad.gmh.common.entity.PagedList;
import com.zes.squad.gmh.web.entity.dto.ProjectTypeDto;
import com.zes.squad.gmh.web.entity.vo.ProjectTypeVo;

public interface ProjectTypeService {

    List<ProjectTypeVo> listProjectTypes();

    List<ProjectTypeVo> listByTopType(Integer topType);

    int insert(ProjectTypeDto dto);

    int update(ProjectTypeDto dto);

    int deleteByIds(Long[] Id);

    PagedList<ProjectTypeDto> searchPagedProjectTypes(Integer pageNum, Integer pageSize, Integer topType,
                                                      String searchString);
}
