package com.zes.squad.gmh.web.service.impl;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.google.common.collect.Lists;
import com.zes.squad.gmh.common.converter.CommonConverter;
import com.zes.squad.gmh.common.entity.PagedList;
import com.zes.squad.gmh.common.entity.PagedLists;
import com.zes.squad.gmh.common.enums.ChargeWayEnum;
import com.zes.squad.gmh.common.enums.SexEnum;
import com.zes.squad.gmh.common.exception.ErrorCodeEnum;
import com.zes.squad.gmh.common.exception.ErrorMessage;
import com.zes.squad.gmh.common.exception.GmhException;
import com.zes.squad.gmh.common.util.EnumUtils;
import com.zes.squad.gmh.web.context.ThreadContext;
import com.zes.squad.gmh.web.entity.condition.MemberQueryCondition;
import com.zes.squad.gmh.web.entity.dto.MemberDto;
import com.zes.squad.gmh.web.entity.po.ConsumeRecordPo;
import com.zes.squad.gmh.web.entity.po.MemberLevelPo;
import com.zes.squad.gmh.web.entity.po.MemberPo;
import com.zes.squad.gmh.web.entity.union.MemberUnion;
import com.zes.squad.gmh.web.entity.vo.MemberVo;
import com.zes.squad.gmh.web.helper.CalculateHelper;
import com.zes.squad.gmh.web.helper.LogicHelper;
import com.zes.squad.gmh.web.mapper.ConsumeRecordMapper;
import com.zes.squad.gmh.web.mapper.MemberLevelMapper;
import com.zes.squad.gmh.web.mapper.MemberMapper;
import com.zes.squad.gmh.web.mapper.MemberUnionMapper;
import com.zes.squad.gmh.web.service.MemberService;

@Service("memberService")
public class MemberServiceImpl implements MemberService {

    @Autowired
    private MemberMapper        memberMapper;
    @Autowired
    private MemberLevelMapper   memberLevelMapper;
    @Autowired
    private MemberUnionMapper   memberUnionMapper;
    @Autowired
    private ConsumeRecordMapper consumeRecordMapper;

    public List<MemberVo> listMembers() {
        MemberQueryCondition condition = new MemberQueryCondition();
        condition.setStoreId(ThreadContext.getStaffStoreId());
        List<MemberUnion> unions = memberUnionMapper.listMemberUnionsByCondition(condition);
        List<MemberVo> vos = buildMemberVosByUnions(unions);
        return vos;
    }

    @Override
    public int insert(MemberDto dto) {
        MemberLevelPo memberLevelPo = memberLevelMapper.selectById(dto.getMemberLevelId());
        LogicHelper.ensureEntityExist(memberLevelPo, ErrorMessage.memberLevelNotFound);
        Long storeId = ThreadContext.getStaffStoreId();
        MemberQueryCondition condition = new MemberQueryCondition();
        condition.setStoreId(storeId);
        condition.setPhone(dto.getPhone());
        MemberPo memberPo = memberMapper.selectByCondition(condition);
        LogicHelper.ensureEntityNotExist(memberPo, ErrorMessage.memberExistInStore);
        dto.setStoreId(storeId);
        MemberPo po = CommonConverter.map(dto, MemberPo.class);
        if (po.getJoinDate() == null) {
            po.setJoinDate(new Date());
        }
        po.setAge(CalculateHelper.calculateAgeByBirthday(po.getBirthday()));
        return memberMapper.insert(po);
    }

    @Override
    public int update(MemberDto dto) {
        MemberLevelPo memberLevelPo = memberLevelMapper.selectById(dto.getMemberLevelId());
        LogicHelper.ensureEntityExist(memberLevelPo, ErrorMessage.memberLevelNotFound);
        Long storeId = ThreadContext.getStaffStoreId();
        MemberQueryCondition condition = new MemberQueryCondition();
        condition.setStoreId(storeId);
        condition.setPhone(dto.getPhone());
        MemberPo memberPo = memberMapper.selectByCondition(condition);
        if (memberPo != null && memberPo.getId() != dto.getId()) {
            throw new GmhException(ErrorCodeEnum.BUSINESS_EXCEPTION_OPERATION_NOT_ALLOWED,
                    ErrorMessage.memberExistInStore);
        }
        dto.setStoreId(storeId);
        MemberPo po = CommonConverter.map(dto, MemberPo.class);
        return memberMapper.updateSelective(po);
    }

    @Override
    public int deleteByIds(Long[] id) {
        return memberMapper.batchDelete(id);
    }

