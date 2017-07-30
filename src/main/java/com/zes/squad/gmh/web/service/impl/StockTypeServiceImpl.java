package com.zes.squad.gmh.web.service.impl;

import java.util.List;
import java.util.Map;

import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.zes.squad.gmh.common.converter.CommonConverter;
import com.zes.squad.gmh.common.entity.PagedList;
import com.zes.squad.gmh.web.context.ThreadContext;
import com.zes.squad.gmh.web.entity.dto.StaffDto;
import com.zes.squad.gmh.web.entity.dto.StockTypeDto;
import com.zes.squad.gmh.web.entity.po.StockTypePo;
import com.zes.squad.gmh.web.entity.vo.StockTypeVo;
import com.zes.squad.gmh.web.mapper.StockTypeMapper;
import com.zes.squad.gmh.web.service.StockTypeService;

@Service("stockTypeService")
public class StockTypeServiceImpl implements StockTypeService {
    @Autowired
    private StockTypeMapper stockTypeMapper;

    public List<StockTypeVo> getAll() {
        StaffDto staffDto = ThreadContext.getCurrentStaff();
        List<StockTypePo> pos = stockTypeMapper.getAll(staffDto.getStoreId());
        if (CollectionUtils.isEmpty(pos)) {
            return Lists.newArrayList();
        }
        List<StockTypeVo> vos = Lists.newArrayList();
        for (StockTypePo po : pos) {
            StockTypeVo vo = new StockTypeVo();
            vo.setId(po.getId());
            vo.setTypeName(po.getName());
            vos.add(vo);
        }
        return vos;
    }

    public int insert(StockTypeDto dto) {
        StockTypePo po = CommonConverter.map(dto, StockTypePo.class);
        po.setName(dto.getTypeName());
        return stockTypeMapper.insert(po);
    }

    public int update(StockTypeDto dto) {
        StockTypePo po = CommonConverter.map(dto, StockTypePo.class);
        po.setName(dto.getTypeName());
        return stockTypeMapper.update(po);
    }

    public int delByIds(Long[] id) {
        return stockTypeMapper.batchDelete(id);
    }

    @Override
    public PagedList<StockTypeDto> searchListByPage(Integer pageNum, Integer pageSize, String searchString) {
        PageHelper.startPage(pageNum, pageSize);
        StaffDto staffDto = ThreadContext.getCurrentStaff();
        Map<String, Object> map = Maps.newHashMap();
        map.put("storeId", staffDto.getStoreId());
        map.put("searchString", searchString);
        List<StockTypePo> pos = stockTypeMapper.search(map);
        if (CollectionUtils.isEmpty(pos)) {
            return PagedList.newMe(pageNum, pageSize, 0L, Lists.newArrayList());
        }
        PageInfo<StockTypePo> info = new PageInfo<>(pos);
        List<StockTypeDto> dtos = Lists.newArrayList();
        for (StockTypePo po : pos) {
            StockTypeDto dto = CommonConverter.map(po, StockTypeDto.class);
            dto.setTypeName(po.getName());
            dtos.add(dto);
        }
        PagedList<StockTypeDto> pagedDtos = PagedList.newMe(info.getPageNum(), info.getPageSize(), info.getTotal(),
                dtos);
        return pagedDtos;
    }
}
