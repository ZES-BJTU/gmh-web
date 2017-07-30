package com.zes.squad.gmh.web.mapper;

import java.util.List;
import java.util.Map;

import com.zes.squad.gmh.web.entity.po.ProjectPo;

public interface ProjectMapper {
    List<ProjectPo> getAll(Long storeId);

    List<ProjectPo> getByType(Long typeId);

    int insert(ProjectPo po);

    int update(ProjectPo po);

    int delById(Long id);

    /**
     * 批量删除
     * 
     * @param ids
     * @return
     */
    int batchDelete(Long[] ids);

    List<ProjectPo> search(Map<String, Object> map);

    List<ProjectPo> searchWtihTop(Map<String, Object> map);

    List<ProjectPo> searchWtihType(Map<String, Object> map);

}
