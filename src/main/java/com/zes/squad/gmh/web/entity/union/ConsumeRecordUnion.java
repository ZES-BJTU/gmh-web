package com.zes.squad.gmh.web.entity.union;

import java.util.List;

import com.zes.squad.gmh.web.entity.po.ConsumeRecordPo;
import com.zes.squad.gmh.web.entity.po.ShopPo;

import lombok.Data;

@Data
public class ConsumeRecordUnion {

    private Long                            id;

    private ConsumeRecordPo                 consumeRecordPo;

    private ShopPo                          shopPo;

    private List<ConsumeRecordProjectUnion> consumeRecordProjectUnions;

}
