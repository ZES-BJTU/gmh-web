package com.zes.squad.gmh.web.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.google.common.collect.Lists;
import com.zes.squad.gmh.common.converter.CommonConverter;
import com.zes.squad.gmh.common.entity.PagedList;
import com.zes.squad.gmh.web.entity.dto.ShopDto;
import com.zes.squad.gmh.web.entity.po.ShopPo;
import com.zes.squad.gmh.web.entity.vo.ShopVo;
import com.zes.squad.gmh.web.mapper.ShopMapper;
import com.zes.squad.gmh.web.service.ShopService;

@Service("shopService")
public class ShopServiceImpl implements ShopService {

    @Autowired
    private ShopMapper shopMapper;

    public int insert(ShopDto dto) {
        ShopPo po = CommonConverter.map(dto, ShopPo.class);
        po.setName(dto.getShopName());
        int i = shopMapper.insert(po);
        return i;
    }

    public int update(ShopDto dto) {
        ShopPo po = CommonConverter.map(dto, ShopPo.class);
        po.setName(dto.getShopName());
        int i = shopMapper.update(po);
        return i;
    }

    public List<ShopVo> getAll() {

        List<ShopPo> pos = shopMapper.getAll();
        if (CollectionUtils.isEmpty(pos)) {
            return Lists.newArrayList();
        }
        List<ShopVo> vos = new ArrayList<ShopVo>();
        for (int i = 0; i < pos.size(); i++) {
            ShopVo vo = CommonConverter.map(pos.get(i), ShopVo.class);
            vo.setShopName(pos.get(i).getName());
            vos.add(vo);
        }
        return vos;
    }

    public int delByIds(Long[] id) {
        int i = 0;
        for (int j = 0; j < id.length; j++) {
            int x = shopMapper.delById(id[j]);
            i = i + x;
        }
        return i;
    }

    @Override
    public PagedList<ShopDto> listByPage(Integer pageNum, Integer pageSize) {
        PageHelper.startPage(pageNum, pageSize);
        List<ShopPo> shopPos = shopMapper.getAll();
        if (CollectionUtils.isEmpty(shopPos)) {
            return PagedList.newMe(pageNum, pageSize, 0L, Lists.newArrayList());
        }
        PageInfo<ShopPo> info = new PageInfo<>(shopPos);
        PagedList<ShopDto> pagedDtos = CommonConverter.mapPageList(
                PagedList.newMe(info.getPageNum(), info.getPageSize(), info.getTotal(), shopPos), ShopDto.class);
        return pagedDtos;
    }

    @Override
    public PagedList<ShopDto> searchListByPage(Integer pageNum, Integer pageSize, String searchString) {
        PageHelper.startPage(pageNum, pageSize);
        List<ShopPo> shopPos = shopMapper.search(searchString);
        if (CollectionUtils.isEmpty(shopPos)) {
            return PagedList.newMe(pageNum, pageSize, 0L, Lists.newArrayList());
        }
        PageInfo<ShopPo> info = new PageInfo<>(shopPos);
        List<ShopDto> dtos = Lists.newArrayList();
        for (ShopPo po : shopPos) {
            ShopDto dto = CommonConverter.map(po, ShopDto.class);
            dto.setShopName(po.getName());
            dtos.add(dto);
        }
        PagedList<ShopDto> pagedList = PagedList.newMe(info.getPageNum(), info.getPageSize(), info.getTotal(), dtos);
        return pagedList;
    }
}
