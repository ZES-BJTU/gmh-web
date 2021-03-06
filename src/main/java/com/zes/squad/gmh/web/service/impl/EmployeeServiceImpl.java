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
import com.zes.squad.gmh.common.entity.PagedLists;
import com.zes.squad.gmh.common.enums.JobEnum;
import com.zes.squad.gmh.common.util.EnumUtils;
import com.zes.squad.gmh.web.context.ThreadContext;
import com.zes.squad.gmh.web.entity.condition.EmployeeJobQueryCondition;
import com.zes.squad.gmh.web.entity.dto.EmployeeDto;
import com.zes.squad.gmh.web.entity.dto.JobDto;
import com.zes.squad.gmh.web.entity.po.EmployeeJobPo;
import com.zes.squad.gmh.web.entity.po.EmployeePo;
import com.zes.squad.gmh.web.entity.union.EmployeeJobUnion;
import com.zes.squad.gmh.web.entity.vo.EmployeeItemVo;
import com.zes.squad.gmh.web.mapper.EmployeeJobMapper;
import com.zes.squad.gmh.web.mapper.EmployeeJobUnionMapper;
import com.zes.squad.gmh.web.mapper.EmployeeMapper;
import com.zes.squad.gmh.web.mapper.ShopMapper;
import com.zes.squad.gmh.web.service.EmployeeService;

@Service("employeeService")
public class EmployeeServiceImpl implements EmployeeService {

    private static final Boolean   DEFAULT_IS_WORK = true;
    @Autowired
    private EmployeeMapper         employeeMapper;
    @Autowired
    private EmployeeJobMapper      employeeJobMapper;
    @Autowired
    private EmployeeJobUnionMapper employeeJobUnionMapper;
    @Autowired
    private ShopMapper             shopMapper;

    @Override
    public PagedList<EmployeeDto> listByPage(Integer pageNum, Integer pageSize) {
        PageHelper.startPage(pageNum, pageSize);
        EmployeeJobQueryCondition condition = new EmployeeJobQueryCondition();
        condition.setPageNum(pageNum);
        condition.setPageSize(pageSize);
        condition.setStoreId(ThreadContext.getStaffStoreId());
        condition.setWork(DEFAULT_IS_WORK);
        List<Long> ids = employeeMapper.selectIdsByCondition(condition);
        if (CollectionUtils.isEmpty(ids)) {
            return PagedLists.newPagedList(pageNum, pageSize);
        }
        PageInfo<Long> info = new PageInfo<>(ids);
        List<EmployeeJobUnion> unions = employeeJobUnionMapper.listEmployeeJobUnionsByCondition(ids);
        List<EmployeeDto> dtos = buildEmployeeDtosByUnions(unions);
        PagedList<EmployeeDto> pagedDtos = PagedLists.newPagedList(info.getPageNum(), info.getPageSize(),
                info.getTotal(), dtos);
        return pagedDtos;
    }

    @Transactional(propagation = Propagation.REQUIRED)
    public EmployeeDto insert(EmployeeDto dto) {
        EmployeePo po = CommonConverter.map(dto, EmployeePo.class);
        if (dto.getEntryDate() == null) {
            po.setEntryDate(new Date());
        }
        po.setName(dto.getEmName());
        po.setWork(true);
        employeeMapper.insert(po);
        List<EmployeeJobPo> pos = CommonConverter.mapList(dto.getJobDtos(), EmployeeJobPo.class);
        for (EmployeeJobPo jobPo : pos) {
            jobPo.setEmployeeId(po.getId());
        }
        employeeJobMapper.batchInsert(pos);
        EmployeeDto employeeDto = CommonConverter.map(po, EmployeeDto.class);
        employeeDto.setShopName(shopMapper.selectById(po.getShopId()).getName());
        employeeDto.setIsWork(po.getWork());
        employeeDto.setJobDtos(CommonConverter.mapList(pos, JobDto.class));
        return employeeDto;
    }

    public int leave(Long[] id) {
        return employeeMapper.batchUpdateWork(new Date(), false, id);
    }

    @Transactional(propagation = Propagation.REQUIRED)
    public int update(EmployeeDto dto) {
        EmployeePo po = CommonConverter.map(dto, EmployeePo.class);
        po.setName(dto.getEmName());
        po.setWork(dto.getIsWork());
        int i = employeeMapper.updateSelective(po);
        employeeJobMapper.delteByEmployeeId(dto.getId());
        List<EmployeeJobPo> pos = CommonConverter.mapList(dto.getJobDtos(), EmployeeJobPo.class);
        for (EmployeeJobPo jobPo : pos) {
            jobPo.setEmployeeId(po.getId());
        }
        employeeJobMapper.batchInsert(pos);
        return i;
    }

