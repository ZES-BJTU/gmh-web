package com.zes.squad.gmh.web.service.impl;

import static com.zes.squad.gmh.web.helper.LogicHelper.ensureConditionSatisfied;
import static com.zes.squad.gmh.web.helper.LogicHelper.ensureEntityExist;
import static com.zes.squad.gmh.web.helper.LogicHelper.ensureParameterExist;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import org.apache.commons.collections4.CollectionUtils;
import org.joda.time.DateTime;
import org.joda.time.Minutes;
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
import com.zes.squad.gmh.common.enums.AppointmentStatusEnum;
import com.zes.squad.gmh.common.enums.ChargeWayEnum;
import com.zes.squad.gmh.common.enums.JobEnum;
import com.zes.squad.gmh.common.enums.ProjectTypeEnum;
import com.zes.squad.gmh.common.enums.YesOrNoEnum;
import com.zes.squad.gmh.common.exception.ErrorCodeEnum;
import com.zes.squad.gmh.common.exception.ErrorMessage;
import com.zes.squad.gmh.common.exception.GmhException;
import com.zes.squad.gmh.common.util.EnumUtils;
import com.zes.squad.gmh.web.context.ThreadContext;
import com.zes.squad.gmh.web.entity.condition.AppointmentQueryCondition;
import com.zes.squad.gmh.web.entity.condition.AppointmentUnionQueryCondition;
import com.zes.squad.gmh.web.entity.condition.MemberQueryCondition;
import com.zes.squad.gmh.web.entity.condition.ProjectQueryCondition;
import com.zes.squad.gmh.web.entity.dto.AppointmentDto;
import com.zes.squad.gmh.web.entity.po.AppointmentPo;
import com.zes.squad.gmh.web.entity.po.ConsumeRecordPo;
import com.zes.squad.gmh.web.entity.po.EmployeeJobPo;
import com.zes.squad.gmh.web.entity.po.EmployeePo;
import com.zes.squad.gmh.web.entity.po.MemberPo;
import com.zes.squad.gmh.web.entity.po.ProjectPo;
import com.zes.squad.gmh.web.entity.po.ProjectTypePo;
import com.zes.squad.gmh.web.entity.union.AppointmentUnion;
import com.zes.squad.gmh.web.entity.union.ProjectUnion;
import com.zes.squad.gmh.web.entity.vo.AppointmentVo;
import com.zes.squad.gmh.web.entity.vo.EmployeeItemVo;
import com.zes.squad.gmh.web.entity.vo.TimeVo;
import com.zes.squad.gmh.web.helper.LogicHelper;
import com.zes.squad.gmh.web.mapper.AppointmentMapper;
import com.zes.squad.gmh.web.mapper.AppointmentUnionMapper;
import com.zes.squad.gmh.web.mapper.ConsumeRecordMapper;
import com.zes.squad.gmh.web.mapper.EmployeeJobMapper;
import com.zes.squad.gmh.web.mapper.EmployeeMapper;
import com.zes.squad.gmh.web.mapper.MemberMapper;
import com.zes.squad.gmh.web.mapper.ProjectMapper;
import com.zes.squad.gmh.web.mapper.ProjectTypeMapper;
import com.zes.squad.gmh.web.mapper.ProjectUnionMapper;
import com.zes.squad.gmh.web.service.AppointmentService;

import lombok.Synchronized;

@Service("appointmentJobService")
public class AppointmentServiceImpl implements AppointmentService {

    private static final int       DEFAULT_REMIND_MINUTE = 30;

    private static final int       DEFAULT_START_HOUR    = 8;
    private static final int       DEFAULT_END_HOUR      = 22;

    private static final int       TOTAL_MINUTE          = (DEFAULT_END_HOUR - DEFAULT_START_HOUR) * 60;

