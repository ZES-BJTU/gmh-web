package com.zes.squad.gmh.web.service.impl;

import java.util.List;

import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.google.common.collect.Lists;
import com.zes.squad.gmh.common.converter.CommonConverter;
import com.zes.squad.gmh.common.entity.PagedList;
import com.zes.squad.gmh.common.entity.PagedLists;
import com.zes.squad.gmh.common.enums.ProjectTypeEnum;
import com.zes.squad.gmh.common.util.EnumUtils;
import com.zes.squad.gmh.web.context.ThreadContext;
import com.zes.squad.gmh.web.entity.dto.ProjectTypeDto;
import com.zes.squad.gmh.web.entity.po.ProjectTypePo;
import com.zes.squad.gmh.web.entity.vo.ProjectTypeVo;
import com.zes.squad.gmh.web.mapper.ProjectTypeMapper;
import com.zes.squad.gmh.web.service.ProjectTypeService;

@Service("projectTypeService")
public class ProjectTypeServiceImpl implements ProjectTypeService {

    @Autowired
    private ProjectTypeMapper projectTypeMapper;

    public List<ProjectTypeVo> listProjectTypes() {
        List<ProjectTypePo> pos = projectTypeMapper.selectByStoreId(ThreadContext.getStaffStoreId());
        if (CollectionUtils.isEmpty(pos)) {
            return Lists.newArrayList();
        }
        List<ProjectTypeVo> vos = CommonConverter.mapList(pos, ProjectTypeVo.class);
        return vos;
    }

    public int insert(ProjectTypeDto dto) {
        ProjectTypePo po = CommonConverter.map(dto, ProjectTypePo.class);
        return projectTypeMapper.insert(po);
    }

    public int update(ProjectTypeDto dto) {
        ProjectTypePo po = CommonConverter.map(dto, ProjectTypePo.class);
        return projectTypeMapper.updateSelective(po);
    }

    public int deleteByIds(Long[] id) {
        return projectTypeMapper.batchDelete(id);
    }

    @Override
    public List<ProjectTypeVo> listByTopType(Integer topType) {
        List<ProjectTypePo> pos = projectTypeMapper.selectByTopType(ThreadContext.getStaffStoreId(), topType);
        if (CollectionUtils.isEmpty(pos)) {
            return Lists.newArrayList();
        }
        List<ProjectTypeVo> vos = CommonConverter.mapList(pos, ProjectTypeVo.class);
        return vos;
    }

    @Override
    public PagedList<ProjectTypeDto> searchPagedProjectTypes(Integer pageNum, Integer pageSize, Integer topType,
                                                             String searchString) {
        PageHelper.startPage(pageNum, pageSize);
        List<ProjectTypePo> pos = null;
        if (topType == null || topType == 0L) {
            pos = projectTypeMapper.search(ThreadContext.getStaffStoreId(), searchString, null);
        } else {
            pos = projectTypeMapper.search(ThreadContext.getStaffStoreId(), searchString, topType);
        }
        if (CollectionUtils.isEmpty(pos)) {
            return PagedLists.newPagedList(pageNum, pageSize);
        }
        PageInfo<ProjectTypePo> info = new PageInfo<>(pos);
        List<ProjectTypeDto> dtos = Lists.newArrayList();
        for (ProjectTypePo po : pos) {
            ProjectTypeDto dto = CommonConverter.map(po, ProjectTypeDto.class);
            dto.setTopTypeName(EnumUtils.getDescByKey(ProjectTypeEnum.class, po.getTopType()));
            dtos.add(dto);
        }
        PagedList<ProjectTypeDto> pagedDtos = PagedLists.newPagedList(info.getPageNum(), info.getPageSize(),
                info.getTotal(), dtos);
        return pagedDtos;
    }
}
