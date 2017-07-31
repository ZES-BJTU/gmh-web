package com.zes.squad.gmh.web.service.impl;

import java.util.List;
import java.util.Map;

import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.zes.squad.gmh.common.converter.CommonConverter;
import com.zes.squad.gmh.common.entity.PagedList;
import com.zes.squad.gmh.web.context.ThreadContext;
import com.zes.squad.gmh.web.entity.dto.ProjectTypeDto;
import com.zes.squad.gmh.web.entity.dto.StaffDto;
import com.zes.squad.gmh.web.entity.po.ProjectTypePo;
import com.zes.squad.gmh.web.entity.vo.ProjectTypeVo;
import com.zes.squad.gmh.web.mapper.ProjectTypeMapper;
import com.zes.squad.gmh.web.service.ProjectTypeService;

@Service("projectTypeService")
public class ProjectTypeServiceImpl implements ProjectTypeService {

    @Autowired
    private ProjectTypeMapper projectTypeMapper;

    public List<ProjectTypeVo> getAll() {
        StaffDto staffDto = ThreadContext.getCurrentStaff();
        List<ProjectTypePo> pos = projectTypeMapper.getAll(staffDto.getStoreId());
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
        return projectTypeMapper.update(po);
    }

    public int delByIds(Long[] id) {
        return projectTypeMapper.batchDelete(id);
    }

    @Override
    public List<ProjectTypeVo> listByTopType(int topType) {
        StaffDto staffDto = ThreadContext.getCurrentStaff();
        Map<String, Object> map = Maps.newHashMap();
        map.put("topType", topType);
        map.put("storeId", staffDto.getStoreId());
        List<ProjectTypePo> pos = projectTypeMapper.getByTopType(map);
        if (CollectionUtils.isEmpty(pos)) {
            return Lists.newArrayList();
        }
        List<ProjectTypeVo> vos = CommonConverter.mapList(pos, ProjectTypeVo.class);
        return vos;
    }

    @Override
    public PagedList<ProjectTypeDto> searchListByPage(Integer pageNum, Integer pageSize, Long topType,
                                                      String searchString) {
        PageHelper.startPage(pageNum, pageSize);
        StaffDto staffDto = ThreadContext.getCurrentStaff();
        List<ProjectTypePo> projectTypePos = Lists.newArrayList();
        Map<String, Object> map = Maps.newHashMap();
        map.put("storeId", staffDto.getStoreId());
        map.put("searchString", searchString);
        if (topType == null || topType == 0L) {
            projectTypePos = projectTypeMapper.search(map);
        } else {
            map.put("topType", topType);
            projectTypePos = projectTypeMapper.search(map);
        }
        if (CollectionUtils.isEmpty(projectTypePos)) {
            return PagedList.newMe(pageNum, pageSize, 0L, Lists.newArrayList());
        }
        PageInfo<ProjectTypePo> info = new PageInfo<>(projectTypePos);
        PagedList<ProjectTypeDto> pagedList = CommonConverter.mapPageList(
                PagedList.newMe(info.getPageNum(), info.getPageSize(), info.getTotal(), projectTypePos),
                ProjectTypeDto.class);
        return pagedList;
    }
}
