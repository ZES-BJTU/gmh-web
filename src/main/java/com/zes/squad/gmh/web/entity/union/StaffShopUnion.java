package com.zes.squad.gmh.web.entity.union;

import com.zes.squad.gmh.web.entity.po.ShopPo;
import com.zes.squad.gmh.web.entity.po.StaffPo;

import lombok.Data;

@Data
public class StaffShopUnion {

    private Long    id;
    private StaffPo staffPo;
    private ShopPo  shopPo;

}
