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
import com.google.common.base.Strings;
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
import com.zes.squad.gmh.web.entity.dto.ConsumeRecordProjectDto;
import com.zes.squad.gmh.web.entity.po.ConsumeRecordPo;
import com.zes.squad.gmh.web.entity.po.ConsumeRecordProjectPo;
import com.zes.squad.gmh.web.entity.po.EmployeeJobPo;
import com.zes.squad.gmh.web.entity.po.EmployeePo;
import com.zes.squad.gmh.web.entity.po.MemberLevelPo;
import com.zes.squad.gmh.web.entity.po.MemberPo;
import com.zes.squad.gmh.web.entity.po.ShopPo;
import com.zes.squad.gmh.web.entity.union.ConsumeRecordProjectUnion;
import com.zes.squad.gmh.web.entity.union.ConsumeRecordUnion;
import com.zes.squad.gmh.web.entity.union.MemberUnion;
import com.zes.squad.gmh.web.entity.union.ProjectUnion;
import com.zes.squad.gmh.web.entity.vo.ConsumeRecordProjectVo;
import com.zes.squad.gmh.web.entity.vo.MemberVo;
import com.zes.squad.gmh.web.entity.vo.PrintSingleVo;
import com.zes.squad.gmh.web.helper.CalculateHelper;
import com.zes.squad.gmh.web.helper.LogicHelper;
import com.zes.squad.gmh.web.mapper.ConsumeRecordMapper;
import com.zes.squad.gmh.web.mapper.ConsumeRecordProjectMapper;
import com.zes.squad.gmh.web.mapper.ConsumeRecordProjectUnionMapper;
import com.zes.squad.gmh.web.mapper.ConsumeRecordUnionMapper;
import com.zes.squad.gmh.web.mapper.EmployeeJobMapper;
import com.zes.squad.gmh.web.mapper.EmployeeMapper;
import com.zes.squad.gmh.web.mapper.MemberLevelMapper;
import com.zes.squad.gmh.web.mapper.MemberMapper;
import com.zes.squad.gmh.web.mapper.MemberUnionMapper;
import com.zes.squad.gmh.web.mapper.ProjectUnionMapper;
import com.zes.squad.gmh.web.mapper.ShopMapper;
import com.zes.squad.gmh.web.service.ConsumeService;

import lombok.Synchronized;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service("consumeRecordService")
public class ConsumeServiceImpl implements ConsumeService {

    @Autowired
    private ConsumeRecordMapper             consumeRecordMapper;
    @Autowired
    private ConsumeRecordProjectMapper      consumeRecordProjectMapper;
    @Autowired
    private ConsumeRecordProjectUnionMapper consumeRecordProjectUnionMapper;
    @Autowired
    private MemberUnionMapper               memberUnionMapper;
    @Autowired
    private ProjectUnionMapper              projectUnionMapper;
    @Autowired
    private MemberMapper                    memberMapper;
    @Autowired
    private MemberLevelMapper               memberLevelMapper;
    @Autowired
    private ConsumeRecordUnionMapper        consumeRecordUnionMapper;
    @Autowired
    private EmployeeMapper                  employeeMapper;
    @Autowired
    private EmployeeJobMapper               employeeJobMepper;
    @Autowired
    private ShopMapper                      shopMapper;

