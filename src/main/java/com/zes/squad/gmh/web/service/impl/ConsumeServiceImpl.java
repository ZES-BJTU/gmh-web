package com.zes.squad.gmh.web.service.impl;

import static com.zes.squad.gmh.web.helper.LogicHelper.ensureCollectionNotEmpty;
import static com.zes.squad.gmh.web.helper.LogicHelper.ensureConditionSatisfied;
import static com.zes.squad.gmh.web.helper.LogicHelper.ensureEntityExist;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Objects;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.VerticalAlignment;
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
import com.zes.squad.gmh.common.enums.JobEnum;
import com.zes.squad.gmh.common.enums.ProjectTypeEnum;
import com.zes.squad.gmh.common.enums.SexEnum;
import com.zes.squad.gmh.common.enums.YesOrNoEnum;
import com.zes.squad.gmh.common.exception.ErrorCodeEnum;
import com.zes.squad.gmh.common.exception.ErrorMessage;
import com.zes.squad.gmh.common.exception.GmhException;
import com.zes.squad.gmh.common.util.EnumUtils;
import com.zes.squad.gmh.web.context.ThreadContext;
import com.zes.squad.gmh.web.entity.condition.ConsumeRecordQueryCondition;
import com.zes.squad.gmh.web.entity.condition.MemberQueryCondition;
import com.zes.squad.gmh.web.entity.condition.ProjectQueryCondition;
import com.zes.squad.gmh.web.entity.dto.ConsumeRecordDto;
import com.zes.squad.gmh.web.entity.po.ConsumeRecordPo;
import com.zes.squad.gmh.web.entity.po.EmployeeJobPo;
import com.zes.squad.gmh.web.entity.po.EmployeePo;
import com.zes.squad.gmh.web.entity.po.MemberPo;
import com.zes.squad.gmh.web.entity.union.ConsumeRecordUnion;
import com.zes.squad.gmh.web.entity.union.MemberUnion;
import com.zes.squad.gmh.web.entity.union.ProjectUnion;
import com.zes.squad.gmh.web.helper.CalculateHelper;
import com.zes.squad.gmh.web.mapper.ConsumeRecordMapper;
import com.zes.squad.gmh.web.mapper.ConsumeRecordUnionMapper;
import com.zes.squad.gmh.web.mapper.EmployeeJobMapper;
import com.zes.squad.gmh.web.mapper.EmployeeMapper;
import com.zes.squad.gmh.web.mapper.MemberMapper;
import com.zes.squad.gmh.web.mapper.MemberUnionMapper;
import com.zes.squad.gmh.web.mapper.ProjectUnionMapper;
import com.zes.squad.gmh.web.service.ConsumeService;

import lombok.Synchronized;
import lombok.extern.slf4j.Slf4j;

