package com.zes.squad.gmh.web.service;

import java.util.List;

import com.zes.squad.gmh.common.entity.PagedList;
import com.zes.squad.gmh.web.entity.dto.EmployeeDto;
import com.zes.squad.gmh.web.entity.vo.EmployeeItemVo;

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

    List<EmployeeDto> listBeauties();

    List<EmployeeDto> listCounselors();
    
    List<EmployeeItemVo> getAll();

    int leave(Long[] id);

    int update(EmployeeDto dto);

    PagedList<EmployeeDto> searchListByPage(Integer pageNum, Integer pageSize, String searchString, Integer jobId);
}
