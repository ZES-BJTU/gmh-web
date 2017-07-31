package com.zes.squad.gmh.web.entity.union;

import com.zes.squad.gmh.web.entity.po.AppointmentPo;
import com.zes.squad.gmh.web.entity.po.EmployeePo;
import com.zes.squad.gmh.web.entity.po.MemberPo;
import com.zes.squad.gmh.web.entity.po.ProjectPo;

import lombok.Data;

@Data
public class AppointmentUnion {

    private AppointmentPo appointmentPo;

    private EmployeePo    employeePo;

    private MemberPo      memberPo;

    private ProjectPo     projectPo;

}
