package com.zes.squad.gmh.web.entity.union;

import com.zes.squad.gmh.web.entity.po.MemberLevelPo;
import com.zes.squad.gmh.web.entity.po.MemberPo;

import lombok.Data;

@Data
public class MemberUnion {

    private MemberPo      memberPo;
    private MemberLevelPo memberLevelPo;

}