    @Transactional(propagation = Propagation.REQUIRED)
    @Synchronized
    @Override
    public void createConsumeRecord(ConsumeRecordDto dto) {
        for (ConsumeRecordProjectDto projectDto : dto.getConsumeRecordProjectDtos()) {
            if (projectDto.getCounselorId() != null) {
                EmployeePo employeePo = employeeMapper.selectById(projectDto.getCounselorId());
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
        }
        MemberQueryCondition memberCondition = new MemberQueryCondition();
        memberCondition.setStoreId(ThreadContext.getStaffStoreId());
        memberCondition.setPhone(dto.getMobile());
        List<MemberUnion> unions = memberUnionMapper.listMemberUnionsByCondition(memberCondition);
        if (!CollectionUtils.isEmpty(unions)) {
            MemberPo member = unions.get(0).getMemberPo();
            dto.setMember(true);
            dto.setMemberId(member.getId());
            dto.setAge(member.getAge());
            dto.setConsumerName(member.getName());
            dto.setSex(Integer.valueOf(String.valueOf(member.getSex())));
            //处理会员消费
            MemberPo memberPo = memberMapper.selectById(dto.getMemberId());
            dto.setAge(CalculateHelper.calculateAgeByBirthday(memberPo.getBirthday()));
            if (dto.getChargeWay() == ChargeWayEnum.CARD.getKey()) {
                ensureEntityExist(memberPo, ErrorMessage.memberNotFound);
                BigDecimal nailMoney = memberPo.getNailMoney();
                BigDecimal beautyMoney = memberPo.getBeautyMoney();

                for (ConsumeRecordProjectDto projectDto : dto.getConsumeRecordProjectDtos()) {
                    //确保美容项目合法
                    ProjectQueryCondition condition = new ProjectQueryCondition();
                    condition.setProjectId(projectDto.getProjectId());
                    List<ProjectUnion> projectUnions = projectUnionMapper.listProjectUnionsByCondition(condition);
                    ensureCollectionNotEmpty(projectUnions, ErrorMessage.projectNotFound);
                    //确保员工合法
                    EmployeePo employeePo = employeeMapper.selectById(projectDto.getEmployeeId());
                    ensureEntityExist(employeePo, ErrorMessage.employeeNotFound);

                    ProjectUnion projectUnion = projectUnions.get(0);
                    Integer projectType = projectUnion.getProjectTypePo().getTopType();
                    if (projectType == ProjectTypeEnum.NAIL.getKey() || projectType == ProjectTypeEnum.LIDS.getKey()) {
                        ensureConditionSatisfied(nailMoney.compareTo(projectDto.getCharge()) != -1,
                                ErrorMessage.nailMoneyNotEnough);
                        //扣除美甲美睫储值
                        nailMoney = nailMoney.subtract(projectDto.getCharge());
                        ensureConditionSatisfied(nailMoney.compareTo(BigDecimal.ZERO) != -1, "美甲美睫储值不足");
                        memberMapper.updateNailMoney(dto.getMemberId(), nailMoney);
                    } else if (projectType == ProjectTypeEnum.BEAUTY.getKey()
                            || projectType == ProjectTypeEnum.PRODUCT.getKey()) {
                        ensureConditionSatisfied(beautyMoney.compareTo(projectDto.getCharge()) != -1,
                                ErrorMessage.beautyMoneyNotEnough);
                        //扣除美容储值
                        beautyMoney = beautyMoney.subtract(projectDto.getCharge());
                        ensureConditionSatisfied(beautyMoney.compareTo(BigDecimal.ZERO) != -1, "美容储值不足");
                        memberMapper.updateBeautyMoney(dto.getMemberId(), beautyMoney);
                    } else {
                        throw new GmhException(ErrorCodeEnum.BUSINESS_EXCEPTION_ILLEGAL_STATUS, "美容项目分类不合法");
                    }

                }

            }
        } else {
            ensureConditionSatisfied(dto.getChargeWay() == ChargeWayEnum.CASH.getKey(),
                    ErrorMessage.consumerShouldChooseCash);
            dto.setMember(false);
        }
        ConsumeRecordPo po = CommonConverter.map(dto, ConsumeRecordPo.class);
        po.setConsumeTime(new Date());
        consumeRecordMapper.insert(po);
        LogicHelper.ensureConditionSatisfied(po.getId() != null, "消费记录标识生成失败");
        List<ConsumeRecordProjectPo> pos = CommonConverter.mapList(dto.getConsumeRecordProjectDtos(),
                ConsumeRecordProjectPo.class);
        for (ConsumeRecordProjectPo projectPo : pos) {
            projectPo.setConsumeRecordId(po.getId());
        }
        consumeRecordProjectMapper.batchInsert(pos);
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
        return PagedLists.newPagedList(info.getPageNum(), info.getPageSize(), unions.size(), dtos);
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
            //            Long counselorId = union.getConsumeRecordPo().getCounselor();
            //            dto.setCounselorId(counselorId);
            //            if (counselorId != null) {
            //                EmployeePo employeePo = employeeMapper.selectById(counselorId);
            //                ensureEntityExist(employeePo, ErrorMessage.employeeNotFound);
            //                dto.setCounselorName(employeePo.getName());
            //            }
            dto.setStoreName(union.getShopPo().getName());
            //            dto.setProjectName(union.getProjectPo() == null ? "" : union.getProjectPo().getName());
            //            dto.setEmployeeName(union.getEmployeePo() == null ? "" : union.getEmployeePo().getName());
            if (union.getConsumeRecordPo().getMember().booleanValue()
                    && union.getConsumeRecordPo().getMemberId() != null) {
                MemberPo memberPo = memberMapper.selectById(union.getConsumeRecordPo().getMemberId());
                ensureEntityExist(memberPo, "获取会员信息失败");
                dto.setMemberName(memberPo.getName());
            }
            List<ConsumeRecordProjectUnion> projectUnions = union.getConsumeRecordProjectUnions();
            List<ConsumeRecordProjectDto> projectDtos = Lists.newArrayList();
            for (ConsumeRecordProjectUnion projectUnion : projectUnions) {
                ConsumeRecordProjectDto projectDto = new ConsumeRecordProjectDto();
                projectDto.setProjectId(projectUnion.getConsumeRecordProjectPo().getProjectId());
                projectDto.setProjectName(projectUnion.getProjectPo().getName());
                projectDto.setEmployeeId(projectUnion.getConsumeRecordProjectPo().getEmployeeId());
                projectDto.setEmployeeName(projectUnion.getEmployeePo().getName());
                projectDto.setCharge(projectUnion.getConsumeRecordProjectPo().getCharge());
                Long counselorId = projectUnion.getConsumeRecordProjectPo().getCounselorId();
                if (counselorId != null) {
                    projectDto.setCounselorId(counselorId);
                    EmployeePo employeePo = employeeMapper.selectById(counselorId);
                    ensureEntityExist(employeePo, ErrorMessage.employeeNotFound);
                    projectDto.setCounselorName(employeePo.getName());
                }
                projectDto.setRetailPrice(projectUnion.getProjectPo().getRetailPrice());
                projectDtos.add(projectDto);
            }
            dto.setConsumeRecordProjectDtos(projectDtos);

            if (union.getConsumeRecordPo().getEmployeeId() != null
                    && union.getConsumeRecordPo().getMember().booleanValue()) {
                dto.setMember(true);
            } else {
                dto.setMember(false);
            }
            dto.setSource(Strings.isNullOrEmpty(union.getConsumeRecordPo().getSource()) ? ""
                    : union.getConsumeRecordPo().getSource());
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
                //                //美容项目
                //                String projectName = union.getProjectPo().getName();
                //                cell = hssfRow.createCell(column++);
                //                cell.setCellType(CellType.STRING);
                //                cell.setCellValue(projectName == null ? "" : projectName);
                //                cell.setCellStyle(style);
                //                //操作员
                //                String employeeName = union.getEmployeePo().getName();
                //                cell = hssfRow.createCell(column++);
                //                cell.setCellType(CellType.STRING);
                //                cell.setCellValue(employeeName == null ? "" : employeeName);
                //                cell.setCellStyle(style);
                //美容项目
                List<ConsumeRecordProjectUnion> projectUnions = union.getConsumeRecordProjectUnions();
                String projectName = "";
                String employeeName = "";
                for (ConsumeRecordProjectUnion projectUnion : projectUnions) {
                    projectName = projectName + "\n" + projectUnion.getProjectPo().getName();
                    employeeName = employeeName + "\n" + projectUnion.getEmployeePo().getName();
                }
                projectName = projectName.substring(1);
                cell = hssfRow.createCell(column++);
                cell.setCellType(CellType.STRING);
                cell.setCellValue(projectName == null ? "" : projectName);
                cell.setCellStyle(style);
                //操作员
                employeeName = employeeName.substring(1);
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
                //                //咨询师/经理
                //                cell = hssfRow.createCell(column++);
                //                cell.setCellType(CellType.STRING);
                //                Long counselorId = union.getConsumeRecordPo().getCounselor();
                //                if (counselorId != null) {
                //                    EmployeePo po = employeeMapper.selectById(counselorId);
                //                    ensureEntityExist(po, ErrorMessage.counselorNotFound);
                //                    cell.setCellValue(counselorId == null ? "" : po.getName());
                //                } else {
                //                    cell.setCellValue("");
                //                }
                //                cell.setCellStyle(style);
                //咨询师/经理
                cell = hssfRow.createCell(column++);
                cell.setCellType(CellType.STRING);
                String conselorName = "";
                for (ConsumeRecordProjectUnion projectUnion : projectUnions) {
                    if (projectUnion.getConsumeRecordProjectPo().getCounselorId() != null) {
                        EmployeePo po = employeeMapper
                                .selectById(projectUnion.getConsumeRecordProjectPo().getCounselorId());
                        ensureEntityExist(po, ErrorMessage.counselorNotFound);
                        conselorName = conselorName + "," + po.getName();
                    }
                }
                cell.setCellValue(conselorName.substring(1));
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
        } catch (Exception e) {
            log.error("消费记录导出到文件异常", e);
            return null;
        }
    }