    @Autowired
    private AppointmentMapper      appointmentMapper;
    @Autowired
    private AppointmentUnionMapper appointmentUnionMapper;
    @Autowired
    private ConsumeRecordMapper    consumeRecordMapper;
    @Autowired
    private EmployeeMapper         employeeMapper;
    @Autowired
    private EmployeeJobMapper      employeeJobMapper;
    @Autowired
    private ProjectUnionMapper     projectUnionMapper;
    @Autowired
    private ProjectMapper          projectMapper;
    @Autowired
    private MemberMapper           memberMapper;
    @Autowired
    private ProjectTypeMapper      projectTypeMapper;

    @Override
    public List<AppointmentVo> listAllAppointments() {
        AppointmentUnionQueryCondition condition = new AppointmentUnionQueryCondition();
        condition.setStoreId(ThreadContext.getStaffStoreId());
        condition.setStatus(
                Lists.newArrayList(AppointmentStatusEnum.TO_DO.getKey(), AppointmentStatusEnum.IN_PROCESS.getKey()));
        List<AppointmentUnion> unions = appointmentUnionMapper.listAppointmentUnionsByCondition(condition);
        List<AppointmentVo> vos = buildAppointmentVosByUnions(unions);
        return vos;
    }

    @Override
    public List<AppointmentVo> queryByPhone(String phone) {
        AppointmentUnionQueryCondition condition = new AppointmentUnionQueryCondition();
        condition.setStoreId(ThreadContext.getStaffStoreId());
        condition.setPhone(phone);
        List<AppointmentUnion> unions = appointmentUnionMapper.listAppointmentUnionsByCondition(condition);
        List<AppointmentVo> vos = buildAppointmentVosByUnions(unions);
        return vos;
    }

    @Override
    public AppointmentUnion queryById(Long id) {
        ensureParameterExist(id, ErrorMessage.appointmentIdIsNull);
        AppointmentUnion union = appointmentUnionMapper.selectById(id);
        ensureEntityExist(union, ErrorMessage.appointmentIsNull);
        return union;
    }

    @Override
    public int insert(AppointmentDto dto) {
        MemberQueryCondition memberQueryCondition = new MemberQueryCondition();
        memberQueryCondition.setPhone(dto.getPhone());
        MemberPo memberPo = memberMapper.selectByCondition(memberQueryCondition);
        ProjectPo projectPo = projectMapper.selectById(dto.getProjectId());
        ensureEntityExist(projectPo, ErrorMessage.projectNotFound);
        EmployeePo employeePo = employeeMapper.selectById(dto.getEmployeeId());
        ensureEntityExist(employeePo, ErrorMessage.employeeNotFound);
        int count = appointmentMapper.selectByCondition(ThreadContext.getStaffStoreId(), dto.getPhone(), null,
                Lists.newArrayList(AppointmentStatusEnum.TO_DO.getKey(), AppointmentStatusEnum.IN_PROCESS.getKey()),
                dto.getBeginTime(), dto.getEndTime());
        ensureConditionSatisfied(count == 0, ErrorMessage.appointmentMemberTimeIsConflicted);
        count = appointmentMapper.selectByCondition(ThreadContext.getStaffStoreId(), null, employeePo.getId(),
                Lists.newArrayList(AppointmentStatusEnum.TO_DO.getKey(), AppointmentStatusEnum.IN_PROCESS.getKey()),
                dto.getBeginTime(), dto.getEndTime());
        ensureConditionSatisfied(count == 0, ErrorMessage.appointmentEmployeeTimeIsConflicted);
        dto.setStoreId(ThreadContext.getStaffStoreId());
        dto.setMemberId(memberPo != null ? memberPo.getId() : null);
        dto.setStatus(AppointmentStatusEnum.TO_DO.getKey());
        AppointmentPo po = CommonConverter.map(dto, AppointmentPo.class);
        if (memberPo != null) {
            po.setMemberId(memberPo.getId());
            po.setName(memberPo.getName());
            po.setSex(Integer.valueOf(String.valueOf(memberPo.getSex())));
        }
        return appointmentMapper.insert(po);
    }

