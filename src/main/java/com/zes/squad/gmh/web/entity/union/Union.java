package com.zes.squad.gmh.web.entity.union;

import java.io.Serializable;

import com.zes.squad.gmh.web.entity.po.StaffPo;

import lombok.Data;

@Data
public class Union implements Serializable {

    private static final long serialVersionUID = 1L;

    private StaffPo           staffPo;

}
