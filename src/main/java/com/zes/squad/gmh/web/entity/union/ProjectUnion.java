package com.zes.squad.gmh.web.entity.union;

import com.zes.squad.gmh.web.entity.po.ProjectPo;
import com.zes.squad.gmh.web.entity.po.ProjectTypePo;

import lombok.Data;

@Data
public class ProjectUnion {

    private Long          id;
    private ProjectPo     projectPo;
    private ProjectTypePo projectTypePo;

}
