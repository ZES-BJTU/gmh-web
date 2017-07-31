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
import com.zes.squad.gmh.common.enums.ChargeWayEnum;
import com.zes.squad.gmh.common.enums.ProjectTypeEnum;
import com.zes.squad.gmh.common.exception.ErrorCodeEnum;
import com.zes.squad.gmh.common.exception.GmhException;
import com.zes.squad.gmh.web.context.ThreadContext;
import com.zes.squad.gmh.web.entity.condition.ConsumeRecordQueryCondition;
import com.zes.squad.gmh.web.entity.dto.ConsumeRecordDto;
import com.zes.squad.gmh.web.entity.dto.StaffDto;
import com.zes.squad.gmh.web.entity.po.ConsumeRecordPo;
import com.zes.squad.gmh.web.entity.po.MemberPo;
import com.zes.squad.gmh.web.entity.union.ConsumeRecordUnion;
import com.zes.squad.gmh.web.entity.union.MemberUnion;
import com.zes.squad.gmh.web.entity.union.ProjectUnion;
import com.zes.squad.gmh.web.mapper.ConsumeRecordMapper;
import com.zes.squad.gmh.web.mapper.ConsumeRecordUnionMapper;
import com.zes.squad.gmh.web.mapper.MemberMapper;
import com.zes.squad.gmh.web.mapper.MemberUnionMapper;
import com.zes.squad.gmh.web.mapper.ProjectUnionMapper;
import com.zes.squad.gmh.web.service.ConsumeService;

@Service("consumeRecordService")
public class ConsumeServiceImpl implements ConsumeService {

    @Autowired
    private ConsumeRecordMapper      consumeRecordmapper;
    @Autowired
    private MemberUnionMapper        memberUnionMapper;
    @Autowired
    private ProjectUnionMapper       projectUnionMapper;
    @Autowired
    private MemberMapper             memberMapper;
    @Autowired
    private ConsumeRecordUnionMapper consumeRecordUnionMapper;

    @Transactional(propagation = Propagation.REQUIRED)
    @Override
    public void addConsumeRecord(ConsumeRecordDto dto) {
        StaffDto staff = ThreadContext.getCurrentStaff();
        if (staff == null) {
            throw new GmhException(ErrorCodeEnum.BUSINESS_EXCEPTION_ENTITY_NOT_FOUND, "获取用户信息失败");
        }
        List<MemberUnion> unions = memberUnionMapper.listMemberUnionsByCondition(staff.getStoreId(), dto.getMobile());
        if (!CollectionUtils.isEmpty(unions)) {
            dto.setMemberId(unions.get(0).getMemberPo().getId());
            //处理会员消费
            if (dto.getChargeWay() == ChargeWayEnum.CARD.getKey()) {
                ProjectUnion union = projectUnionMapper.listProjectUnions(dto.getProjectId()).get(0);
                if (union == null) {
                    throw new GmhException(ErrorCodeEnum.BUSINESS_EXCEPTION_ENTITY_NOT_FOUND, "获取美容项目信息失败");
                }
                MemberPo memberPo = memberMapper.selectById(dto.getMemberId());
                BigDecimal nailMoney = memberPo.getNailMoney();
                BigDecimal beautyMoney = memberPo.getBeautyMoney();
                Integer projectType = union.getProjectTypePo().getTopType();
                if (projectType == ProjectTypeEnum.NAIL.getKey() || projectType == ProjectTypeEnum.LIDS.getKey()) {
                    synchronized (this) {
                        if (nailMoney.compareTo(dto.getCharge()) == -1) {
                            throw new GmhException(ErrorCodeEnum.BUSINESS_EXCEPTION_OPERATION_NOT_ALLOWED, "美甲美睫储值不足");
                        }
                        //扣除美甲美睫储值
                        memberMapper.updateNailMoney(dto.getMemberId(), nailMoney.subtract(dto.getCharge()));
                    }
                } else if (projectType == ProjectTypeEnum.BEAUTY.getKey()
                        || projectType == ProjectTypeEnum.PRODUCT.getKey()) {
                    synchronized (this) {
                        if (beautyMoney.compareTo(dto.getCharge()) == -1) {
                            throw new GmhException(ErrorCodeEnum.BUSINESS_EXCEPTION_OPERATION_NOT_ALLOWED, "美容储值不足");
                        }
                        //扣除美容储值
                        memberMapper.updateBeautyMoney(dto.getMemberId(), beautyMoney.subtract(dto.getCharge()));
                    }
                } else {
                    throw new GmhException(ErrorCodeEnum.BUSINESS_EXCEPTION_ILLEGAL_STATUS, "美容项目分类不合法");
                }
            }
        }
        ConsumeRecordPo po = CommonConverter.map(dto, ConsumeRecordPo.class);
        po.setConsumeTime(new Date());
        consumeRecordmapper.insert(po);
    }

    @Override
    public PagedList<ConsumeRecordDto> listPagedConsumeRecords(ConsumeRecordQueryCondition condition) {
        PageHelper.startPage(condition.getPageNum(), condition.getPageSize());
        List<ConsumeRecordUnion> unions = consumeRecordUnionMapper.listConsumeRecordsByCondition(condition);
        if (CollectionUtils.isEmpty(unions)) {
            return PagedList.newMe(condition.getPageNum(), condition.getPageSize(), 0L, Lists.newArrayList());
        }
        PageInfo<ConsumeRecordUnion> info = new PageInfo<>(unions);
        List<ConsumeRecordDto> dtos = buildConsumeRecordDtosByUnions(unions);
        return PagedList.newMe(info.getPageNum(), info.getPageSize(), info.getTotal(), dtos);
    }

    public List<ConsumeRecordDto> buildConsumeRecordDtosByUnions(List<ConsumeRecordUnion> unions) {
        List<ConsumeRecordDto> dtos = Lists.newArrayList();
        for (ConsumeRecordUnion union : unions) {
            ConsumeRecordDto dto = CommonConverter.map(union.getConsumeRecordPo(), ConsumeRecordDto.class);
            dto.setStoreName(union.getShopPo().getName());
            dto.setProjectName(union.getProjectPo().getName());
            dto.setEmployeeName(union.getEmployeePo().getName());
            if (union.getConsumeRecordPo().getEmployeeId() != null) {
                dto.setMember(true);
            } else {
                dto.setMember(false);
            }
            dtos.add(dto);
        }
        return dtos;
    }

}
