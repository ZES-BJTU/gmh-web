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
import com.zes.squad.gmh.common.entity.PagedLists;
import com.zes.squad.gmh.common.enums.ProjectTypeEnum;
import com.zes.squad.gmh.common.exception.ErrorCodeEnum;
import com.zes.squad.gmh.common.exception.ErrorMessage;
import com.zes.squad.gmh.common.exception.GmhException;
import com.zes.squad.gmh.common.util.EnumUtils;
import com.zes.squad.gmh.web.context.ThreadContext;
import com.zes.squad.gmh.web.entity.condition.ProjectQueryCondition;
import com.zes.squad.gmh.web.entity.dto.ProjectDto;
import com.zes.squad.gmh.web.entity.po.ProjectPo;
import com.zes.squad.gmh.web.entity.po.ProjectTypePo;
import com.zes.squad.gmh.web.entity.union.ProjectUnion;
import com.zes.squad.gmh.web.entity.vo.ProjectVo;
import com.zes.squad.gmh.web.mapper.ProjectMapper;
import com.zes.squad.gmh.web.mapper.ProjectTypeMapper;
import com.zes.squad.gmh.web.mapper.ProjectUnionMapper;
import com.zes.squad.gmh.web.service.ProjectService;

@Service("projectService")
public class ProjectServiceImpl implements ProjectService {

    @Autowired
    private ProjectMapper      projectMapper;
    @Autowired
    private ProjectTypeMapper  projectTypeMapper;
    @Autowired
    private ProjectUnionMapper projectUnionMapper;

    @Override
    public List<ProjectVo> listProjects() {
        ProjectQueryCondition condition = new ProjectQueryCondition();
        condition.setStoreId(ThreadContext.getStaffStoreId());
        List<ProjectUnion> unions = projectUnionMapper.listProjectUnionsByCondition(condition);
        if (CollectionUtils.isEmpty(unions)) {
            return Lists.newArrayList();
        }
        return buildProjectVosByUnions(unions);
    }

    private List<ProjectVo> buildProjectVosByUnions(List<ProjectUnion> unions) {
        List<ProjectVo> vos = Lists.newArrayList();
        for (ProjectUnion union : unions) {
            ProjectVo vo = CommonConverter.map(union.getProjectPo(), ProjectVo.class);
            vo.setTopType(union.getProjectTypePo().getTopType());
            vo.setTopTypeName(EnumUtils.getDescByKey(ProjectTypeEnum.class, union.getProjectTypePo().getTopType()));
            vo.setTypeName(union.getProjectTypePo().getTypeName());
            vo.setProjectName(union.getProjectPo().getName());
            vos.add(vo);
        }
        return vos;
    }

    @Override
    public PagedList<ProjectDto> search(Integer pageNum, Integer pageSize, Integer topType, Long projectType,
                                        String searchString) {
        ProjectQueryCondition condition = new ProjectQueryCondition();
        condition.setPageNum(pageNum);
        condition.setPageSize(pageSize);
        condition.setStoreId(ThreadContext.getStaffStoreId());
        condition.setTopType(topType == 0 ? null : topType);
        condition.setSearchString(searchString);
        condition.setProjectTypeId(projectType == 0 ? null : projectType);
        PageHelper.startPage(condition.getPageNum(), condition.getPageSize());
        List<ProjectUnion> unions = projectUnionMapper.listProjectUnionsByCondition(condition);
        if (CollectionUtils.isEmpty(unions)) {
            return PagedLists.newPagedList(pageNum, pageSize);
        }
        PageInfo<ProjectUnion> info = new PageInfo<>(unions);
        List<ProjectDto> dtos = buildProjectDtosByUnions(unions);
        PagedList<ProjectDto> pagedDtos = PagedLists.newPagedList(info.getPageNum(), info.getPageSize(),
                info.getTotal(), dtos);
        return pagedDtos;
    }

    private List<ProjectDto> buildProjectDtosByUnions(List<ProjectUnion> unions) {
        List<ProjectDto> dtos = Lists.newArrayList();
        for (ProjectUnion union : unions) {
            ProjectDto dto = CommonConverter.map(union.getProjectPo(), ProjectDto.class);
            dto.setTopType(union.getProjectTypePo().getTopType());
            dto.setTypeName(union.getProjectTypePo().getTypeName());
            dto.setProjectTypeName(union.getProjectTypePo().getTypeName());
            dto.setProjectName(union.getProjectPo().getName());
            dtos.add(dto);
        }
        return dtos;
    }

    @Override
    public List<ProjectVo> listByType(Long typeId) {
        ProjectQueryCondition condition = new ProjectQueryCondition();
        condition.setStoreId(ThreadContext.getStaffStoreId());
        condition.setProjectTypeId(typeId);
        List<ProjectUnion> unions = projectUnionMapper.listProjectUnionsByCondition(condition);
        if (CollectionUtils.isEmpty(unions)) {
            return Lists.newArrayList();
        }
        List<ProjectVo> vos = buildProjectVosByUnions(unions);
        return vos;
    }

    @Override
    public int insert(ProjectDto dto) {
        Long projectTypeId = dto.getProjectTypeId();
        ProjectTypePo typePo = projectTypeMapper.selectById(projectTypeId);
        if (typePo == null) {
            throw new GmhException(ErrorCodeEnum.BUSINESS_EXCEPTION_ENTITY_NOT_FOUND, ErrorMessage.projectTypeNotFound);
        }
        ProjectPo po = CommonConverter.map(dto, ProjectPo.class);
        po.setName(dto.getProjectName());
        return projectMapper.insert(po);
    }

    @Override
    public int update(ProjectDto dto) {
        Long projectTypeId = dto.getProjectTypeId();
        ProjectTypePo typePo = projectTypeMapper.selectById(projectTypeId);
        if (typePo == null) {
            throw new GmhException(ErrorCodeEnum.BUSINESS_EXCEPTION_ENTITY_NOT_FOUND, "未找到美容项分类信息");
        }
        ProjectPo po = CommonConverter.map(dto, ProjectPo.class);
        po.setName(dto.getProjectName());
        return projectMapper.updateSelective(po);
    }

    @Override
    public int deleteByIds(Long[] id) {
        return projectMapper.batchDelete(id);
    }

    @Override
    public ProjectVo queryById(Long id) {
        ProjectPo po = projectMapper.selectById(id);
        ProjectVo vo = CommonConverter.map(po, ProjectVo.class);
        vo.setProjectName(po.getName());
        vo.setTypeId(po.getProjectTypeId());
        return vo;
    }

}
