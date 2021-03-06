package com.zes.squad.gmh.web.entity.vo;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import lombok.Data;

@Data
public class PrintSingleVo {

    private String                       address;
    private String                       shopPhone;
    private String                       memberPhone;
    private String                       memberName;
    private Date                         consumeTime;
    private String                       chargeWay;
    private String                       chargeCard;
    private BigDecimal                   nailMoney;
    private BigDecimal                   beautyMoney;
    private List<ConsumeRecordProjectVo> consumeRecordProjectVos;
    private List<MemberVo>               memberVos;

}
