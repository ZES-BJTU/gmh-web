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
import com.zes.squad.gmh.web.context.ThreadContext;
import com.zes.squad.gmh.web.entity.dto.MemberLevelDto;
import com.zes.squad.gmh.web.entity.po.MemberLevelPo;
import com.zes.squad.gmh.web.entity.vo.MemberLevelVo;
import com.zes.squad.gmh.web.mapper.MemberLevelMapper;
import com.zes.squad.gmh.web.service.MemberLevelService;

@Service("memberLevelService")
public class MemberLevelServiceImpl implements MemberLevelService {

    @Autowired
    private MemberLevelMapper memberLevelMapper;

    public List<MemberLevelVo> listAllMemberLevels() {
        List<MemberLevelPo> pos = memberLevelMapper.selectByStoreId(ThreadContext.getStaffStoreId());
        if (CollectionUtils.isEmpty(pos)) {
            return Lists.newArrayList();
        }
        List<MemberLevelVo> vos = Lists.newArrayList();
        for (MemberLevelPo po : pos) {
            MemberLevelVo vo = CommonConverter.map(po, MemberLevelVo.class);
            vo.setLevelName(po.getName());
        }
        return vos;
    }

    public int insert(MemberLevelDto dto) {
        MemberLevelPo po = CommonConverter.map(dto, MemberLevelPo.class);
        po.setName(dto.getLevelName());
        return memberLevelMapper.insert(po);
    }

    public int update(MemberLevelDto dto) {
        MemberLevelPo po = CommonConverter.map(dto, MemberLevelPo.class);
        po.setName(dto.getLevelName());
        return memberLevelMapper.updateSelective(po);
    }

    public int deleteByIds(Long[] ids) {
        return memberLevelMapper.batchDelete(ids);

    }

    @Override
    public PagedList<MemberLevelDto> listByPage(Integer pageNum, Integer pageSize) {
        PageHelper.startPage(pageNum, pageSize);
        List<MemberLevelPo> pos = memberLevelMapper.selectByStoreId(ThreadContext.getStaffStoreId());
        if (CollectionUtils.isEmpty(pos)) {
            return PagedLists.newPagedList(pageNum, pageSize);
        }
        PageInfo<MemberLevelPo> info = new PageInfo<>(pos);
        List<MemberLevelDto> dtos = Lists.newArrayList();
        for (MemberLevelPo po : pos) {
            MemberLevelDto dto = CommonConverter.map(po, MemberLevelDto.class);
            dto.setLevelName(po.getName());
            dtos.add(dto);
        }
        PagedList<MemberLevelDto> pagedList = PagedLists.newPagedList(info.getPageNum(), info.getPageSize(),
                info.getTotal(), dtos);
        return pagedList;
    }
}
