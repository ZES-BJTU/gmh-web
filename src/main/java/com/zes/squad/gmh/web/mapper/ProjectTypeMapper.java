package com.zes.squad.gmh.web.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.zes.squad.gmh.web.entity.po.ProjectTypePo;

public interface ProjectTypeMapper {

    ProjectTypePo selectById(Long id);

    List<ProjectTypePo> selectByStoreId(Long storeId);

    List<ProjectTypePo> selectByTopType(@Param("storeId") Long storeId, @Param("topType") Integer topType);

    int insert(ProjectTypePo po);

    int updateSelective(ProjectTypePo po);

    @Deprecated
    int delById(Long id);

    /**
     * 批量删除
     * 
     * @param ids
     * @return
     */
    int batchDelete(Long[] ids);

    List<ProjectTypePo> search(@Param("storeId") Long storeId, @Param("searchString") String searchString,
                               @Param("topType") Integer topType);

}
