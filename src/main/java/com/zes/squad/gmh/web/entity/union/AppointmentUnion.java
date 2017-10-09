package com.zes.squad.gmh.web.entity.union;

import java.util.List;

import com.zes.squad.gmh.web.entity.po.AppointmentPo;
import com.zes.squad.gmh.web.entity.po.AppointmentProjectPo;
import com.zes.squad.gmh.web.entity.po.EmployeePo;
import com.zes.squad.gmh.web.entity.po.MemberPo;
import com.zes.squad.gmh.web.entity.po.ProjectPo;
import com.zes.squad.gmh.web.entity.po.ProjectTypePo;

import lombok.Data;

@Data
public class AppointmentUnion {

    private Long                       id;

    private AppointmentPo              appointmentPo;

    private List<AppointmentProjectPo> appointmentProjectPos;

    private EmployeePo                 employeePo;

    private MemberPo                   memberPo;

    private ProjectPo                  projectPo;

    private ProjectTypePo              projectTypePo;

}