    @Transactional(propagation = Propagation.REQUIRED)
    @Override
    public int update(AppointmentDto dto) {
        ProjectPo projectPo = projectMapper.selectById(dto.getProjectId());
        ensureEntityExist(projectPo, ErrorMessage.projectNotFound);
        EmployeePo employeePo = employeeMapper.selectById(dto.getEmployeeId());
        ensureEntityExist(employeePo, ErrorMessage.employeeNotFound);
        AppointmentPo po = appointmentMapper.selectById(dto.getId());
        ensureEntityExist(po, ErrorMessage.appointmentNotFound);
        appointmentMapper.deleteById(dto.getId());
        //判断是否时间上冲突
        int count = appointmentMapper.selectByCondition(ThreadContext.getStaffStoreId(), dto.getPhone(), null,
                Lists.newArrayList(AppointmentStatusEnum.TO_DO.getKey(), AppointmentStatusEnum.IN_PROCESS.getKey()),
                dto.getBeginTime(), dto.getEndTime());
        ensureConditionSatisfied(count == 0, ErrorMessage.appointmentMemberTimeIsConflicted);
        count = appointmentMapper.selectByCondition(ThreadContext.getStaffStoreId(), null, dto.getEmployeeId(),
                Lists.newArrayList(AppointmentStatusEnum.TO_DO.getKey(), AppointmentStatusEnum.IN_PROCESS.getKey()),
                dto.getBeginTime(), dto.getEndTime());
        ensureConditionSatisfied(count == 0, ErrorMessage.appointmentEmployeeTimeIsConflicted);
        po.setPhone(dto.getPhone());
        po.setName(dto.getName());
        po.setSex(dto.getSex());
        po.setProjectId(dto.getProjectId());
        po.setEmployeeId(dto.getEmployeeId());
        po.setBeginTime(dto.getBeginTime());
        po.setEndTime(dto.getEndTime());
        po.setLine(dto.getLine());
        po.setRemark(dto.getRemark());
        MemberQueryCondition memberQueryCondition = new MemberQueryCondition();
        memberQueryCondition.setPhone(dto.getPhone());
        MemberPo memberPo = memberMapper.selectByCondition(memberQueryCondition);
        if (memberPo != null) {
            po.setMemberId(memberPo.getId());
            po.setName(memberPo.getName());
            po.setSex(Integer.valueOf(String.valueOf(memberPo.getSex())));
        }
        return appointmentMapper.insert(po);
    }

    @Override
    public int cancel(Long id) {
        return appointmentMapper.updateForCancel(id, AppointmentStatusEnum.CANCEL.getKey());
    }

    @Override
    public int start(Long id) {
        return appointmentMapper.updateForStart(id, AppointmentStatusEnum.IN_PROCESS.getKey());
    }

