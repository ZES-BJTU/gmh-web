package com.zes.squad.gmh.web.entity.union;

import com.zes.squad.gmh.web.entity.po.StockPo;
import com.zes.squad.gmh.web.entity.po.StockTypePo;

import lombok.Data;

@Data
public class StockUnion {

    private StockPo     stockPo;
    private StockTypePo stockTypePo;

}
