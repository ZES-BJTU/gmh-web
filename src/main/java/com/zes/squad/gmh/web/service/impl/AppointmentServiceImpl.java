package com.zes.squad.gmh.web.service.impl;

import static com.zes.squad.gmh.web.helper.LogicHelper.ensureConditionSatisfied;
import static com.zes.squad.gmh.web.helper.LogicHelper.ensureEntityExist;
import static com.zes.squad.gmh.web.helper.LogicHelper.ensureParameterExist;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Objects;

import org.apache.commons.collections4.CollectionUtils;
import org.joda.time.DateTime;
import org.joda.time.Minutes;
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
import com.zes.squad.gmh.common.enums.AppointmentStatusEnum;
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
import com.zes.squad.gmh.web.entity.condition.AppointmentQueryCondition;
import com.zes.squad.gmh.web.entity.condition.AppointmentUnionQueryCondition;
import com.zes.squad.gmh.web.entity.condition.MemberQueryCondition;
import com.zes.squad.gmh.web.entity.condition.ProjectQueryCondition;
import com.zes.squad.gmh.web.entity.dto.AppointmentDto;
import com.zes.squad.gmh.web.entity.dto.AppointmentProjectDto;
import com.zes.squad.gmh.web.entity.po.AppointmentPo;
import com.zes.squad.gmh.web.entity.po.AppointmentProjectPo;
import com.zes.squad.gmh.web.entity.po.ConsumeRecordPo;
import com.zes.squad.gmh.web.entity.po.ConsumeRecordProjectPo;
import com.zes.squad.gmh.web.entity.po.EmployeeJobPo;
import com.zes.squad.gmh.web.entity.po.EmployeePo;
import com.zes.squad.gmh.web.entity.po.MemberPo;
import com.zes.squad.gmh.web.entity.po.ProjectPo;
import com.zes.squad.gmh.web.entity.po.ProjectTypePo;
import com.zes.squad.gmh.web.entity.union.AppointmentProjectUnion;
import com.zes.squad.gmh.web.entity.union.AppointmentTimeUnion;
import com.zes.squad.gmh.web.entity.union.AppointmentUnion;
import com.zes.squad.gmh.web.entity.union.ProjectUnion;
import com.zes.squad.gmh.web.entity.vo.AppointmentVo;
import com.zes.squad.gmh.web.entity.vo.EmployeeItemVo;
import com.zes.squad.gmh.web.entity.vo.TimeVo;
import com.zes.squad.gmh.web.helper.LogicHelper;
import com.zes.squad.gmh.web.mapper.AppointmentMapper;
import com.zes.squad.gmh.web.mapper.AppointmentProjectMapper;
import com.zes.squad.gmh.web.mapper.AppointmentTimeUnionMapper;
import com.zes.squad.gmh.web.mapper.AppointmentUnionMapper;
import com.zes.squad.gmh.web.mapper.ConsumeRecordMapper;
import com.zes.squad.gmh.web.mapper.ConsumeRecordProjectMapper;
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

    private static final int           DEFAULT_REMIND_MINUTE = 30;

    private static final int           DEFAULT_START_HOUR    = 8;
    private static final int           DEFAULT_END_HOUR      = 22;

    private static final int           TOTAL_MINUTE          = (DEFAULT_END_HOUR - DEFAULT_START_HOUR) * 60;

    @Autowired
    private AppointmentMapper          appointmentMapper;
    @Autowired
    private AppointmentProjectMapper   appointmentProjectMapper;
    @Autowired
    private AppointmentUnionMapper     appointmentUnionMapper;
    @Autowired
    private ConsumeRecordMapper        consumeRecordMapper;
    @Autowired
    private ConsumeRecordProjectMapper consumeRecordProjectMapper;
    @Autowired
    private EmployeeMapper             employeeMapper;
    @Autowired
    private EmployeeJobMapper          employeeJobMapper;
    @Autowired
    private ProjectUnionMapper         projectUnionMapper;
    @Autowired
    private ProjectMapper              projectMapper;
    @Autowired
    private MemberMapper               memberMapper;
    @Autowired
    private ProjectTypeMapper          projectTypeMapper;
    @Autowired
    private AppointmentTimeUnionMapper appointmentTimeUnionMapper;

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

    @Transactional(propagation = Propagation.REQUIRED)
    @Override
    public int insert(AppointmentDto dto) {
        MemberQueryCondition memberQueryCondition = new MemberQueryCondition();
        memberQueryCondition.setPhone(dto.getPhone());
        for (AppointmentProjectDto projectDto : dto.getAppointmentProjectDtos()) {
            ProjectPo projectPo = projectMapper.selectById(projectDto.getProjectId());
            ensureEntityExist(projectPo, ErrorMessage.projectNotFound);
            EmployeePo employeePo = employeeMapper.selectById(projectDto.getEmployeeId());
            ensureEntityExist(employeePo, ErrorMessage.employeeNotFound);
            int count = appointmentMapper.selectByCondition(ThreadContext.getStaffStoreId(), null, employeePo.getId(),
                    Lists.newArrayList(AppointmentStatusEnum.TO_DO.getKey(), AppointmentStatusEnum.IN_PROCESS.getKey()),
                    projectDto.getBeginTime(), projectDto.getEndTime());
            ensureConditionSatisfied(count == 0, ErrorMessage.appointmentEmployeeTimeIsConflicted);
        }
        List<MemberPo> memberPos = memberMapper.selectByCondition(memberQueryCondition);
        dto.setStoreId(ThreadContext.getStaffStoreId());
        dto.setStatus(AppointmentStatusEnum.TO_DO.getKey());
        AppointmentPo po = CommonConverter.map(dto, AppointmentPo.class);
        if (CollectionUtils.isNotEmpty(memberPos)) {
            po.setName(memberPos.get(0).getName());
            po.setSex(Integer.valueOf(String.valueOf(memberPos.get(0).getSex())));
        }
        int result = appointmentMapper.insert(po);
        ensureConditionSatisfied(po.getId() != null, "预约标识绑定失败");
        List<AppointmentProjectDto> dtos = dto.getAppointmentProjectDtos();
        List<AppointmentProjectPo> pos = CommonConverter.mapList(dtos, AppointmentProjectPo.class);
        for (AppointmentProjectPo projectPo : pos) {
            projectPo.setAppointmentId(po.getId());
        }
        appointmentProjectMapper.batchInsert(pos);
        return result;
    }

    @Transactional(propagation = Propagation.REQUIRED)
    @Override
    public int update(AppointmentDto dto) {
        AppointmentPo po = appointmentMapper.selectById(dto.getId());
        ensureEntityExist(po, ErrorMessage.appointmentNotFound);
        appointmentProjectMapper.deleteByAppointmentId(dto.getId());
        for (AppointmentProjectDto projectDto : dto.getAppointmentProjectDtos()) {
            ProjectPo projectPo = projectMapper.selectById(projectDto.getProjectId());
            ensureEntityExist(projectPo, ErrorMessage.projectNotFound);
            EmployeePo employeePo = employeeMapper.selectById(projectDto.getEmployeeId());
            ensureEntityExist(employeePo, ErrorMessage.employeeNotFound);
            //判断是否时间上冲突
            int count = appointmentMapper.selectByCondition(ThreadContext.getStaffStoreId(), null,
                    projectDto.getEmployeeId(),
                    Lists.newArrayList(AppointmentStatusEnum.TO_DO.getKey(), AppointmentStatusEnum.IN_PROCESS.getKey()),
                    projectDto.getBeginTime(), projectDto.getEndTime());
            ensureConditionSatisfied(count == 0, ErrorMessage.appointmentEmployeeTimeIsConflicted);
        }
        appointmentMapper.deleteById(dto.getId());
        po.setPhone(dto.getPhone());
        po.setName(dto.getName());
        po.setSex(dto.getSex());
        po.setLine(dto.getLine());
        po.setRemark(dto.getRemark());
        MemberQueryCondition memberQueryCondition = new MemberQueryCondition();
        memberQueryCondition.setPhone(dto.getPhone());
        List<MemberPo> memberPos = memberMapper.selectByCondition(memberQueryCondition);
        if (CollectionUtils.isNotEmpty(memberPos)) {
            po.setName(memberPos.get(0).getName());
            po.setSex(Integer.valueOf(String.valueOf(memberPos.get(0).getSex())));
        }
        int result = appointmentMapper.insert(po);
        List<AppointmentProjectDto> dtos = dto.getAppointmentProjectDtos();
        List<AppointmentProjectPo> pos = CommonConverter.mapList(dtos, AppointmentProjectPo.class);
        for (AppointmentProjectPo projectPo : pos) {
            projectPo.setAppointmentId(po.getId());
        }
        appointmentProjectMapper.batchInsert(pos);
        return result;
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
    //projects数据projectId,charge,conselorId
    public int finish(Long id, Integer chargeWay, Long chargeCard, BigDecimal totalCharge, String projects,
                      String source, String remark) {
        AppointmentPo po = appointmentMapper.selectById(id);
        ensureEntityExist(po, ErrorMessage.appointmentNotFound);
        String[] consumeProjects = projects.split(";");
        List<ConsumeRecordProjectPo> projectPos = Lists.newArrayList();
        for (String project : consumeProjects) {
            String[] detail = project.split(",");
            ConsumeRecordProjectPo projectPo = new ConsumeRecordProjectPo();
            projectPo.setProjectId(Long.valueOf(detail[0]));
            projectPo.setCharge(new BigDecimal(detail[1]));
            if (detail.length > 2 && detail[2] != null && !Objects.equals(detail[2], "null")) {
                projectPo.setCounselorId(detail[2].equals("0") ? null : Long.valueOf(detail[2]));
            }
            projectPos.add(projectPo);
        }
        ConsumeRecordPo recordPo = new ConsumeRecordPo();
        recordPo.setCharge(BigDecimal.ZERO);
        for (ConsumeRecordProjectPo consumeRecordProjectPo : projectPos) {
            recordPo.setCharge(recordPo.getCharge().add(consumeRecordProjectPo.getCharge()));
        }
        MemberPo memberPo = memberMapper.selectById(chargeCard);
        if (memberPo == null) {
            if (chargeWay == ChargeWayEnum.CARD.getKey()) {
                throw new GmhException(ErrorCodeEnum.BUSINESS_EXCEPTION_INVALID_PARAMETERS, "非会员不能使用会员卡消费");
            }
            recordPo.setMember(false);
        } else {
            recordPo.setMember(true);
            recordPo.setMemberId(chargeCard);
        }
        recordPo.setStoreId(po.getStoreId());
        recordPo.setMobile(po.getPhone());
        recordPo.setSex(po.getSex());
        recordPo.setConsumerName(po.getName());
        recordPo.setCharge(totalCharge);
        recordPo.setChargeWay(chargeWay);
        recordPo.setSource(source);
        recordPo.setConsumeTime(new Date());
        recordPo.setRemark(remark);
        consumeRecordMapper.insert(recordPo);
        List<AppointmentProjectPo> appointmentProjectPos = appointmentProjectMapper.selectByAppointmentId(id);
        ensureConditionSatisfied(CollectionUtils.isNotEmpty(appointmentProjectPos), "根据预约标识查询预约项目失败");
        for (ConsumeRecordProjectPo projectPo : projectPos) {
            projectPo.setConsumeRecordId(recordPo.getId());
            for (AppointmentProjectPo appointmentProjectPo : appointmentProjectPos) {
                if (appointmentProjectPo.getProjectId().equals(projectPo.getProjectId())) {
                    projectPo.setEmployeeId(appointmentProjectPo.getEmployeeId());
                }
            }
        }
        consumeRecordProjectMapper.batchInsert(projectPos);
        if (memberPo != null) {
            // 扣除会员卡储值
            if (chargeWay == ChargeWayEnum.CARD.getKey()) {
                for (ConsumeRecordProjectPo projectPo : projectPos) {

                    ProjectQueryCondition projectQueryCondition = new ProjectQueryCondition();
                    projectQueryCondition.setProjectId(projectPo.getProjectId());
                    ProjectUnion union = projectUnionMapper.listProjectUnionsByCondition(projectQueryCondition).get(0);
                    ensureEntityExist(union, ErrorMessage.appointmentNotFound);
                    BigDecimal nailMoney = memberPo.getNailMoney();
                    BigDecimal beautyMoney = memberPo.getBeautyMoney();
                    Integer projectType = union.getProjectTypePo().getTopType();
                    if (projectType == ProjectTypeEnum.NAIL.getKey() || projectType == ProjectTypeEnum.LIDS.getKey()) {
                        nailMoney = nailMoney.subtract(projectPo.getCharge());
                        ensureConditionSatisfied(nailMoney.compareTo(BigDecimal.ZERO) != -1,
                                ErrorMessage.nailMoneyNotEnough);
                        //扣除美甲美睫储值
                        memberMapper.updateNailMoney(po.getMemberId(), nailMoney);
                    } else if (projectType == ProjectTypeEnum.BEAUTY.getKey()
                            || projectType == ProjectTypeEnum.PRODUCT.getKey()) {
                        beautyMoney = beautyMoney.subtract(projectPo.getCharge());
                        ensureConditionSatisfied(beautyMoney.compareTo(BigDecimal.ZERO) != -1,
                                ErrorMessage.beautyMoneyNotEnough);
                        //扣除美容储值
                        memberMapper.updateBeautyMoney(po.getMemberId(), beautyMoney);
                    } else {
                        throw new GmhException(ErrorCodeEnum.BUSINESS_EXCEPTION_ILLEGAL_STATUS,
                                ErrorMessage.projectTopTypeIsError);
                    }

                }
            }
        }
        return appointmentMapper.updateForFinish(id, AppointmentStatusEnum.DONE.getKey());
    }

    private List<AppointmentVo> buildAppointmentVosByUnions(List<AppointmentUnion> unions) {
        List<AppointmentVo> vos = Lists.newArrayList();
        for (AppointmentUnion union : unions) {
            if (union.getAppointmentPo() == null || union.getAppointmentProjectUnions() == null
                    || union.getAppointmentProjectUnions().isEmpty()) {
                continue;
            }
            AppointmentVo vo = buildAppointmentVoByUnion(union);
            vos.add(vo);
        }
        return vos;
    }

    private AppointmentVo buildAppointmentVoByUnion(AppointmentUnion union) {
        AppointmentPo appointmentPo = union.getAppointmentPo();
        List<AppointmentProjectUnion> unions = union.getAppointmentProjectUnions();
        ensureConditionSatisfied(unions != null && !unions.isEmpty(), "预约项目查询失败");
        AppointmentVo vo = CommonConverter.map(appointmentPo, AppointmentVo.class);
        vo.setStatus(EnumUtils.getDescByKey(AppointmentStatusEnum.class, appointmentPo.getStatus()));
        //        List<MemberPo> memberPos = null;
        //        if (union.getMemberPo() == null) {
        //            MemberQueryCondition condition = new MemberQueryCondition();
        //            condition.setPhone(appointmentPo.getPhone());
        //            condition.setStoreId(ThreadContext.getStaffStoreId());
        //            memberPos = memberMapper.selectByCondition(condition);
        //            if (CollectionUtils.isEmpty(memberPos)) {
        //                vo.setMemberName("");
        //            } else {
        //                vo.setMemberName(memberPos.get(0).getName());
        //            }
        //        } else {
        //            vo.setMemberName(
        //                    !Strings.isNullOrEmpty(union.getMemberPo().getName()) ? union.getMemberPo().getName() : "");
        //        }
        vo.setMemberName(appointmentPo.getName());
        Integer[] topTypes = new Integer[unions.size()];
        String[] topTypeNames = new String[unions.size()];
        Long[] typeIds = new Long[unions.size()];
        String[] typeNames = new String[unions.size()];
        Long[] projectIds = new Long[unions.size()];
        String[] projectNames = new String[unions.size()];
        BigDecimal[] projectCharges = new BigDecimal[unions.size()];
        Long[] employeeIds = new Long[unions.size()];
        String[] employeeNames = new String[unions.size()];
        Date[] beginTimes = new Date[unions.size()];
        Date[] endTimes = new Date[unions.size()];
        for (int i = 0; i < unions.size(); i++) {
            topTypes[i] = unions.get(i).getProjectTypePo().getTopType();
            topTypeNames[i] = EnumUtils.getDescByKey(ProjectTypeEnum.class,
                    Integer.valueOf(String.valueOf(unions.get(i).getProjectTypePo().getTopType())));
            typeIds[i] = unions.get(i).getProjectTypePo().getId();
            typeNames[i] = unions.get(i).getProjectTypePo().getTypeName();
            projectIds[i] = unions.get(i).getProjectPo().getId();
            projectNames[i] = unions.get(i).getProjectPo().getName();
            projectCharges[i] = unions.get(i).getProjectPo().getRetailPrice();
            employeeIds[i] = unions.get(i).getEmployeePo().getId();
            employeeNames[i] = unions.get(i).getEmployeePo().getName();
            beginTimes[i] = unions.get(i).getAppointmentProjectPo().getBeginTime();
            endTimes[i] = unions.get(i).getAppointmentProjectPo().getEndTime();
        }
        vo.setTopTypes(topTypes);
        vo.setTopTypeNames(topTypeNames);
        vo.setTypeIds(typeIds);
        vo.setTypeNames(typeNames);
        vo.setProjectIds(projectIds);
        vo.setProjectNames(projectNames);
        vo.setProjectCharges(projectCharges);
        vo.setEmployeeIds(employeeIds);
        vo.setEmployeeNames(employeeNames);
        vo.setBeginTimes(beginTimes);
        vo.setEndTimes(endTimes);
        vo.setLine(appointmentPo.getLine().booleanValue() ? YesOrNoEnum.YES.getDesc() : YesOrNoEnum.NO.getDesc());
        if (union.getAppointmentPo().getSex() != null) {
            vo.setSex(EnumUtils.getDescByKey(SexEnum.class, union.getAppointmentPo().getSex()));
        }
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
        return PagedLists.newPagedList(info.getPageNum(), info.getPageSize(), unions.size(), vos);
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
        List<AppointmentVo> vos = buildAppointmentVosByUnions(unions);
        return vos;
    }

    @Override
    public List<TimeVo> queryTime(Date time, Long employeeId) {
        EmployeePo employeePo = employeeMapper.selectById(employeeId);
        LogicHelper.ensureEntityExist(employeePo, ErrorMessage.employeeNotFound);

        List<AppointmentTimeUnion> unions = appointmentTimeUnionMapper.selectByEmployeeAndTime(time, employeeId,
                getDefaultBeginTime(time), getDefaultEndTime(time));
        List<AppointmentPo> pos = Lists.newArrayList();
        for (AppointmentTimeUnion union : unions) {
            for (AppointmentProjectPo projectPo : union.getAppointmentProjectPos()) {
                AppointmentPo appointmentPo = CommonConverter.map(union, AppointmentPo.class);
                appointmentPo.setProjectId(projectPo.getProjectId());
                appointmentPo.setEmployeeId(projectPo.getEmployeeId());
                appointmentPo.setBeginTime(projectPo.getBeginTime());
                appointmentPo.setEndTime(projectPo.getEndTime());
                pos.add(appointmentPo);
            }
        }

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
            SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
            AppointmentPo appointmentPo = appointmentPos.get(i);
            String beginTime = sdf.format(appointmentPo.getBeginTime());
            String endTime = sdf.format(appointmentPo.getEndTime());
            vo.setTime(beginTime + "-" + endTime);
            int minute = getMinutesBetweenDates(appointmentPos.get(i).getBeginTime(),
                    appointmentPos.get(i).getEndTime());
            double persent = ((double) minute) / TOTAL_MINUTE;
            DecimalFormat decimalFormat = new DecimalFormat("##.00%");
            vo.setPercent(decimalFormat.format(persent));
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

    private static Date getDefaultBeginTime(Date date) {
        DateTime dateTime = new DateTime(date.getTime());
        DateTime defaultDateTime = new DateTime(dateTime.getYear(), dateTime.getMonthOfYear(), dateTime.getDayOfMonth(),
                DEFAULT_START_HOUR, 0);
        return defaultDateTime.toDate();
    }

    private static Date getDefaultEndTime(Date date) {
        DateTime dateTime = new DateTime(date.getTime());
        DateTime defaultDateTime = new DateTime(dateTime.getYear(), dateTime.getMonthOfYear(), dateTime.getDayOfMonth(),
                DEFAULT_END_HOUR, 0);
        return defaultDateTime.toDate();
    }

}