    @Transactional(propagation = Propagation.REQUIRED)
    @Synchronized
    @Override
    public int finish(Long id, BigDecimal charge, Integer chargeWay, Long counselorId, String source, String remark) {
        AppointmentPo po = appointmentMapper.selectById(id);
        ensureEntityExist(po, ErrorMessage.appointmentNotFound);
        MemberPo memberPo = null;
        if (po.getMemberId() != null) {
            memberPo = memberMapper.selectById(po.getMemberId());
        } else {
            if (chargeWay == ChargeWayEnum.CARD.getKey()) {
                throw new GmhException(ErrorCodeEnum.BUSINESS_EXCEPTION_INVALID_PARAMETERS, "非会员不能使用会员卡消费");
            }
        }
        ConsumeRecordPo recordPo = new ConsumeRecordPo();
        recordPo.setStoreId(po.getStoreId());
        recordPo.setProjectId(po.getProjectId());
        recordPo.setEmployeeId(po.getEmployeeId());
        if (po.getMemberId() != null) {
            recordPo.setMember(true);
            recordPo.setMemberId(po.getMemberId());
        } else {
            recordPo.setMember(false);
        }
        recordPo.setMobile(po.getPhone());
        recordPo.setSex(po.getSex());
        recordPo.setConsumerName(po.getName());
        recordPo.setCharge(charge);
        recordPo.setChargeWay(chargeWay);
        recordPo.setCounselor(counselorId);
        recordPo.setSource(source);
        recordPo.setConsumeTime(new Date());
        recordPo.setRemark(remark);
        consumeRecordMapper.insert(recordPo);
        if (memberPo != null) {
            // 扣除会员卡储值
            if (chargeWay == ChargeWayEnum.CARD.getKey()) {
                ProjectQueryCondition condition = new ProjectQueryCondition();
                condition.setProjectId(po.getProjectId());
                ProjectUnion union = projectUnionMapper.listProjectUnionsByCondition(condition).get(0);
                ensureEntityExist(union, ErrorMessage.appointmentNotFound);
                BigDecimal nailMoney = memberPo.getNailMoney();
                BigDecimal beautyMoney = memberPo.getBeautyMoney();
                Integer projectType = union.getProjectTypePo().getTopType();
                if (projectType == ProjectTypeEnum.NAIL.getKey() || projectType == ProjectTypeEnum.LIDS.getKey()) {
                    ensureConditionSatisfied(nailMoney.compareTo(charge) != -1, ErrorMessage.nailMoneyNotEnough);
                    //扣除美甲美睫储值
                    memberMapper.updateNailMoney(po.getMemberId(), nailMoney.subtract(charge));
                } else if (projectType == ProjectTypeEnum.BEAUTY.getKey()
                        || projectType == ProjectTypeEnum.PRODUCT.getKey()) {
                    ensureConditionSatisfied(beautyMoney.compareTo(charge) != -1, ErrorMessage.beautyMoneyNotEnough);
                    //扣除美容储值
                    memberMapper.updateBeautyMoney(po.getMemberId(), beautyMoney.subtract(charge));
                } else {
                    throw new GmhException(ErrorCodeEnum.BUSINESS_EXCEPTION_ILLEGAL_STATUS,
                            ErrorMessage.projectTopTypeIsError);
                }
            }
        }
        return appointmentMapper.updateForFinish(id, AppointmentStatusEnum.DONE.getKey());
    }

    private List<AppointmentVo> buildAppointmentVosByUnions(List<AppointmentUnion> unions) {
        List<AppointmentVo> vos = Lists.newArrayList();
        for (AppointmentUnion union : unions) {
            if (union.getAppointmentPo() == null || union.getEmployeePo() == null || union.getProjectPo() == null
                    || union.getProjectTypePo() == null) {
                continue;
            }
            AppointmentVo vo = buildAppointmentVoByUnion(union);
            vos.add(vo);
        }
        return vos;
    }

    private AppointmentVo buildAppointmentVoByUnion(AppointmentUnion union) {
        AppointmentPo appointmentPo = union.getAppointmentPo();
        EmployeePo employeePo = union.getEmployeePo();
        ProjectPo projectPo = union.getProjectPo();
        ProjectTypePo projectTypePo = union.getProjectTypePo();
        AppointmentVo vo = CommonConverter.map(appointmentPo, AppointmentVo.class);
        vo.setStatus(EnumUtils.getDescByKey(AppointmentStatusEnum.class, appointmentPo.getStatus()));
        vo.setMemberName(!Strings.isNullOrEmpty(appointmentPo.getName()) ? appointmentPo.getName() : "");
        vo.setEmployeeName(employeePo.getName());
        vo.setProjectCharge(projectPo.getRetailPrice());
        vo.setProjectName(projectPo.getName());
        vo.setTopType(projectTypePo.getTopType());
        vo.setTopTypeName(EnumUtils.getDescByKey(ProjectTypeEnum.class, projectTypePo.getTopType()));
        vo.setTypeId(projectPo.getProjectTypeId());
        vo.setTypeName(projectTypePo.getTypeName());
        vo.setLine(appointmentPo.getLine().booleanValue() ? YesOrNoEnum.YES.getDesc() : YesOrNoEnum.NO.getDesc());
        return vo;
    }

