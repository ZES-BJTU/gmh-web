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
import com.zes.squad.gmh.common.exception.GmhException;
import com.zes.squad.gmh.web.cache.RedisComponent;
import com.zes.squad.gmh.web.constant.Consts;
import com.zes.squad.gmh.web.entity.dto.ShopDto;
import com.zes.squad.gmh.web.entity.po.ShopPo;
import com.zes.squad.gmh.web.entity.vo.ShopVo;
import com.zes.squad.gmh.web.mapper.ShopMapper;
import com.zes.squad.gmh.web.mapper.StaffMapper;
import com.zes.squad.gmh.web.mapper.StaffTokenMapper;
import com.zes.squad.gmh.web.service.ShopService;

@Service("shopService")
public class ShopServiceImpl implements ShopService {

    @Autowired
    private ShopMapper       shopMapper;
    @Autowired
    private StaffMapper      staffMapper;
    @Autowired
    private StaffTokenMapper staffTokenMapper;
    @Autowired
    private RedisComponent   redisComponent;

    public int insert(ShopDto dto) {
        ShopPo po = CommonConverter.map(dto, ShopPo.class);
        po.setName(dto.getShopName());
        return shopMapper.insert(po);
    }

    public int update(ShopDto dto) {
        ShopPo shopPo = shopMapper.selectById(dto.getId());
        if (shopPo == null) {
            throw new GmhException(ErrorCodeEnum.BUSINESS_EXCEPTION_ENTITY_NOT_FOUND.getCode(),
                    ErrorMessage.storeNotFound);
        }
        ShopPo po = CommonConverter.map(dto, ShopPo.class);
        po.setName(dto.getShopName());
        int i = shopMapper.updateSelective(po);
        return i;
    }

    public List<ShopVo> listAllShops() {

        List<ShopPo> pos = shopMapper.selectAll();
        if (CollectionUtils.isEmpty(pos)) {
            return Lists.newArrayList();
        }
        List<ShopVo> vos = Lists.newArrayList();
        for (ShopPo po : pos) {
            ShopVo vo = CommonConverter.map(po, ShopVo.class);
            vo.setShopName(po.getName());
            vos.add(vo);
        }
        return vos;
    }

    public int deleteByIds(Long[] id) {
        List<Long> ids = staffMapper.selectByStoreIds(id);
        if (CollectionUtils.isNotEmpty(ids)) {
            staffMapper.batchDeleteByIds(ids.toArray(new Long[ids.size()]));
            staffTokenMapper.batchDeleteByStaffIds(ids.toArray(new Long[ids.size()]));
            //删除redis里面token
            boolean isCacheValid = redisComponent.isValid();
            if (isCacheValid) {
                List<String> tokens = staffTokenMapper.selectByStaffIds(id);
                if (CollectionUtils.isNotEmpty(tokens)) {
                    List<String> cacheKeys = Lists.newArrayList();
                    for (String token : tokens) {
                        String cacheKey = String.format(Consts.CACHE_KEY_TOKEN_PREFIX, token);
                        cacheKeys.add(cacheKey);
                    }
                    redisComponent.batchDelete(cacheKeys);
                }
            }
        }
        return shopMapper.batchDelete(id);
    }

    @Override
    public PagedList<ShopDto> listByPage(Integer pageNum, Integer pageSize) {
        PageHelper.startPage(pageNum, pageSize);
        List<ShopPo> pos = shopMapper.selectAll();
        if (CollectionUtils.isEmpty(pos)) {
            return PagedLists.newPagedList(pageNum, pageSize);
        }
        PageInfo<ShopPo> info = new PageInfo<>(pos);
        List<ShopDto> dtos = buildShopDtosByPos(pos);
        PagedList<ShopDto> pagedDtos = PagedLists.newPagedList(info.getPageNum(), info.getPageSize(), info.getTotal(),
                dtos);
        return pagedDtos;
    }

    @Override
    public PagedList<ShopDto> searchListByPage(Integer pageNum, Integer pageSize, String searchString) {
        PageHelper.startPage(pageNum, pageSize);
        List<ShopPo> pos = shopMapper.search(searchString);
        if (CollectionUtils.isEmpty(pos)) {
            return PagedLists.newPagedList(pageNum, pageSize);
        }
        PageInfo<ShopPo> info = new PageInfo<>(pos);
        List<ShopDto> dtos = buildShopDtosByPos(pos);
        PagedList<ShopDto> pagedList = PagedLists.newPagedList(info.getPageNum(), info.getPageSize(), info.getTotal(),
                dtos);
        return pagedList;
    }

    private List<ShopDto> buildShopDtosByPos(List<ShopPo> pos) {
        List<ShopDto> dtos = Lists.newArrayList();
        for (ShopPo po : pos) {
            ShopDto dto = CommonConverter.map(po, ShopDto.class);
            dto.setShopName(po.getName());
            dtos.add(dto);
        }
        return dtos;
    }

}
