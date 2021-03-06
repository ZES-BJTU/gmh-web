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
import com.zes.squad.gmh.web.context.ThreadContext;
import com.zes.squad.gmh.web.entity.dto.MemberLevelDto;
import com.zes.squad.gmh.web.entity.po.MemberLevelPo;
import com.zes.squad.gmh.web.entity.po.MemberPo;
import com.zes.squad.gmh.web.entity.vo.MemberLevelVo;
import com.zes.squad.gmh.web.mapper.MemberLevelMapper;
import com.zes.squad.gmh.web.mapper.MemberMapper;
import com.zes.squad.gmh.web.service.MemberLevelService;

@Service("memberLevelService")
public class MemberLevelServiceImpl implements MemberLevelService {

    @Autowired
    private MemberLevelMapper memberLevelMapper;
    @Autowired
    private MemberMapper      memberMapper;

    public List<MemberLevelVo> listAllMemberLevels() {
        List<MemberLevelPo> pos = memberLevelMapper.selectByCondition(ThreadContext.getStaffStoreId(), null);
        if (CollectionUtils.isEmpty(pos)) {
            return Lists.newArrayList();
        }
        List<MemberLevelVo> vos = Lists.newArrayList();
        for (MemberLevelPo po : pos) {
            MemberLevelVo vo = CommonConverter.map(po, MemberLevelVo.class);
            vo.setLevelName(po.getName());
            vos.add(vo);
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
        List<MemberPo> pos = memberMapper.selectByIds(ids);
        if (!CollectionUtils.isEmpty(pos)) {
            throw new GmhException(ErrorCodeEnum.BUSINESS_EXCEPTION_OPERATION_NOT_ALLOWED,
                    ErrorMessage.memberNotNullForDeleteLevel);
        }
        return memberLevelMapper.batchDelete(ids);
    }

    @Override
    public PagedList<MemberLevelDto> search(Integer pageNum, Integer pageSize, String searchString) {
        PageHelper.startPage(pageNum, pageSize);
        List<MemberLevelPo> pos = memberLevelMapper.selectByCondition(ThreadContext.getStaffStoreId(), searchString);
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
