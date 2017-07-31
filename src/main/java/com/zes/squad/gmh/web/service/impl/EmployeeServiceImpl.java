package com.zes.squad.gmh.web.service.impl;

import java.util.Date;
import java.util.List;

import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.google.common.collect.Lists;
import com.zes.squad.gmh.common.converter.CommonConverter;
import com.zes.squad.gmh.common.entity.PagedList;
import com.zes.squad.gmh.web.context.ThreadContext;
import com.zes.squad.gmh.web.entity.dto.EmployeeDto;
import com.zes.squad.gmh.web.entity.dto.JobDto;
import com.zes.squad.gmh.web.entity.dto.StaffDto;
import com.zes.squad.gmh.web.entity.po.EmployeeJobPo;
import com.zes.squad.gmh.web.entity.po.EmployeePo;
import com.zes.squad.gmh.web.entity.union.EmployeeJobUnion;
import com.zes.squad.gmh.web.mapper.EmployeeJobMapper;
import com.zes.squad.gmh.web.mapper.EmployeeJobUnionMapper;
import com.zes.squad.gmh.web.mapper.EmployeeMapper;
import com.zes.squad.gmh.web.service.EmployeeService;

@Service("employeeService")
public class EmployeeServiceImpl implements EmployeeService {

    @Autowired
    private EmployeeMapper         employeeMapper;
    @Autowired
    private EmployeeJobMapper      employeeJobMapper;
    @Autowired
    private EmployeeJobUnionMapper employeeJobUnionMapper;

    @Override
    public PagedList<EmployeeDto> listByPage(Integer pageNum, Integer pageSize) {
        StaffDto staff = ThreadContext.getCurrentStaff();
        PageHelper.startPage(pageNum, pageSize);
        List<EmployeeJobUnion> unions = employeeJobUnionMapper.listEmployeeJobUnionsByCondition(staff.getStoreId(),
                null);
        if (CollectionUtils.isEmpty(unions)) {
            return PagedList.newMe(pageNum, pageSize, 0L, Lists.newArrayList());
        }
        PageInfo<EmployeeJobUnion> info = new PageInfo<>(unions);
        List<EmployeeDto> dtos = Lists.newArrayList();
        for (EmployeeJobUnion union : unions) {
            EmployeeDto dto = CommonConverter.map(union.getEmployeePo(), EmployeeDto.class);
            dto.setEmName(union.getEmployeePo().getName());
            dto.setIsWork(union.getEmployeePo().getWork());
            List<JobDto> jobDtos = CommonConverter.mapList(union.getEmployeeJobPos(), JobDto.class);
            dto.setJobDtos(jobDtos);
            dtos.add(dto);
        }
        PagedList<EmployeeDto> pagedDtos = PagedList.newMe(info.getPageNum(), info.getPageSize(), info.getTotal(),
                dtos);
        return pagedDtos;
    }

    @Transactional(propagation = Propagation.REQUIRED)
    public EmployeeDto insert(EmployeeDto dto) {
        EmployeePo po = CommonConverter.map(dto, EmployeePo.class);
        Date entry = new Date();
        po.setEntryDate(entry);
        po.setName(dto.getEmName());
        po.setWork(true);
        employeeMapper.insert(po);
        List<EmployeeJobPo> pos = CommonConverter.mapList(dto.getJobDtos(), EmployeeJobPo.class);
        for (EmployeeJobPo jobPo : pos) {
            jobPo.setEmployeeId(po.getId());
        }
        employeeJobMapper.batchInsert(pos);
        EmployeeDto newDto = CommonConverter.map(po, EmployeeDto.class);
        newDto.setJobDtos(CommonConverter.mapList(pos, JobDto.class));
        return newDto;
    }

    public int leave(Long[] id) {
        return employeeMapper.batchUpdateWork(new Date(), false, id);
    }

    public int update(EmployeeDto dto) {
        EmployeePo po = CommonConverter.map(dto, EmployeePo.class);
        Date entry = new Date();
        po.setEntryDate(entry);
        po.setName(dto.getEmName());
        po.setWork(dto.getIsWork());
        int i = employeeMapper.update(po);
        employeeJobMapper.delteByEmployeeId(dto.getId());
        List<EmployeeJobPo> pos = CommonConverter.mapList(dto.getJobDtos(), EmployeeJobPo.class);
        for (EmployeeJobPo jobPo : pos) {
            jobPo.setEmployeeId(po.getId());
        }
        employeeJobMapper.batchInsert(pos);
        return i;
    }

    @Override
    public PagedList<EmployeeDto> searchListByPage(Integer pageNum, Integer pageSize, String searchString) {
        PageHelper.startPage(pageNum, pageSize);
        StaffDto staffDto = ThreadContext.getCurrentStaff();
        List<EmployeeJobUnion> unions = employeeJobUnionMapper.listEmployeeJobUnionsByCondition(staffDto.getStoreId(),
                searchString);
        if (CollectionUtils.isEmpty(unions)) {
            return PagedList.newMe(pageNum, pageSize, 0L, Lists.newArrayList());
        }
        PageInfo<EmployeeJobUnion> info = new PageInfo<>(unions);
        List<EmployeeDto> dtos = Lists.newArrayList();
        for (EmployeeJobUnion union : unions) {
            EmployeeDto dto = CommonConverter.map(union.getEmployeePo(), EmployeeDto.class);
            dto.setEmName(union.getEmployeePo().getName());
            dto.setIsWork(union.getEmployeePo().getWork());
            List<JobDto> jobDtos = CommonConverter.mapList(union.getEmployeeJobPos(), JobDto.class);
            dto.setJobDtos(jobDtos);
            dtos.add(dto);
        }
        PagedList<EmployeeDto> pagedDtos = PagedList.newMe(info.getPageNum(), info.getPageSize(), info.getTotal(),
                dtos);
        return pagedDtos;
    }
}