    @Override
    public PagedList<AppointmentVo> searchPagedAppointments(AppointmentQueryCondition condition) {
        PageHelper.startPage(condition.getPageNum(), condition.getPageSize());
        AppointmentUnionQueryCondition queryCondition = new AppointmentUnionQueryCondition();
        queryCondition.setStoreId(ThreadContext.getStaffStoreId());
        queryCondition.setSearchString(condition.getSearchString());
        queryCondition.setStatus(
                Lists.newArrayList(AppointmentStatusEnum.TO_DO.getKey(), AppointmentStatusEnum.IN_PROCESS.getKey()));
        List<AppointmentUnion> unions = appointmentUnionMapper.listAppointmentUnionsByCondition(queryCondition);
        if (CollectionUtils.isEmpty(unions)) {
            return PagedLists.newPagedList(condition.getPageNum(), condition.getPageSize());
        }
        PageInfo<AppointmentUnion> info = new PageInfo<>(unions);
        List<AppointmentVo> vos = buildAppointmentVosByUnions(unions);
        return PagedLists.newPagedList(info.getPageNum(), info.getPageSize(), info.getTotal(), vos);
    }

    @Override
    public List<AppointmentVo> remind() {
        List<AppointmentUnion> unions = appointmentUnionMapper.selectByTime(DEFAULT_REMIND_MINUTE,
                AppointmentStatusEnum.TO_DO.getKey(), ThreadContext.getStaffStoreId());
        if (CollectionUtils.isEmpty(unions)) {
            return Lists.newArrayList();
        }
        List<AppointmentVo> vos = buildAppointmentVosByUnions(unions);
        return vos;
    }

    @Override
    public List<EmployeeItemVo> listEmployeesByProject(Long projectId) {
        ProjectPo projectPo = projectMapper.selectById(projectId);
        ensureEntityExist(projectPo, ErrorMessage.projectNotFound);
        ProjectTypePo projectTypePo = projectTypeMapper.selectById(projectPo.getProjectTypeId());
        ensureEntityExist(projectTypePo, ErrorMessage.projectTypeNotFound);
        List<EmployeeJobPo> pos = employeeJobMapper
                .selectByJobType(getJobTypeByProjectTopType(projectTypePo.getTopType()));
        List<Long> ids = Lists.newArrayList();
        for (EmployeeJobPo po : pos) {
            ids.add(po.getEmployeeId());
        }
        List<EmployeeItemVo> vos = Lists.newArrayList();
        List<EmployeePo> employeePos = employeeMapper.selectByIds(ids, true, ThreadContext.getStaffStoreId());
        for (EmployeePo po : employeePos) {
            EmployeeItemVo vo = new EmployeeItemVo();
            vo.setEmployeeId(po.getId());
            vo.setEmployeeName(po.getName());
            vos.add(vo);
        }
        return vos;
    }

    private Integer getJobTypeByProjectTopType(Integer topType) {
        if (topType.intValue() == JobEnum.NAIL_TECHNICIAN.getKey()) {
            return ProjectTypeEnum.NAIL.getKey();
        }
        if (topType.intValue() == JobEnum.BEAUTY_STYLIST.getKey()) {
            return ProjectTypeEnum.LIDS.getKey();
        }
        if (topType.intValue() == JobEnum.BEAUTICIAN.getKey()) {
            return ProjectTypeEnum.BEAUTY.getKey();
        }
        return null;
    }

    @Override
    public List<AppointmentVo> listAppointmentsByEmployee(Long employeeId) {
        if (employeeId.longValue() == 0L) {
            employeeId = null;
        }
        if (employeeId != null) {
            EmployeePo po = employeeMapper.selectById(employeeId);
            ensureEntityExist(po, ErrorMessage.employeeNotFound);
        }
        AppointmentUnionQueryCondition condition = new AppointmentUnionQueryCondition();
        condition.setStoreId(ThreadContext.getStaffStoreId());
        condition.setStatus(
                Lists.newArrayList(AppointmentStatusEnum.TO_DO.getKey(), AppointmentStatusEnum.IN_PROCESS.getKey()));
        condition.setEmployeeId(employeeId);
        List<AppointmentUnion> unions = appointmentUnionMapper.listAppointmentUnionsByCondition(condition);
        return buildAppointmentVosByUnions(unions);
    }

