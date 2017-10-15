package com.zes.squad.gmh.web.entity.union;

import java.util.List;

import com.zes.squad.gmh.web.entity.po.AppointmentPo;

import lombok.Data;

@Data
public class AppointmentUnion {

    private Long                          id;

    private AppointmentPo                 appointmentPo;

    private List<AppointmentProjectUnion> appointmentProjectUnions;

}
