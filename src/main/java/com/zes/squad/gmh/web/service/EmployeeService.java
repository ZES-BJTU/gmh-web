package com.zes.squad.gmh.web.service;

import com.zes.squad.gmh.common.entity.PagedList;
import com.zes.squad.gmh.web.entity.dto.EmployeeDto;

public interface EmployeeService {

    /**
     * 分页查询数据
     * 
     * @param pageNum
     * @param pageSize
     * @return
     */
    PagedList<EmployeeDto> listByPage(Integer pageNum, Integer pageSize);

    EmployeeDto insert(EmployeeDto dto);

    int leave(Long[] id);

    int update(EmployeeDto dto, Long[] jobId);

}
