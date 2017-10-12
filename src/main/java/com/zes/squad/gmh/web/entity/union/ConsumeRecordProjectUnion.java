package com.zes.squad.gmh.web.entity.union;

import com.zes.squad.gmh.web.entity.po.ConsumeRecordProjectPo;
import com.zes.squad.gmh.web.entity.po.EmployeePo;
import com.zes.squad.gmh.web.entity.po.ProjectPo;
import com.zes.squad.gmh.web.entity.po.ProjectTypePo;

import lombok.Data;

@Data
public class ConsumeRecordProjectUnion {

    private Long                   id;
    private ConsumeRecordProjectPo consumeRecordProjectPo;
    private ProjectPo              projectPo;
    private ProjectTypePo          projectTypePo;
    private EmployeePo             employeePo;

}