@Slf4j
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
    @Autowired
    private EmployeeMapper           employeeMapper;
    @Autowired
    private EmployeeJobMapper        employeeJobMepper;

    @Transactional(propagation = Propagation.REQUIRED)
    @Synchronized
    @Override
    public void createConsumeRecord(ConsumeRecordDto dto) {
        //确保美容项目合法
        ProjectQueryCondition condition = new ProjectQueryCondition();
        condition.setProjectId(dto.getProjectId());
        List<ProjectUnion> projectUnions = projectUnionMapper.listProjectUnionsByCondition(condition);
        ensureCollectionNotEmpty(projectUnions, ErrorMessage.projectNotFound);
        ProjectUnion projectUnion = projectUnions.get(0);
        //确保员工合法
        EmployeePo employeePo = employeeMapper.selectById(dto.getEmployeeId());
        ensureEntityExist(employeePo, ErrorMessage.employeeNotFound);
        if (dto.getCounselorId() != null) {
            employeePo = employeeMapper.selectById(dto.getCounselorId());
            ensureEntityExist(employeePo, ErrorMessage.employeeNotFound);
            List<EmployeeJobPo> employeeJobPos = employeeJobMepper.selectByEmployeeId(employeePo.getId());
            if (CollectionUtils.isEmpty(employeeJobPos)) {
                throw new GmhException(ErrorCodeEnum.BUSINESS_EXCEPTION_COLLECTION_IS_EMPTY,
                        ErrorMessage.employeeJobNotFound);
            }
            boolean contain = false;
            for (EmployeeJobPo po : employeeJobPos) {
                if (po.getJobType() == JobEnum.MANAGER.getKey() || po.getJobType() == JobEnum.COUNSELOR.getKey()) {
                    contain = true;
                    break;
                }
            }
            ensureConditionSatisfied(contain, ErrorMessage.employeeJobTypeIsError);
        }
        MemberQueryCondition memberCondition = new MemberQueryCondition();
        memberCondition.setStoreId(ThreadContext.getStaffStoreId());
        memberCondition.setPhone(dto.getMobile());
        List<MemberUnion> unions = memberUnionMapper.listMemberUnionsByCondition(memberCondition);
        if (!CollectionUtils.isEmpty(unions)) {
            dto.setMember(true);
            dto.setMemberId(unions.get(0).getMemberPo().getId());
            //处理会员消费
            MemberPo memberPo = memberMapper.selectById(dto.getMemberId());
            dto.setAge(CalculateHelper.calculateAgeByBirthday(memberPo.getBirthday()));
            if (dto.getChargeWay() == ChargeWayEnum.CARD.getKey()) {
                ensureEntityExist(memberPo, ErrorMessage.memberNotFound);
                BigDecimal nailMoney = memberPo.getNailMoney();
                BigDecimal beautyMoney = memberPo.getBeautyMoney();
                Integer projectType = projectUnion.getProjectTypePo().getTopType();
                if (projectType == ProjectTypeEnum.NAIL.getKey() || projectType == ProjectTypeEnum.LIDS.getKey()) {
                    ensureConditionSatisfied(nailMoney.compareTo(dto.getCharge()) != -1,
                            ErrorMessage.nailMoneyNotEnough);
                    //扣除美甲美睫储值
                    memberMapper.updateNailMoney(dto.getMemberId(), nailMoney.subtract(dto.getCharge()));
                } else if (projectType == ProjectTypeEnum.BEAUTY.getKey()
                        || projectType == ProjectTypeEnum.PRODUCT.getKey()) {
                    ensureConditionSatisfied(beautyMoney.compareTo(dto.getCharge()) != -1,
                            ErrorMessage.beautyMoneyNotEnough);
                    //扣除美容储值
                    memberMapper.updateBeautyMoney(dto.getMemberId(), beautyMoney.subtract(dto.getCharge()));
                } else {
                    throw new GmhException(ErrorCodeEnum.BUSINESS_EXCEPTION_ILLEGAL_STATUS, "美容项目分类不合法");
                }
            }
        } else {
            ensureConditionSatisfied(dto.getChargeWay() == ChargeWayEnum.CASH.getKey(),
                    ErrorMessage.consumerShouldChooseCash);
            dto.setMember(false);
        }
        ConsumeRecordPo po = CommonConverter.map(dto, ConsumeRecordPo.class);
        po.setConsumeTime(new Date());
        po.setCounselor(dto.getCounselorId());
        consumeRecordmapper.insert(po);
    }

    @Override
    public PagedList<ConsumeRecordDto> listPagedConsumeRecords(ConsumeRecordQueryCondition condition) {
        PageHelper.startPage(condition.getPageNum(), condition.getPageSize());
        List<ConsumeRecordUnion> unions = consumeRecordUnionMapper.listConsumeRecordsByCondition(condition);
        if (CollectionUtils.isEmpty(unions)) {
            return PagedLists.newPagedList(condition.getPageNum(), condition.getPageSize());
        }
        PageInfo<ConsumeRecordUnion> info = new PageInfo<>(unions);
        List<ConsumeRecordDto> dtos = buildConsumeRecordDtosByUnions(unions);
        return PagedLists.newPagedList(info.getPageNum(), info.getPageSize(), info.getTotal(), dtos);
    }

    @Override
    public HSSFWorkbook exportToExcel(ConsumeRecordQueryCondition condition) {
        List<ConsumeRecordUnion> unions = consumeRecordUnionMapper.listConsumeRecordsByCondition(condition);
        if (CollectionUtils.isEmpty(unions)) {
            throw new GmhException(ErrorCodeEnum.BUSINESS_EXCEPTION_COLLECTION_IS_EMPTY,
                    ErrorMessage.consumeRecordIsEmpty);
        }
        //写文件
        HSSFWorkbook workbook = exportRecordToFile(unions);
        if (workbook == null) {
            throw new GmhException(ErrorCodeEnum.BUSINESS_EXCEPTION_OPERATION_FAILED, ErrorMessage.exportOccursFail);
        }
        return workbook;
    }

    private List<ConsumeRecordDto> buildConsumeRecordDtosByUnions(List<ConsumeRecordUnion> unions) {
        List<ConsumeRecordDto> dtos = Lists.newArrayList();
        for (ConsumeRecordUnion union : unions) {
            ConsumeRecordDto dto = CommonConverter.map(union.getConsumeRecordPo(), ConsumeRecordDto.class);
            Long counselorId = union.getConsumeRecordPo().getCounselor();
            dto.setCounselorId(counselorId);
            if (counselorId != null) {
                EmployeePo employeePo = employeeMapper.selectById(counselorId);
                ensureEntityExist(employeePo, ErrorMessage.employeeNotFound);
                dto.setCounselorName(employeePo.getName());
            }
            dto.setStoreName(union.getShopPo().getName());
            dto.setProjectName(union.getProjectPo().getName());
            dto.setEmployeeName(union.getEmployeePo().getName());
            if (union.getConsumeRecordPo().getEmployeeId() != null
                    && union.getConsumeRecordPo().getMember().booleanValue()) {
                dto.setMember(true);
            } else {
                dto.setMember(false);
            }
            dtos.add(dto);
        }
        return dtos;
    }

    private HSSFWorkbook exportRecordToFile(List<ConsumeRecordUnion> unions) {
        try {
            //创建excel工作簿
            HSSFWorkbook workbook = new HSSFWorkbook();
            //创建excel表
            HSSFSheet sheet = workbook.createSheet("消费记录");
            //单元格样式(水平垂直居中)
            HSSFCellStyle style = workbook.createCellStyle();
            style.setAlignment(HorizontalAlignment.LEFT);
            style.setVerticalAlignment(VerticalAlignment.CENTER);
            int row = 0;
            HSSFRow hssfRow = sheet.createRow(row);
            int column = 0;
            HSSFCell cell = hssfRow.createCell(column++);
            cell.setCellType(CellType.STRING);
            cell.setCellValue("序号");
            cell.setCellStyle(style);
            cell = hssfRow.createCell(column++);
            cell.setCellType(CellType.STRING);
            cell.setCellValue("门店名称");
            cell.setCellStyle(style);
            cell = hssfRow.createCell(column++);
            cell.setCellType(CellType.STRING);
            cell.setCellValue("美容项目名称");
            cell.setCellStyle(style);
            cell = hssfRow.createCell(column++);
            cell.setCellType(CellType.STRING);
            cell.setCellValue("操作员");
            cell.setCellStyle(style);
            cell = hssfRow.createCell(column++);
            cell.setCellType(CellType.STRING);
            cell.setCellValue("是否会员");
            cell.setCellStyle(style);
            cell = hssfRow.createCell(column++);
            cell.setCellType(CellType.STRING);
            cell.setCellValue("顾客姓名");
            cell.setCellStyle(style);
            cell = hssfRow.createCell(column++);
            cell.setCellType(CellType.STRING);
            cell.setCellValue("联系方式");
            cell.setCellStyle(style);
            cell = hssfRow.createCell(column++);
            cell.setCellType(CellType.STRING);
            cell.setCellValue("年龄");
            cell.setCellStyle(style);
            cell = hssfRow.createCell(column++);
            cell.setCellType(CellType.STRING);
            cell.setCellValue("性别");
            cell.setCellStyle(style);
            cell = hssfRow.createCell(column++);
            cell.setCellType(CellType.STRING);
            cell.setCellValue("消费金额");
            cell.setCellStyle(style);
            cell = hssfRow.createCell(column++);
            cell.setCellType(CellType.STRING);
            cell.setCellValue("支付方式");
            cell.setCellStyle(style);
            cell = hssfRow.createCell(column++);
            cell.setCellType(CellType.STRING);
            cell.setCellValue("咨询师/经理");
            cell.setCellStyle(style);
            cell = hssfRow.createCell(column++);
            cell.setCellType(CellType.STRING);
            cell.setCellValue("来源");
            cell.setCellStyle(style);
            cell = hssfRow.createCell(column++);
            cell.setCellType(CellType.STRING);
            cell.setCellValue("消费时间");
            cell.setCellStyle(style);
            cell = hssfRow.createCell(column++);
            cell.setCellType(CellType.STRING);
            cell.setCellValue("备注");
            cell.setCellStyle(style);
            ConsumeRecordUnion union = null;
            for (row = 1; row <= unions.size(); row++) {
                column = 0;
                union = unions.get(row - 1);
                hssfRow = sheet.createRow(row);
                //序号
                cell = hssfRow.createCell(column++);
                cell.setCellType(CellType.STRING);
                cell.setCellValue(row);
                cell.setCellStyle(style);
                //门店名称
                String shopName = union.getShopPo().getName();
                cell = hssfRow.createCell(column++);
                cell.setCellType(CellType.STRING);
                cell.setCellValue(shopName == null ? "" : shopName);
                cell.setCellStyle(style);
                //美容项目
                String projectName = union.getProjectPo().getName();
                cell = hssfRow.createCell(column++);
                cell.setCellType(CellType.STRING);
                cell.setCellValue(projectName == null ? "" : projectName);
                cell.setCellStyle(style);
                //操作员
                String employeeName = union.getEmployeePo().getName();
                cell = hssfRow.createCell(column++);
                cell.setCellType(CellType.STRING);
                cell.setCellValue(employeeName == null ? "" : employeeName);
                cell.setCellStyle(style);
                //是否会员
                cell = hssfRow.createCell(column++);
                cell.setCellType(CellType.STRING);
                cell.setCellValue(union.getConsumeRecordPo().getEmployeeId() != null
                        && union.getConsumeRecordPo().getMember().booleanValue() ? YesOrNoEnum.YES.getDesc()
                                : YesOrNoEnum.NO.getDesc());
                cell.setCellStyle(style);
                //顾客姓名
                String consumerName = union.getConsumeRecordPo().getConsumerName();
                cell = hssfRow.createCell(column++);
                cell.setCellType(CellType.STRING);
                cell.setCellValue(consumerName == null ? "" : consumerName);
                cell.setCellStyle(style);
                //联系方式
                String mobile = union.getConsumeRecordPo().getMobile();
                cell = hssfRow.createCell(column++);
                cell.setCellType(CellType.STRING);
                cell.setCellValue(mobile == null ? "" : mobile);
                cell.setCellStyle(style);
                //年龄
                String age = String.valueOf(union.getConsumeRecordPo().getAge());
                cell = hssfRow.createCell(column++);
                cell.setCellType(CellType.STRING);
                cell.setCellValue(age == null || Objects.equals(age, "null") ? "" : age);
                cell.setCellStyle(style);
                //性别
                Integer gender = union.getConsumeRecordPo().getSex();
                cell = hssfRow.createCell(column++);
                cell.setCellType(CellType.STRING);
                cell.setCellValue(gender == null ? "" : EnumUtils.getDescByKey(SexEnum.class, gender));
                cell.setCellStyle(style);
                //消费金额
                BigDecimal charge = union.getConsumeRecordPo().getCharge();
                cell = hssfRow.createCell(column++);
                cell.setCellType(CellType.STRING);
                cell.setCellValue(charge == null ? "" : charge.toString());
                cell.setCellStyle(style);
                //支付方式
                Integer chargeWay = union.getConsumeRecordPo().getChargeWay();
                cell = hssfRow.createCell(column++);
                cell.setCellType(CellType.STRING);
                cell.setCellValue(chargeWay == null ? "" : EnumUtils.getDescByKey(ChargeWayEnum.class, chargeWay));
                cell.setCellStyle(style);
                //咨询师/经理
                cell = hssfRow.createCell(column++);
                cell.setCellType(CellType.STRING);
                Long counselorId = union.getConsumeRecordPo().getCounselor();
                if (counselorId != null) {
                    EmployeePo po = employeeMapper.selectById(counselorId);
                    ensureEntityExist(po, ErrorMessage.counselorNotFound);
                    cell.setCellValue(counselorId == null ? "" : po.getName());
                } else {
                    cell.setCellValue("");
                }
                cell.setCellStyle(style);
                //来源
                String source = union.getConsumeRecordPo().getSource();
                cell = hssfRow.createCell(column++);
                cell.setCellType(CellType.STRING);
                cell.setCellValue(source == null ? "" : source);
                cell.setCellStyle(style);
                //消费时间
                Date consumeTime = union.getConsumeRecordPo().getConsumeTime();
                cell = hssfRow.createCell(column++);
                cell.setCellType(CellType.STRING);
                cell.setCellValue(consumeTime == null ? "" : formatDateTime(consumeTime));
                cell.setCellStyle(style);
                //备注
                String remark = union.getConsumeRecordPo().getRemark();
                cell = hssfRow.createCell(column++);
                cell.setCellType(CellType.STRING);
                cell.setCellValue(remark == null ? "" : remark);
                cell.setCellStyle(style);
            }
            return workbook;
        } catch (

        Exception e) {
            log.error("消费记录导出到文件异常", e);
            return null;
        }
    }

    private static String formatDateTime(Date date) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return sdf.format(date);
    }

}