    @Override
    public PagedList<EmployeeDto> searchListByPage(Integer pageNum, Integer pageSize, String searchString,
                                                   Integer jobId) {
        if (jobId == 0) {
            jobId = null;
        }
        PageHelper.startPage(pageNum, pageSize);
        EmployeeJobQueryCondition condition = new EmployeeJobQueryCondition();
        condition.setPageNum(pageNum);
        condition.setPageSize(pageSize);
        condition.setStoreId(ThreadContext.getStaffStoreId());
        condition.setWork(DEFAULT_IS_WORK);
        condition.setSearchString(searchString);
        if (jobId != null) {
            condition.setJobTypes(Lists.newArrayList(jobId));
        }
        List<Long> ids = employeeJobUnionMapper.selectIdsByCondition(condition);
        if (CollectionUtils.isEmpty(ids)) {
            return PagedLists.newPagedList(pageNum, pageSize);
        }
        PageInfo<Long> info = new PageInfo<>(ids);
        List<EmployeeJobUnion> unions = employeeJobUnionMapper.listEmployeeJobUnionsByCondition(ids);
        List<EmployeeDto> dtos = buildEmployeeDtosByUnions(unions);
        PagedList<EmployeeDto> pagedDtos = PagedLists.newPagedList(info.getPageNum(), info.getPageSize(),
                info.getTotal(), dtos);
        return pagedDtos;
    }

    private List<EmployeeDto> buildEmployeeDtosByUnions(List<EmployeeJobUnion> unions) {
        List<EmployeeDto> dtos = Lists.newArrayList();
        for (EmployeeJobUnion union : unions) {
            EmployeeDto dto = CommonConverter.map(union.getEmployeePo(), EmployeeDto.class);
            dto.setEmName(union.getEmployeePo().getName());
            dto.setShopName(union.getShopPo().getName());
            dto.setIsWork(union.getEmployeePo().getWork());
            List<JobDto> jobDtos = Lists.newArrayList();
            for (EmployeeJobPo jobPo : union.getEmployeeJobPos()) {
                JobDto jobDto = CommonConverter.map(jobPo, JobDto.class);
                jobDto.setJobName(EnumUtils.getDescByKey(JobEnum.class, jobPo.getJobType()));
                jobDtos.add(jobDto);
            }
            dto.setJobDtos(jobDtos);
            dtos.add(dto);
        }
        return dtos;
    }

    @Override
    public List<EmployeeDto> listCounselors() {
        Long storeId = ThreadContext.getStaffStoreId();
        List<Integer> jobTypes = Lists.newArrayList(JobEnum.COUNSELOR.getKey(), JobEnum.MANAGER.getKey());
        EmployeeJobQueryCondition condition = new EmployeeJobQueryCondition();
        condition.setStoreId(storeId);
        condition.setWork(DEFAULT_IS_WORK);
        condition.setJobTypes(jobTypes);
        List<Long> ids = employeeJobUnionMapper.selectIdsByCondition(condition);
        if (CollectionUtils.isEmpty(ids)) {
            return Lists.newArrayList();
        }
        List<EmployeeJobUnion> unions = employeeJobUnionMapper.listEmployeeJobUnionsByCondition(ids);
        return buildEmployeeDtosByUnions(unions);
    }

    @Override
    public List<EmployeeDto> listBeauties() {
        Long storeId = ThreadContext.getStaffStoreId();
        List<Integer> jobTypes = Lists.newArrayList(JobEnum.BEAUTY_STYLIST.getKey(), JobEnum.BEAUTICIAN.getKey(),
                JobEnum.NAIL_TECHNICIAN.getKey());
        EmployeeJobQueryCondition condition = new EmployeeJobQueryCondition();
        condition.setStoreId(storeId);
        condition.setWork(DEFAULT_IS_WORK);
        condition.setJobTypes(jobTypes);
        List<Long> ids = employeeJobUnionMapper.selectIdsByCondition(condition);
        if (CollectionUtils.isEmpty(ids)) {
            return Lists.newArrayList();
        }
        List<EmployeeJobUnion> unions = employeeJobUnionMapper.listEmployeeJobUnionsByCondition(ids);
        return buildEmployeeDtosByUnions(unions);
    }

	@Override
	public List<EmployeeItemVo> getAll() {
		Long storeId = ThreadContext.getStaffStoreId();
		List<EmployeeItemVo> vos = Lists.newArrayList();
		List<EmployeePo> pos = employeeMapper.getAll(storeId);
		for (EmployeePo po : pos) {
            EmployeeItemVo vo = new EmployeeItemVo();
            vo.setEmployeeId(po.getId());
            vo.setEmployeeName(po.getName());
            vos.add(vo);
        }
		return vos;
	}

}