    @Override
    public List<TimeVo> queryTime(Date time, Long employeeId) {
        EmployeePo employeePo = employeeMapper.selectById(employeeId);
        LogicHelper.ensureEntityExist(employeePo, ErrorMessage.employeeNotFound);
        List<AppointmentPo> pos = appointmentMapper.selectByEmployeeAndTime(time, employeeId);
        if (CollectionUtils.isEmpty(pos)) {
            TimeVo vo = new TimeVo();
            vo.setTime("8:00-22:00");
            vo.setType("空闲");
            vo.setPercent("100%");
            return Lists.newArrayList(vo);
        }
        List<AppointmentPo> appointmentPos = Lists.newArrayList();
        AppointmentPo firstAppointmentPo = new AppointmentPo();
        firstAppointmentPo.setBeginTime(getDefaultBeginTime(time));
        firstAppointmentPo.setEndTime(pos.get(0).getBeginTime());
        appointmentPos.add(firstAppointmentPo);
        for (int i = 0; i < pos.size(); i++) {
            AppointmentPo busyAppointmentPo = new AppointmentPo();
            busyAppointmentPo.setBeginTime(pos.get(i).getBeginTime());
            busyAppointmentPo.setEndTime(pos.get(i).getEndTime());
            appointmentPos.add(busyAppointmentPo);
            if (i < pos.size() - 1) {
                AppointmentPo freeAppointmentPo = new AppointmentPo();
                freeAppointmentPo.setBeginTime(pos.get(i).getEndTime());
                freeAppointmentPo.setEndTime(pos.get(i + 1).getBeginTime());
                appointmentPos.add(freeAppointmentPo);
            } else {
                AppointmentPo freeAppointmentPo = new AppointmentPo();
                freeAppointmentPo.setBeginTime(pos.get(i).getEndTime());
                freeAppointmentPo.setEndTime(getDefaultEndTime(time));
                appointmentPos.add(freeAppointmentPo);
            }
        }
        List<TimeVo> vos = Lists.newArrayList();
        for (int i = 0; i < appointmentPos.size(); i++) {
            TimeVo vo = new TimeVo();
            if (i % 2 == 0) {
                vo.setType("空闲");
            } else {
                vo.setType("占用");
            }
            SimpleDateFormat sdf = new SimpleDateFormat("mm:ss");
            vo.setTime(sdf.format(appointmentPos.get(i).getBeginTime()) + "-"
                    + sdf.format(appointmentPos.get(i).getEndTime()));

            vo.setPercent(
                    getMinutesBetweenDates(appointmentPos.get(i).getBeginTime(), appointmentPos.get(i).getEndTime())
                            / TOTAL_MINUTE * 100 + "%");
            vos.add(vo);
        }
        List<TimeVo> timeVos = Lists.newArrayList();
        for (TimeVo vo : vos) {
            if (!"0%".equals(vo.getPercent())) {
                TimeVo timeVo = CommonConverter.map(vo, TimeVo.class);
                timeVos.add(timeVo);
            }
        }
        return vos;
    }

    private int getMinutesBetweenDates(Date startDate, Date endDate) {
        DateTime startDateTime = new DateTime(startDate.getTime());
        DateTime endDateTime = new DateTime(endDate.getTime());
        return Minutes.minutesBetween(startDateTime, endDateTime).getMinutes();
    }

    private Date getDefaultBeginTime(Date date) {
        DateTime dateTime = new DateTime(date.getTime());
        DateTime defaultDateTime = new DateTime(dateTime.getYear(), dateTime.getMonthOfYear(), dateTime.getDayOfMonth(),
                DEFAULT_START_HOUR, 0);
        return defaultDateTime.toDate();
    }

    private Date getDefaultEndTime(Date date) {
        DateTime dateTime = new DateTime(date.getTime());
        DateTime defaultDateTime = new DateTime(dateTime.getYear(), dateTime.getMonthOfYear(), dateTime.getDayOfMonth(),
                DEFAULT_END_HOUR, 0);
        return defaultDateTime.toDate();
    }

}
