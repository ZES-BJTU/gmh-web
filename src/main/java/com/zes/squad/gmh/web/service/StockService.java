package com.zes.squad.gmh.web.service;

import java.util.List;

import com.zes.squad.gmh.common.entity.PagedList;
import com.zes.squad.gmh.web.entity.dto.StockDto;
import com.zes.squad.gmh.web.entity.vo.StockVo;

public interface StockService {

    List<StockVo> getAll();

    List<StockVo> getByType(Long stockType);

    int insert(StockDto dto);

    int update(StockDto dto);

    int delByIds(Long[] Id);

    PagedList<StockVo> searchListByPage(Integer pageNum, Integer pageSize, Long typeId, String searchString);
}
