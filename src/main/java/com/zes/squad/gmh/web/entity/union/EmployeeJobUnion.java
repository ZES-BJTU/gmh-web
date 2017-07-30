package com.zes.squad.gmh.web.entity.union;

import java.io.Serializable;
import java.util.List;

import com.zes.squad.gmh.web.entity.po.EmployeeJobPo;
import com.zes.squad.gmh.web.entity.po.EmployeePo;

import lombok.Data;

@Data
public class EmployeeJobUnion implements Serializable {

    private static final long   serialVersionUID = 1L;

    private EmployeePo          employeePo;

    private List<EmployeeJobPo> employeeJobPos;

}
