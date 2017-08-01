package com.zes.squad.gmh.web.service.impl;

import java.util.List;

import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.google.common.collect.Lists;
import com.zes.squad.gmh.common.converter.CommonConverter;
import com.zes.squad.gmh.common.entity.PagedList;
import com.zes.squad.gmh.web.context.ThreadContext;
import com.zes.squad.gmh.web.entity.dto.StockDto;
import com.zes.squad.gmh.web.entity.po.StockPo;
import com.zes.squad.gmh.web.entity.union.StockUnion;
import com.zes.squad.gmh.web.entity.vo.StockVo;
import com.zes.squad.gmh.web.mapper.StockMapper;
import com.zes.squad.gmh.web.mapper.StockUnionMapper;
import com.zes.squad.gmh.web.service.StockService;

@Service("stockService")
public class StockServiceImpl implements StockService {

    @Autowired
    private StockMapper      stockMapper;
    @Autowired
    private StockUnionMapper stockUnionMapper;

    @Override
    public List<StockVo> getAll() {
        List<StockUnion> unions = stockUnionMapper.listStockUnions(ThreadContext.getStaffStoreId(), null, null);
        if (CollectionUtils.isEmpty(unions)) {
            return Lists.newArrayList();
        }
        List<StockVo> vos = buildStockVosByUnion(unions);
        return vos;
    }

    @Override
    public List<StockVo> getByType(Long typeId) {
        List<StockUnion> unions = stockUnionMapper.listStockUnions(null, typeId, null);
        if (CollectionUtils.isEmpty(unions)) {
            return Lists.newArrayList();
        }
        List<StockVo> vos = buildStockVosByUnion(unions);
        return vos;
    }

    @Override
    public int insert(StockDto dto) {
        StockPo po = CommonConverter.map(dto, StockPo.class);
        po.setStockTypeId(dto.getTypeId());
        po.setName(dto.getStockName());
        return stockMapper.insert(po);
    }

    @Override
    public int update(StockDto dto) {
        StockPo po = CommonConverter.map(dto, StockPo.class);
        po.setStockTypeId(dto.getTypeId());
        po.setName(dto.getStockName());
        return stockMapper.update(po);
    }

    @Override
    public int delByIds(Long[] id) {
        return stockMapper.batchDelete(id);
    }

    @Override
    public PagedList<StockVo> searchListByPage(Integer pageNum, Integer pageSize, Long typeId, String searchString) {
        if (typeId == null || typeId == 0L) {
            typeId = null;
        }
        PageHelper.startPage(pageNum, pageSize);
        List<StockUnion> unions = stockUnionMapper.listStockUnions(ThreadContext.getStaffStoreId(), typeId,
                searchString);
        PageInfo<StockUnion> info = new PageInfo<>(unions);
        List<StockVo> vos = buildStockVosByUnion(unions);
        PagedList<StockVo> pagedList = PagedList.newMe(info.getPageNum(), info.getPageSize(), info.getTotal(), vos);
        return pagedList;
    }

    private List<StockVo> buildStockVosByUnion(List<StockUnion> unions) {
        List<StockVo> vos = Lists.newArrayList();
        for (StockUnion union : unions) {
            StockVo vo = CommonConverter.map(union.getStockPo(), StockVo.class);
            vo.setStockName(union.getStockPo().getName());
            vo.setStoreId(union.getStockTypePo().getStoreId());
            vo.setTypeId(union.getStockTypePo().getId());
            vo.setTypeName(union.getStockTypePo().getName());
            vos.add(vo);
        }
        return vos;
    }

}
