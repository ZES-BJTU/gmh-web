package com.zes.squad.gmh.web.mapper;

import java.util.List;

import com.zes.squad.gmh.web.entity.condition.ProjectQueryCondition;
import com.zes.squad.gmh.web.entity.union.ProjectUnion;

public interface ProjectUnionMapper {

    /**
     * 关联查询
     * 
     * @param condition
     * @return
     */
    List<ProjectUnion> listProjectUnionsByCondition(ProjectQueryCondition condition);

}
