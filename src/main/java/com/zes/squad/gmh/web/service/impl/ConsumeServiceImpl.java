package com.zes.squad.gmh.web.service.impl;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
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
import com.zes.squad.gmh.common.exception.ErrorMessage;
import com.zes.squad.gmh.common.exception.GmhException;
import com.zes.squad.gmh.common.util.EnumUtils;
import com.zes.squad.gmh.web.context.ThreadContext;
import com.zes.squad.gmh.web.entity.condition.ConsumeRecordQueryCondition;
import com.zes.squad.gmh.web.entity.dto.ConsumeRecordDto;
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
import com.zes.squad.gmh.web.property.ExportProperties;
import com.zes.squad.gmh.web.service.ConsumeService;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service("consumeRecordService")
public class ConsumeServiceImpl implements ConsumeService {

    private static final String      DEFAULT_NEXT_LINE = "\r\n";

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
        List<MemberUnion> unions = memberUnionMapper.listMemberUnionsByCondition(ThreadContext.getStaffStoreId(),
                dto.getMobile());
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

    @Override
    public void exportToExcel(ConsumeRecordQueryCondition condition) {
        List<ConsumeRecordUnion> unions = consumeRecordUnionMapper.listConsumeRecordsByCondition(condition);
        if (CollectionUtils.isEmpty(unions)) {
            throw new GmhException(ErrorCodeEnum.BUSINESS_EXCEPTION_COLLECTION_IS_EMPTY,
                    ErrorMessage.consumeRecordIsEmpty);
        }
        //写文件
        exportRecordToFile(unions);
    }

    private List<ConsumeRecordDto> buildConsumeRecordDtosByUnions(List<ConsumeRecordUnion> unions) {
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

    private void exportRecordToFile(List<ConsumeRecordUnion> unions) {
        try {
            String charset = ExportProperties.get("charset");
            String path = ExportProperties.get("path");
            String fileName = ThreadContext.getStaffStoreId() + "_" + formatDate(new Date()) + ".csv";
            OutputStream output = new FileOutputStream(new File(path + fileName), true);
            output.write(("序号,门店名称,美容项目名称,美容师,是否会员,顾客姓名,顾客手机号,消费金额,支付方式,消费时间" + DEFAULT_NEXT_LINE).getBytes(charset));
            int i = 1;
            for (ConsumeRecordUnion union : unions) {
                //序号
                output.write(i);
                output.write(",".getBytes(charset));
                //门店名称
                output.write(union.getShopPo().getName().getBytes(charset));
                output.write(",".getBytes(charset));
                //美容项目
                output.write(union.getProjectPo().getName().getBytes(charset));
                output.write(",".getBytes(charset));
                //美容师
                output.write(union.getEmployeePo().getName().getBytes(charset));
                output.write(",".getBytes(charset));
                //是否会员
                if (union.getConsumeRecordPo().getEmployeeId() != null) {
                    output.write("是".getBytes(charset));
                    output.write(",".getBytes(charset));
                } else {
                    output.write("否".getBytes(charset));
                    output.write(",".getBytes(charset));
                }
                //顾客姓名
                output.write(union.getConsumeRecordPo().getConsumerName().getBytes(charset));
                output.write(",".getBytes(charset));
                //顾客手机号
                output.write(union.getConsumeRecordPo().getMobile().getBytes(charset));
                output.write(",".getBytes(charset));
                //消费金额
                output.write(String.valueOf(union.getConsumeRecordPo().getCharge()).getBytes(charset));
                output.write(",".getBytes(charset));
                //支付方式
                output.write(EnumUtils.getDescByKey(ChargeWayEnum.class, union.getConsumeRecordPo().getChargeWay())
                        .getBytes(charset));
                output.write(",".getBytes(charset));
                //消费时间
                output.write(formatDateTime(union.getConsumeRecordPo().getConsumeTime()).getBytes(charset));
                output.write(",".getBytes(charset));
                output.write(DEFAULT_NEXT_LINE.getBytes(charset));
                i++;
            }
            output.flush();
            output.close();
        } catch (Exception e) {
            log.error("消费记录导出到文件异常", e);
        }
    }

    private static String formatDate(Date date) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        return sdf.format(date);
    }

    private static String formatDateTime(Date date) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return sdf.format(date);
    }

}
