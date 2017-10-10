package com.zes.squad.gmh.web.entity.union;

import com.zes.squad.gmh.web.entity.po.AppointmentProjectPo;
import com.zes.squad.gmh.web.entity.po.EmployeePo;
import com.zes.squad.gmh.web.entity.po.ProjectPo;
import com.zes.squad.gmh.web.entity.po.ProjectTypePo;

import lombok.Data;

@Data
public class AppointmentProjectUnion {

    private Long                 id;
    private AppointmentProjectPo appointmentProjectPo;
    private ProjectTypePo        projectTypePo;
    private ProjectPo            projectPo;
    private EmployeePo           employeePo;

}
