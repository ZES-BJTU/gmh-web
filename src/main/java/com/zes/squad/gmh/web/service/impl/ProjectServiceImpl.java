package com.zes.squad.gmh.web.service.impl;

import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.google.common.collect.Lists;
import com.zes.squad.gmh.common.converter.CommonConverter;
import com.zes.squad.gmh.common.entity.PagedList;
import com.zes.squad.gmh.common.exception.ErrorCodeEnum;
import com.zes.squad.gmh.common.exception.GmhException;
import com.zes.squad.gmh.web.context.ThreadContext;
import com.zes.squad.gmh.web.entity.dto.ProjectDto;
import com.zes.squad.gmh.web.entity.dto.StaffDto;
import com.zes.squad.gmh.web.entity.po.ProjectPo;
import com.zes.squad.gmh.web.entity.po.ProjectTypePo;
import com.zes.squad.gmh.web.entity.vo.ProjectVo;
import com.zes.squad.gmh.web.mapper.ProjectMapper;
import com.zes.squad.gmh.web.mapper.ProjectTypeMapper;
import com.zes.squad.gmh.web.service.ProjectService;

@Service("projectService")
public class ProjectServiceImpl implements ProjectService {

    @Autowired
    private ProjectMapper     projectMapper;
    @Autowired
    private ProjectTypeMapper projectTypeMapper;

    @Override
    public List<ProjectVo> getAll() {
        StaffDto staffDto = ThreadContext.getCurrentStaff();
        List<ProjectPo> pos = projectMapper.getAll(staffDto.getStoreId());
        if (CollectionUtils.isEmpty(pos)) {
            return Lists.newArrayList();
        }
        List<ProjectVo> vos = Lists.newArrayList();
        for (ProjectPo po : pos) {
            ProjectVo vo = CommonConverter.map(po, ProjectVo.class);
            vo.setTypeId(po.getProjectTypeId());
            vo.setProjectName(po.getName());
        }
        return vos;
    }

    @Override
    public PagedList<ProjectDto> listByPage(Integer pageNum, Integer pageSize) {
        StaffDto staff = ThreadContext.getCurrentStaff();
        PageHelper.startPage(pageNum, pageSize);
        List<ProjectPo> projectPos = projectMapper.getAll(staff.getStoreId());
        if (CollectionUtils.isEmpty(projectPos)) {
            return PagedList.newMe(pageNum, pageSize, 0L, Lists.newArrayList());
        }
        PageInfo<ProjectPo> info = new PageInfo<>(projectPos);
        List<ProjectDto> dtos = Lists.newArrayList();
        for (ProjectPo po : projectPos) {
            ProjectDto dto = CommonConverter.map(po, ProjectDto.class);
            dto.setTypeId(po.getProjectTypeId());
            dto.setProjectName(po.getName());
        }
        PagedList<ProjectDto> pagedDtos = PagedList.newMe(info.getPageNum(), info.getPageSize(), info.getTotal(), dtos);
        return pagedDtos;
    }

    @Override
    public List<ProjectVo> getByType(Long typeId) {
        List<ProjectPo> pos = projectMapper.getByType(typeId);
        List<ProjectVo> vos = Lists.newArrayList();
        for (ProjectPo po : pos) {
            ProjectVo vo = CommonConverter.map(po, ProjectVo.class);
            vo.setTypeId(po.getProjectTypeId());
            vo.setProjectName(po.getName());
            vos.add(vo);
        }
        return vos;
    }

    @Override
    public int insert(ProjectDto dto) {
        Long projectTypeId = dto.getTypeId();
        ProjectTypePo typePo = projectTypeMapper.selectById(projectTypeId);
        if (typePo == null) {
            throw new GmhException(ErrorCodeEnum.BUSINESS_EXCEPTION_ENTITY_NOT_FOUND, "未找到美容项分类信息");
        }
        ProjectPo po = CommonConverter.map(dto, ProjectPo.class);
        po.setProjectTypeId(dto.getTypeId());
        po.setName(dto.getProjectName());
        return projectMapper.insert(po);
    }

    @Override
    public int update(ProjectDto dto) {
        Long projectTypeId = dto.getTypeId();
        ProjectTypePo typePo = projectTypeMapper.selectById(projectTypeId);
        if (typePo == null) {
            throw new GmhException(ErrorCodeEnum.BUSINESS_EXCEPTION_ENTITY_NOT_FOUND, "未找到美容项分类信息");
        }
        ProjectPo po = CommonConverter.map(dto, ProjectPo.class);
        po.setProjectTypeId(dto.getTypeId());
        po.setName(dto.getProjectName());
        return projectMapper.update(po);
    }

    @Override
    public int delByIds(Long[] id) {
        return projectMapper.batchDelete(id);
    }

}
