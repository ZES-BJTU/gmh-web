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
import com.zes.squad.gmh.common.entity.PagedLists;
import com.zes.squad.gmh.common.exception.ErrorCodeEnum;
import com.zes.squad.gmh.common.exception.ErrorMessage;
import com.zes.squad.gmh.common.exception.GmhCacheException;
import com.zes.squad.gmh.web.context.ThreadContext;
import com.zes.squad.gmh.web.entity.condition.StockQueryCondition;
import com.zes.squad.gmh.web.entity.dto.StockDto;
import com.zes.squad.gmh.web.entity.po.StockPo;
import com.zes.squad.gmh.web.entity.po.StockTypePo;
import com.zes.squad.gmh.web.entity.union.StockUnion;
import com.zes.squad.gmh.web.entity.vo.StockVo;
import com.zes.squad.gmh.web.mapper.StockMapper;
import com.zes.squad.gmh.web.mapper.StockTypeMapper;
import com.zes.squad.gmh.web.mapper.StockUnionMapper;
import com.zes.squad.gmh.web.service.StockService;

@Service("stockService")
public class StockServiceImpl implements StockService {

    @Autowired
    private StockMapper      stockMapper;
    @Autowired
    private StockUnionMapper stockUnionMapper;
    @Autowired
    private StockTypeMapper  stockTypeMapper;

    @Override
    public List<StockVo> listStockVos() {
        StockQueryCondition condition = new StockQueryCondition();
        condition.setStoreId(ThreadContext.getStaffStoreId());
        List<StockUnion> unions = stockUnionMapper.listStockUnionsByCondition(condition);
        if (CollectionUtils.isEmpty(unions)) {
            return Lists.newArrayList();
        }
        List<StockVo> vos = buildStockVosByUnions(unions);
        return vos;
    }

    @Override
    public List<StockVo> listStockVosByType(Long stockTypeId) {
        StockQueryCondition condition = new StockQueryCondition();
        condition.setTypeId(stockTypeId);
        List<StockUnion> unions = stockUnionMapper.listStockUnionsByCondition(condition);
        if (CollectionUtils.isEmpty(unions)) {
            return Lists.newArrayList();
        }
        List<StockVo> vos = buildStockVosByUnions(unions);
        return vos;
    }

    @Override
    public int insert(StockDto dto) {
        StockTypePo typePo = stockTypeMapper.selectById(dto.getTypeId());
        if (typePo == null) {
            throw new GmhCacheException(ErrorCodeEnum.BUSINESS_EXCEPTION_ENTITY_NOT_FOUND,
                    ErrorMessage.stockTypeNotFound);
        }
        StockPo po = CommonConverter.map(dto, StockPo.class);
        po.setStockTypeId(dto.getTypeId());
        po.setName(dto.getStockName());
        return stockMapper.insert(po);
    }

    @Override
    public int update(StockDto dto) {
        StockTypePo typePo = stockTypeMapper.selectById(dto.getTypeId());
        if (typePo == null) {
            throw new GmhCacheException(ErrorCodeEnum.BUSINESS_EXCEPTION_ENTITY_NOT_FOUND,
                    ErrorMessage.stockTypeNotFound);
        }
        StockPo po = CommonConverter.map(dto, StockPo.class);
        po.setStockTypeId(dto.getTypeId());
        po.setName(dto.getStockName());
        return stockMapper.update(po);
    }

    @Override
    public int deleteByIds(Long[] id) {
        return stockMapper.batchDelete(id);
    }

    @Override
    public PagedList<StockVo> searchListByPage(Integer pageNum, Integer pageSize, Long typeId, String searchString) {
        if (typeId == null || typeId == 0L) {
            typeId = null;
        }
        PageHelper.startPage(pageNum, pageSize);
        StockQueryCondition condition = new StockQueryCondition();
        condition.setStoreId(ThreadContext.getStaffStoreId());
        condition.setTypeId(typeId);
        condition.setSearchString(searchString);
        List<StockUnion> unions = stockUnionMapper.listStockUnionsByCondition(condition);
        if (CollectionUtils.isEmpty(unions)) {
            return PagedLists.newPagedList(pageNum, pageSize);
        }
        PageInfo<StockUnion> info = new PageInfo<>(unions);
        List<StockVo> vos = buildStockVosByUnions(unions);
        PagedList<StockVo> pagedList = PagedLists.newPagedList(info.getPageNum(), info.getPageSize(), info.getTotal(),
                vos);
        return pagedList;
    }

    private List<StockVo> buildStockVosByUnions(List<StockUnion> unions) {
        List<StockVo> vos = Lists.newArrayList();
        for (StockUnion union : unions) {
            StockVo vo = CommonConverter.map(union.getStockPo(), StockVo.class);
            vo.setStoreId(union.getStockTypePo().getStoreId());
            vo.setTypeId(union.getStockPo().getStockTypeId());
            vo.setStockName(union.getStockPo().getName());
            vo.setTypeName(union.getStockTypePo().getName());
            vos.add(vo);
        }
        return vos;
    }

}
