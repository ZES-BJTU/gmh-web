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
import com.zes.squad.gmh.web.entity.dto.MemberLevelDto;
import com.zes.squad.gmh.web.entity.dto.StaffDto;
import com.zes.squad.gmh.web.entity.po.MemberLevelPo;
import com.zes.squad.gmh.web.entity.vo.MemberLevelVo;
import com.zes.squad.gmh.web.mapper.MemberLevelMapper;
import com.zes.squad.gmh.web.service.MemberLevelService;

@Service("memberLevelService")
public class MemberLevelServiceImpl implements MemberLevelService {

    @Autowired
    private MemberLevelMapper memberLevelMapper;

    public List<MemberLevelVo> getAll() {
        StaffDto staffDto = ThreadContext.getCurrentStaff();
        List<MemberLevelPo> pos = memberLevelMapper.getAll(staffDto.getStoreId());
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
        int i = 0;
        i = memberLevelMapper.update(po);
        return i;
    }

    public int delByIds(Long[] ids) {
        List<Long> idList = Lists.newArrayList();
        for (Long id : ids) {
            idList.add(id);
        }
        return memberLevelMapper.batchDelete(idList);

    }

    @Override
    public PagedList<MemberLevelDto> listByPage(Integer pageNum, Integer pageSize) {
        StaffDto staff = ThreadContext.getCurrentStaff();
        PageHelper.startPage(pageNum, pageSize);
        List<MemberLevelPo> memberLevelPos = memberLevelMapper.getAll(staff.getStoreId());
        if (CollectionUtils.isEmpty(memberLevelPos)) {
            return PagedList.newMe(pageNum, pageSize, 0L, Lists.newArrayList());
        }
        PageInfo<MemberLevelPo> info = new PageInfo<>(memberLevelPos);
        PagedList<MemberLevelDto> pagedList = CommonConverter.mapPageList(
                PagedList.newMe(info.getPageNum(), info.getPageSize(), info.getTotal(), memberLevelPos),
                MemberLevelDto.class);
        return pagedList;
    }
}