    private static String formatDateTime(Date date) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return sdf.format(date);
    }

    @Override
    public List<MemberVo> listMemberCardsByPhone(String phone) {
        MemberQueryCondition condition = new MemberQueryCondition();
        condition.setPhone(phone);
        condition.setStoreId(ThreadContext.getStaffStoreId());
        List<MemberUnion> unions = memberUnionMapper.listMemberUnionsByCondition(condition);
        if (CollectionUtils.isEmpty(unions)) {
            return Lists.newArrayList();
        }
        return buildMemberVosByUnions(unions);
    }

    private List<MemberVo> buildMemberVosByUnions(List<MemberUnion> unions) {
        List<MemberVo> vos = Lists.newArrayList();
        for (MemberUnion union : unions) {
            MemberVo vo = CommonConverter.map(union.getMemberPo(), MemberVo.class);
            vo.setMemberLevelName(union.getMemberLevelPo().getName());
            vos.add(vo);
        }
        return vos;
    }

    @Override
    public void modifyConsumeRecord(ConsumeRecordDto dto) {
        //回复会员卡储值
        if (dto.getChargeWay().intValue() == ChargeWayEnum.CARD.getKey()) {
            Long memberId = consumeRecordMapper.selectMemberById(dto.getId());
            ensureConditionSatisfied(memberId != null, "根据消费记录获取会员信息失败");
            MemberPo memberPo = memberMapper.selectById(memberId);
            ensureEntityExist(memberPo, "查询会员信息失败");
            BigDecimal nailMoney = memberPo.getNailMoney();
            BigDecimal beautyMoney = memberPo.getBeautyMoney();
            List<ConsumeRecordProjectUnion> unions = consumeRecordProjectUnionMapper
                    .selectByConsumeRecordId(dto.getId());
            ensureCollectionNotEmpty(unions, "消费记录项目查询失败");
            for (ConsumeRecordProjectUnion union : unions) {
                if (union.getProjectTypePo().getTopType() == ProjectTypeEnum.NAIL.getKey()
                        || union.getProjectTypePo().getTopType() == ProjectTypeEnum.LIDS.getKey()) {
                    nailMoney = nailMoney.add(union.getConsumeRecordProjectPo().getCharge());
                    memberMapper.updateNailMoney(memberId, nailMoney);
                }
                if (union.getProjectTypePo().getTopType() == ProjectTypeEnum.BEAUTY.getKey()
                        || union.getProjectTypePo().getTopType() == ProjectTypeEnum.PRODUCT.getKey()) {
                    beautyMoney = beautyMoney.add(union.getConsumeRecordProjectPo().getCharge());
                    memberMapper.updateBeautyMoney(memberId, beautyMoney);
                }
            }
        }
        //删除老记录
        consumeRecordMapper.deleteById(dto.getId());
        consumeRecordProjectMapper.deleteByConsumeRecordId(dto.getId());
        //复用新建消费记录
        for (ConsumeRecordProjectDto projectDto : dto.getConsumeRecordProjectDtos()) {
            if (projectDto.getCounselorId() != null) {
                EmployeePo employeePo = employeeMapper.selectById(projectDto.getCounselorId());
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
        }
        MemberQueryCondition memberCondition = new MemberQueryCondition();
        memberCondition.setStoreId(ThreadContext.getStaffStoreId());
        memberCondition.setPhone(dto.getMobile());
        List<MemberUnion> unions = memberUnionMapper.listMemberUnionsByCondition(memberCondition);
        if (!CollectionUtils.isEmpty(unions)) {
            MemberPo member = unions.get(0).getMemberPo();
            dto.setMember(true);
            dto.setMemberId(member.getId());
            dto.setAge(member.getAge());
            dto.setConsumerName(member.getName());
            dto.setSex(Integer.valueOf(String.valueOf(member.getSex())));
            //处理会员消费
            MemberPo memberPo = memberMapper.selectById(dto.getMemberId());
            dto.setAge(CalculateHelper.calculateAgeByBirthday(memberPo.getBirthday()));
            if (dto.getChargeWay() == ChargeWayEnum.CARD.getKey()) {
                ensureEntityExist(memberPo, ErrorMessage.memberNotFound);
                BigDecimal nailMoney = memberPo.getNailMoney();
                BigDecimal beautyMoney = memberPo.getBeautyMoney();

                for (ConsumeRecordProjectDto projectDto : dto.getConsumeRecordProjectDtos()) {
                    //确保美容项目合法
                    ProjectQueryCondition condition = new ProjectQueryCondition();
                    condition.setProjectId(projectDto.getProjectId());
                    List<ProjectUnion> projectUnions = projectUnionMapper.listProjectUnionsByCondition(condition);
                    ensureCollectionNotEmpty(projectUnions, ErrorMessage.projectNotFound);
                    //确保员工合法
                    EmployeePo employeePo = employeeMapper.selectById(projectDto.getEmployeeId());
                    ensureEntityExist(employeePo, ErrorMessage.employeeNotFound);

                    ProjectUnion projectUnion = projectUnions.get(0);
                    Integer projectType = projectUnion.getProjectTypePo().getTopType();
                    if (projectType == ProjectTypeEnum.NAIL.getKey() || projectType == ProjectTypeEnum.LIDS.getKey()) {
                        ensureConditionSatisfied(nailMoney.compareTo(projectDto.getCharge()) != -1,
                                ErrorMessage.nailMoneyNotEnough);
                        //扣除美甲美睫储值
                        nailMoney = nailMoney.subtract(projectDto.getCharge());
                        ensureConditionSatisfied(nailMoney.compareTo(BigDecimal.ZERO) != -1, "美甲美睫储值不足");
                        memberMapper.updateNailMoney(dto.getMemberId(), nailMoney);
                    } else if (projectType == ProjectTypeEnum.BEAUTY.getKey()
                            || projectType == ProjectTypeEnum.PRODUCT.getKey()) {
                        ensureConditionSatisfied(beautyMoney.compareTo(projectDto.getCharge()) != -1,
                                ErrorMessage.beautyMoneyNotEnough);
                        //扣除美容储值
                        beautyMoney = beautyMoney.subtract(projectDto.getCharge());
                        ensureConditionSatisfied(beautyMoney.compareTo(BigDecimal.ZERO) != -1, "美容储值不足");
                        memberMapper.updateBeautyMoney(dto.getMemberId(), beautyMoney);
                    } else {
                        throw new GmhException(ErrorCodeEnum.BUSINESS_EXCEPTION_ILLEGAL_STATUS, "美容项目分类不合法");
                    }

                }

            }
        } else {
            ensureConditionSatisfied(dto.getChargeWay() == ChargeWayEnum.CASH.getKey(),
                    ErrorMessage.consumerShouldChooseCash);
            dto.setMember(false);
        }
        ConsumeRecordPo po = CommonConverter.map(dto, ConsumeRecordPo.class);
        po.setConsumeTime(new Date());
        consumeRecordMapper.insert(po);
        LogicHelper.ensureConditionSatisfied(po.getId() != null, "消费记录标识生成失败");
        List<ConsumeRecordProjectPo> pos = CommonConverter.mapList(dto.getConsumeRecordProjectDtos(),
                ConsumeRecordProjectPo.class);
        for (ConsumeRecordProjectPo projectPo : pos) {
            projectPo.setConsumeRecordId(po.getId());
        }
        consumeRecordProjectMapper.batchInsert(pos);
    }

    @Override
    public PrintSingleVo queryById(Long id) {
        ConsumeRecordPo consumeRecordPo = consumeRecordMapper.selectById(id);
        ensureEntityExist(consumeRecordPo, "获取消费记录失败");
        ShopPo shopPo = shopMapper.selectById(ThreadContext.getStaffStoreId());
        ensureEntityExist(shopPo, "获取门店信息失败");
        PrintSingleVo vo = new PrintSingleVo();
        if (consumeRecordPo.getMemberId() != null) {
            MemberPo memberPo = memberMapper.selectById(consumeRecordPo.getMemberId());
            ensureEntityExist(memberPo, "会员信息查询失败");
            MemberLevelPo memberLevelPo = memberLevelMapper.selectById(memberPo.getMemberLevelId());
            ensureEntityExist(memberLevelPo, "会员卡信息查询失败");
            vo.setChargeCard(memberLevelPo.getName());
        }
        vo.setAddress(shopPo.getAddress());
        vo.setShopPhone(shopPo.getPhone());
        vo.setMemberPhone(consumeRecordPo.getMobile());
        MemberQueryCondition condition = new MemberQueryCondition();
        condition.setStoreId(ThreadContext.getStaffStoreId());
        condition.setPhone(consumeRecordPo.getMobile());
        List<MemberUnion> memberUnions = memberUnionMapper.listMemberUnionsByCondition(condition);
        if (CollectionUtils.isNotEmpty(memberUnions)) {
            List<MemberVo> vos = Lists.newArrayList();
            for (MemberUnion union : memberUnions) {
                MemberVo memberVo = new MemberVo();
                memberVo.setId(union.getMemberPo().getId());
                memberVo.setStoreId(ThreadContext.getStaffStoreId());
                memberVo.setMemberLevelId(union.getMemberLevelPo().getId());
                memberVo.setMemberLevelName(union.getMemberLevelPo().getName());
                memberVo.setPhone(union.getMemberPo().getPhone());
                memberVo.setName(union.getMemberPo().getName());
                memberVo.setSex(EnumUtils.getDescByKey(SexEnum.class,
                        Integer.valueOf(String.valueOf(union.getMemberPo().getSex()))));
                memberVo.setAge(union.getMemberPo().getAge());
                memberVo.setBirthday(union.getMemberPo().getBirthday());
                memberVo.setJoinDate(union.getMemberPo().getJoinDate());
                memberVo.setValidDate(union.getMemberPo().getValidDate());
                memberVo.setNailMoney(union.getMemberPo().getNailMoney());
                memberVo.setBeautyMoney(union.getMemberPo().getBeautyMoney());
                vos.add(memberVo);
            }
            vo.setMemberVos(vos);
        }
        if (consumeRecordPo.getMember().booleanValue()) {
            MemberPo memberPo = memberMapper.selectById(consumeRecordPo.getMemberId());
            ensureEntityExist(memberPo, "获取会员信息失败");
            vo.setNailMoney(memberPo.getNailMoney());
            vo.setBeautyMoney(memberPo.getBeautyMoney());
        }
        vo.setConsumeTime(consumeRecordPo.getConsumeTime());
        vo.setChargeWay(EnumUtils.getDescByKey(ChargeWayEnum.class, consumeRecordPo.getChargeWay()));
        List<ConsumeRecordProjectUnion> unions = consumeRecordProjectUnionMapper.selectByConsumeRecordId(id);
        ensureCollectionNotEmpty(unions, "获取消费记录项目失败");
        List<ConsumeRecordProjectVo> vos = Lists.newArrayList();
        for (ConsumeRecordProjectUnion union : unions) {
            ConsumeRecordProjectVo projectVo = new ConsumeRecordProjectVo();
            projectVo.setProjectId(union.getConsumeRecordProjectPo().getProjectId());
            projectVo.setProjectName(union.getProjectPo().getName());
            projectVo.setEmployeeId(union.getConsumeRecordProjectPo().getEmployeeId());
            projectVo.setEmployeeName(union.getEmployeePo().getName());
            projectVo.setCharge(union.getConsumeRecordProjectPo().getCharge());
            projectVo.setRetailPrice(union.getProjectPo().getRetailPrice());
            Long counselorId = union.getConsumeRecordProjectPo().getCounselorId();
            if (counselorId != null) {
                EmployeePo employeePo = employeeMapper.selectById(counselorId);
                ensureEntityExist(employeePo, "获取咨询师/经理信息失败");
                projectVo.setCounselorId(counselorId);
                projectVo.setCounselorName(employeePo.getName());
            }
            vos.add(projectVo);
        }
        vo.setConsumeRecordProjectVos(vos);
        return vo;
    }

    @Override
    public List<PrintSingleVo> listConsumeRecords(String mobile, Long memberId, Date startTime, Date endTime) {
        List<ConsumeRecordPo> consumeRecordPos = consumeRecordMapper.selectByCondition(ThreadContext.getStaffStoreId(),
                mobile, memberId, startTime, endTime);
        if (CollectionUtils.isEmpty(consumeRecordPos)) {
            return Lists.newArrayList();
        }
        List<PrintSingleVo> printSingleVos = Lists.newArrayList();
        for (ConsumeRecordPo consumeRecordPo : consumeRecordPos) {
            ShopPo shopPo = shopMapper.selectById(ThreadContext.getStaffStoreId());
            ensureEntityExist(shopPo, "获取门店信息失败");
            PrintSingleVo vo = new PrintSingleVo();
            MemberQueryCondition condition = new MemberQueryCondition();
            condition.setStoreId(ThreadContext.getStaffStoreId());
            condition.setPhone(consumeRecordPo.getMobile());
            List<MemberUnion> memberUnions = memberUnionMapper.listMemberUnionsByCondition(condition);
            if (CollectionUtils.isNotEmpty(memberUnions)) {
                List<MemberVo> vos = Lists.newArrayList();
                for (MemberUnion union : memberUnions) {
                    MemberVo memberVo = new MemberVo();
                    memberVo.setId(union.getMemberPo().getId());
                    memberVo.setStoreId(ThreadContext.getStaffStoreId());
                    memberVo.setMemberLevelId(union.getMemberLevelPo().getId());
                    memberVo.setMemberLevelName(union.getMemberLevelPo().getName());
                    memberVo.setPhone(union.getMemberPo().getPhone());
                    memberVo.setName(union.getMemberPo().getName());
                    memberVo.setSex(EnumUtils.getDescByKey(SexEnum.class,
                            Integer.valueOf(String.valueOf(union.getMemberPo().getSex()))));
                    memberVo.setAge(union.getMemberPo().getAge());
                    memberVo.setBirthday(union.getMemberPo().getBirthday());
                    memberVo.setJoinDate(union.getMemberPo().getJoinDate());
                    memberVo.setValidDate(union.getMemberPo().getValidDate());
                    memberVo.setNailMoney(union.getMemberPo().getNailMoney());
                    memberVo.setBeautyMoney(union.getMemberPo().getBeautyMoney());
                    vos.add(memberVo);
                }
                vo.setMemberVos(vos);
            }
            if (memberId != null) {
                MemberPo memberPo = memberMapper.selectById(memberId);
                ensureEntityExist(memberPo, "会员信息获取失败");
                MemberLevelPo memberLevelPo = memberLevelMapper.selectById(memberPo.getMemberLevelId());
                ensureEntityExist(memberLevelPo, "会员卡信息获取失败");
                vo.setChargeCard(memberLevelPo.getName());
            } else {
                if (consumeRecordPo.getMemberId() != null) {
                    MemberPo memberPo = memberMapper.selectById(consumeRecordPo.getMemberId());
                    ensureEntityExist(memberPo, "会员信息查询失败");
                    MemberLevelPo memberLevelPo = memberLevelMapper.selectById(memberPo.getMemberLevelId());
                    ensureEntityExist(memberLevelPo, "会员卡信息查询失败");
                    vo.setChargeCard(memberLevelPo.getName());
                }
            }
            vo.setAddress(shopPo.getAddress());
            vo.setShopPhone(shopPo.getPhone());
            vo.setMemberPhone(consumeRecordPo.getMobile());
            if (consumeRecordPo.getMember().booleanValue()) {
                MemberPo memberPo = memberMapper.selectById(consumeRecordPo.getMemberId());
                ensureEntityExist(memberPo, "获取会员信息失败");
                vo.setNailMoney(memberPo.getNailMoney());
                vo.setBeautyMoney(memberPo.getBeautyMoney());
            }
            vo.setConsumeTime(consumeRecordPo.getConsumeTime());
            vo.setChargeWay(EnumUtils.getDescByKey(ChargeWayEnum.class, consumeRecordPo.getChargeWay()));
            List<ConsumeRecordProjectUnion> unions = consumeRecordProjectUnionMapper
                    .selectByConsumeRecordId(consumeRecordPo.getId());
            ensureCollectionNotEmpty(unions, "获取消费记录项目失败");
            List<ConsumeRecordProjectVo> vos = Lists.newArrayList();
            for (ConsumeRecordProjectUnion union : unions) {
                ConsumeRecordProjectVo projectVo = new ConsumeRecordProjectVo();
                projectVo.setProjectId(union.getConsumeRecordProjectPo().getProjectId());
                projectVo.setProjectName(union.getProjectPo().getName());
                projectVo.setEmployeeId(union.getConsumeRecordProjectPo().getEmployeeId());
                projectVo.setEmployeeName(union.getEmployeePo().getName());
                projectVo.setCharge(union.getConsumeRecordProjectPo().getCharge());
                projectVo.setRetailPrice(union.getProjectPo().getRetailPrice());
                Long counselorId = union.getConsumeRecordProjectPo().getCounselorId();
                if (counselorId != null) {
                    EmployeePo employeePo = employeeMapper.selectById(counselorId);
                    ensureEntityExist(employeePo, "获取咨询师/经理信息失败");
                    projectVo.setCounselorId(counselorId);
                    projectVo.setCounselorName(employeePo.getName());
                }
                vos.add(projectVo);
            }
            vo.setConsumeRecordProjectVos(vos);
            printSingleVos.add(vo);
        }
        return printSingleVos;
    }

}
