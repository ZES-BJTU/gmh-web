package com.zes.squad.gmh.web.mapper;

import java.util.List;
import java.util.Map;

import com.zes.squad.gmh.web.entity.po.ProjectTypePo;

public interface ProjectTypeMapper {
    
    ProjectTypePo selectById(Long id);

    List<ProjectTypePo> getAll(Long storeId);

    List<ProjectTypePo> getByTopType(Map<String, Object> map);

    int insert(ProjectTypePo po);

    int update(ProjectTypePo po);

    int delById(Long id);

    /**
     * 批量删除
     * 
     * @param ids
     * @return
     */
    int batchDelete(Long[] ids);

    List<ProjectTypePo> search(Map<String, Object> map);

}
