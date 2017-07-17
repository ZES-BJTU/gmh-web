package com.zes.squad.gmh.web.service.impl;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.zes.squad.gmh.common.converter.CommonConverter;
import com.zes.squad.gmh.common.entity.PagedList;
import com.zes.squad.gmh.web.context.ThreadContext;
import com.zes.squad.gmh.web.entity.dto.EmployeeDto;
import com.zes.squad.gmh.web.entity.dto.StaffDto;
import com.zes.squad.gmh.web.entity.po.EmployeePo;
import com.zes.squad.gmh.web.mapper.EmployeeJobMapper;
import com.zes.squad.gmh.web.mapper.EmployeeMapper;
import com.zes.squad.gmh.web.service.EmployeeService;

@Service("employeeService")
public class EmployeeServiceImpl implements EmployeeService {

    @Autowired
    private EmployeeMapper    employeeMapper;
    @Autowired
    private EmployeeJobMapper emJobMapper;

    @Override
    public PagedList<EmployeeDto> listByPage(Integer pageNum, Integer pageSize) {
        StaffDto staff = ThreadContext.getCurrentStaff();
        PageHelper.startPage(pageNum, pageSize);
        List<EmployeePo> employeePos = employeeMapper.listByPage(staff.getStoreId());
        PageInfo<EmployeePo> info = new PageInfo<>(employeePos);
        PagedList<EmployeeDto> pagedList = CommonConverter.mapPageList(
                PagedList.newMe(info.getPageNum(), info.getPageSize(), info.getTotal(), employeePos),
                EmployeeDto.class);
        return pagedList;
    }

    public EmployeeDto insert(EmployeeDto dto) {
        EmployeePo po = CommonConverter.map(dto, EmployeePo.class);
        Date entry = new Date();
        po.setEntryDate(entry);
        employeeMapper.insert(po);
        EmployeeDto newDto = CommonConverter.map(po, EmployeeDto.class);
        return newDto;
    }

    public int leave(Long[] id) {
        int i = 0;
        Date leave = new Date();
        Map map = new HashMap();

        for (int j = 0; j < id.length; j++) {
            map.put("id", id[j]);
            map.put("leave", leave);
            i = i + employeeMapper.leave(map);
        }
        return i;
    }

    public int update(EmployeeDto dto, Long[] jobId) {
        EmployeePo po = CommonConverter.map(dto, EmployeePo.class);
        int i = employeeMapper.update(po);
        if (i > 0) {
            int j = emJobMapper.delByEmId(dto.getId());
            if (j > 0) {
                for (int m = 0; m < jobId.length; m++) {
                    emJobMapper.insert(dto.getId(), jobId[m]);
                }
            }
        }
        return i;
    }

}
