package com.zes.squad.gmh.web.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.zes.squad.gmh.web.entity.union.StaffShopUnion;

public interface StaffShopUnionMapper {

    /**
     * 条件关联查询
     * 
     * @param searchString
     * @return
     */
    List<StaffShopUnion> listStaffShopUnions(@Param("searchString") String searchString);

}