    @Override
    public MemberVo queryByPhone(String phone) {
        MemberQueryCondition condition = new MemberQueryCondition();
        condition.setStoreId(ThreadContext.getStaffStoreId());
        condition.setPhone(phone);
        List<MemberUnion> unions = memberUnionMapper.listMemberUnionsByCondition(condition);
        List<MemberVo> vos = buildMemberVosByUnions(unions);
        return vos.get(0);
    }

    @Override
    public PagedList<MemberVo> search(Integer pageNum, Integer pageSize, Long memberLevelId, String searchString) {
        if (memberLevelId != null && memberLevelId == 0) {
            memberLevelId = null;
        }
        PageHelper.startPage(pageNum, pageSize);
        MemberQueryCondition condition = new MemberQueryCondition();
        condition.setStoreId(ThreadContext.getStaffStoreId());
        condition.setMemberLevelId(memberLevelId);
        condition.setSearchString(searchString);
        List<MemberUnion> unions = memberUnionMapper.listMemberUnionsByCondition(condition);
        if (CollectionUtils.isEmpty(unions)) {
            return PagedLists.newPagedList(pageNum, pageSize);
        }
        PageInfo<MemberUnion> info = new PageInfo<>(unions);
        PagedList<MemberVo> pagedVos = PagedLists.newPagedList(info.getPageNum(), info.getPageSize(), info.getTotal(),
                buildMemberVosByUnions(unions));
        return pagedVos;
    }

    private List<MemberVo> buildMemberVosByUnions(List<MemberUnion> unions) {
        List<MemberVo> vos = Lists.newArrayList();
        for (MemberUnion union : unions) {
            MemberVo vo = CommonConverter.map(union.getMemberPo(), MemberVo.class);
            vo.setMemberLevelName(union.getMemberLevelPo().getName());
            vo.setSex(EnumUtils.getDescByKey(SexEnum.class,
                    Integer.valueOf(String.valueOf(union.getMemberPo().getSex()))));
            vo.setNailMoney(
                    union.getMemberPo().getNailMoney() == null ? BigDecimal.ZERO : union.getMemberPo().getNailMoney());
            vo.setBeautyMoney(union.getMemberPo().getBeautyMoney() == null ? BigDecimal.ZERO
                    : union.getMemberPo().getBeautyMoney());
            vos.add(vo);
        }
        return vos;
    }

    @Transactional(propagation = Propagation.REQUIRED)
    @Override
    public void recharge(Long id, BigDecimal nailMoney, BigDecimal beautyMoney) {
        MemberPo memberPo = memberMapper.selectById(id);
        LogicHelper.ensureEntityExist(memberPo, ErrorMessage.memberNotFound);
        if (nailMoney != null && !nailMoney.equals(BigDecimal.ZERO)) {
            memberMapper.updateNailMoney(id, nailMoney.add(memberPo.getNailMoney()));
            ConsumeRecordPo po = new ConsumeRecordPo();
            po.setStoreId(ThreadContext.getStaffStoreId());
            po.setProjectId(1L);
            po.setEmployeeId(null);
            po.setMember(true);
            po.setMemberId(memberPo.getId());
            po.setMobile(memberPo.getPhone());
            po.setAge(memberPo.getAge());
            po.setSex(Integer.valueOf(String.valueOf(memberPo.getSex())));
            po.setConsumerName(memberPo.getName());
            po.setCharge(nailMoney);
            po.setChargeWay(ChargeWayEnum.CASH.getKey());
            po.setCounselor(null);
            po.setSource(null);
            po.setConsumeTime(new Date());
            po.setRemark(null);
            consumeRecordMapper.insert(po);
        }
        if (beautyMoney != null && !beautyMoney.equals(BigDecimal.ZERO)) {
            memberMapper.updateBeautyMoney(id, beautyMoney.add(memberPo.getBeautyMoney()));
            ConsumeRecordPo po = new ConsumeRecordPo();
            po.setStoreId(ThreadContext.getStaffStoreId());
            po.setProjectId(2L);
            po.setEmployeeId(null);
            po.setMember(true);
            po.setMemberId(memberPo.getId());
            po.setMobile(memberPo.getPhone());
            po.setAge(memberPo.getAge());
            po.setSex(Integer.valueOf(String.valueOf(memberPo.getSex())));
            po.setConsumerName(memberPo.getName());
            po.setCharge(beautyMoney);
            po.setChargeWay(ChargeWayEnum.CASH.getKey());
            po.setCounselor(null);
            po.setSource(null);
            po.setConsumeTime(new Date());
            po.setRemark(null);
            consumeRecordMapper.insert(po);
        }
    }

}
