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
import com.zes.squad.gmh.common.enums.SexEnum;
import com.zes.squad.gmh.common.util.EnumUtils;
import com.zes.squad.gmh.web.context.ThreadContext;
import com.zes.squad.gmh.web.entity.dto.MemberDto;
import com.zes.squad.gmh.web.entity.dto.StaffDto;
import com.zes.squad.gmh.web.entity.po.MemberPo;
import com.zes.squad.gmh.web.entity.union.MemberUnion;
import com.zes.squad.gmh.web.entity.vo.MemberVo;
import com.zes.squad.gmh.web.mapper.MemberMapper;
import com.zes.squad.gmh.web.mapper.MemberUnionMapper;
import com.zes.squad.gmh.web.service.MemberService;

@Service("memberService")
public class MemberServiceImpl implements MemberService {

    @Autowired
    private MemberMapper      memberMapper;
    @Autowired
    private MemberUnionMapper memberUnionMapper;

    public List<MemberVo> getAll() {
        StaffDto staffDto = ThreadContext.getCurrentStaff();
        List<MemberUnion> unions = memberUnionMapper.listMemberUnionsByCondition(staffDto.getStoreId(), null);
        List<MemberVo> vos = buildMemberVosByUnions(unions);
        return vos;
    }

    @Override
    public int insert(MemberDto dto) {
        StaffDto staffDto = ThreadContext.getCurrentStaff();
        dto.setStoreId(staffDto.getStoreId());
        MemberPo po = CommonConverter.map(dto, MemberPo.class);
        po.setMemberLevelId(dto.getLevelId());
        po.setName(dto.getMemberName());
        return memberMapper.insert(po);
    }

    @Override
    public int update(MemberDto dto) {
        MemberPo po = CommonConverter.map(dto, MemberPo.class);
        po.setMemberLevelId(dto.getLevelId());
        po.setName(dto.getMemberName());
        return memberMapper.update(po);
    }

    @Override
    public int delByIds(Long[] id) {
        return memberMapper.batchDelete(id);
    }

    @Override
    public MemberVo getByPhone(String phone) {
        List<MemberUnion> unions = memberUnionMapper.listMemberUnionsByCondition(null, phone);
        List<MemberVo> vos = buildMemberVosByUnions(unions);
        return vos.get(0);
    }

    @Override
    public PagedList<MemberVo> listByPage(Integer pageNum, Integer pageSize) {
        StaffDto staff = ThreadContext.getCurrentStaff();
        PageHelper.startPage(pageNum, pageSize);
        List<MemberUnion> unions = memberUnionMapper.listMemberUnionsByCondition(staff.getStoreId(), null);
        if (CollectionUtils.isEmpty(unions)) {
            return PagedList.newMe(pageNum, pageSize, 0L, Lists.newArrayList());
        }
        PageInfo<MemberUnion> info = new PageInfo<>(unions);
        PagedList<MemberVo> pagedVos = PagedList.newMe(info.getPageNum(), info.getPageSize(), info.getTotal(),
                buildMemberVosByUnions(unions));
        return pagedVos;
    }

    private List<MemberVo> buildMemberVosByUnions(List<MemberUnion> unions) {
        List<MemberVo> vos = Lists.newArrayList();
        for (MemberUnion union : unions) {
            MemberVo vo = CommonConverter.map(union.getMemberPo(), MemberVo.class);
            vo.setLevelId(union.getMemberPo().getMemberLevelId());
            vo.setLevelName(union.getMemberLevelPo().getName());
            vo.setMemberName(union.getMemberPo().getName());
            vo.setSex(EnumUtils.getDescByKey(SexEnum.class, union.getMemberPo().getSex()));
            vos.add(vo);
        }
        return vos;
    }

}
