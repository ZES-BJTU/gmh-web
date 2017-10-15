package com.zes.squad.gmh.web.entity.union;

import java.util.List;

import com.zes.squad.gmh.web.entity.po.AppointmentPo;
import com.zes.squad.gmh.web.entity.po.AppointmentProjectPo;

import lombok.Data;

@Data
public class AppointmentTimeUnion {

    private Long                       id;

    private AppointmentPo              appointmentPo;

    private List<AppointmentProjectPo> appointmentProjectPos;

}
