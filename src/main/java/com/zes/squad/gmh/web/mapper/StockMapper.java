package com.zes.squad.gmh.web.mapper;

import java.util.List;
import java.util.Map;

import com.zes.squad.gmh.web.entity.po.StockPo;
import com.zes.squad.gmh.web.entity.vo.StockVo;

public interface StockMapper {

    List<StockPo> getAll(Long storeId);

    List<StockPo> getByType(Long typeId);

    int insert(StockPo po);

    int update(StockPo po);

    int delById(Long id);

    /**
     * 批量删除
     * 
     * @param ids
     * @return
     */
    int batchDelete(Long[] ids);

    List<StockVo> search(Map<String, Object> map);

    List<StockVo> searchWithoutType(Map<String, Object> map);
}
