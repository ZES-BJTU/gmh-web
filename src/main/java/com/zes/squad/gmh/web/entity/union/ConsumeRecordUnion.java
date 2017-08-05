package com.zes.squad.gmh.web.entity.union;

import com.zes.squad.gmh.web.entity.po.ConsumeRecordPo;
import com.zes.squad.gmh.web.entity.po.EmployeePo;
import com.zes.squad.gmh.web.entity.po.ProjectPo;
import com.zes.squad.gmh.web.entity.po.ShopPo;

import lombok.Data;

@Data
public class ConsumeRecordUnion {

    private Long            id;

    private ConsumeRecordPo consumeRecordPo;

    private ShopPo          shopPo;

    private ProjectPo       projectPo;

    private EmployeePo      employeePo;

}
