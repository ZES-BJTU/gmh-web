package com.zes.squad.gmh.web.service.impl;

import static com.zes.squad.gmh.web.helper.LogicHelper.ensureConditionSatisfied;
import static com.zes.squad.gmh.web.helper.LogicHelper.ensureEntityExist;

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
import com.zes.squad.gmh.common.enums.AppointmentStatusEnum;
import com.zes.squad.gmh.common.enums.ChargeWayEnum;
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
import com.zes.squad.gmh.web.entity.po.EmployeePo;
import com.zes.squad.gmh.web.entity.po.MemberPo;
import com.zes.squad.gmh.web.entity.po.ProjectPo;
import com.zes.squad.gmh.web.entity.po.ProjectTypePo;
import com.zes.squad.gmh.web.entity.union.AppointmentUnion;
import com.zes.squad.gmh.web.entity.union.ProjectUnion;
import com.zes.squad.gmh.web.entity.vo.AppointmentVo;
import com.zes.squad.gmh.web.helper.LogicHelper;
import com.zes.squad.gmh.web.mapper.AppointmentMapper;
import com.zes.squad.gmh.web.mapper.AppointmentUnionMapper;
import com.zes.squad.gmh.web.mapper.ConsumeRecordMapper;
import com.zes.squad.gmh.web.mapper.EmployeeMapper;
import com.zes.squad.gmh.web.mapper.MemberMapper;
import com.zes.squad.gmh.web.mapper.ProjectMapper;
import com.zes.squad.gmh.web.mapper.ProjectUnionMapper;
import com.zes.squad.gmh.web.service.AppointmentService;

import lombok.Synchronized;

@Service("appointmentJobService")
public class AppointmentServiceImpl implements AppointmentService {

    @Autowired
    private AppointmentMapper      appointmentMapper;
    @Autowired
    private AppointmentUnionMapper appointmentUnionMapper;
    @Autowired
    private ConsumeRecordMapper    consumeRecordMapper;
    @Autowired
    private EmployeeMapper         employeeMapper;
    @Autowired
    private ProjectUnionMapper     projectUnionMapper;
    @Autowired
    private ProjectMapper          projectMapper;
    @Autowired
    private MemberMapper           memberMapper;

    @Override
    public List<AppointmentVo> listAllAppointments() {
        AppointmentUnionQueryCondition condition = new AppointmentUnionQueryCondition();
        condition.setStoreId(ThreadContext.getStaffStoreId());
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
        LogicHelper.ensureParameterExist(id, ErrorMessage.appointmentIdIsNull);
        AppointmentUnion union = appointmentUnionMapper.selectById(id);
        LogicHelper.ensureEntityExist(union, ErrorMessage.appointmentIsNull);
        return union;
    }

    @Override
    public int insert(AppointmentDto dto) {
        MemberQueryCondition memberQueryCondition = new MemberQueryCondition();
        memberQueryCondition.setPhone(dto.getPhone());
        MemberPo memberPo = memberMapper.selectByCondition(memberQueryCondition);
        LogicHelper.ensureEntityExist(memberPo, ErrorMessage.memberNotFound);
        ProjectPo projectPo = projectMapper.selectById(dto.getProjectId());
        LogicHelper.ensureEntityExist(projectPo, ErrorMessage.projectNotFound);
        EmployeePo employeePo = employeeMapper.selectById(dto.getEmployeeId());
        LogicHelper.ensureEntityExist(employeePo, ErrorMessage.employeeNotFound);
        int count = appointmentMapper.selectByCondition(ThreadContext.getStaffStoreId(), memberPo.getId(), null,
                AppointmentStatusEnum.TO_DO.getKey(), dto.getBeginTime(), dto.getEndTime());
        LogicHelper.ensureConditionSatisfied(count == 0, ErrorMessage.appointmentMemberTimeIsConflicted);
        count = appointmentMapper.selectByCondition(ThreadContext.getStaffStoreId(), null, employeePo.getId(),
                AppointmentStatusEnum.TO_DO.getKey(), dto.getBeginTime(), dto.getEndTime());
        LogicHelper.ensureConditionSatisfied(count == 0, ErrorMessage.appointmentEmployeeTimeIsConflicted);
        dto.setStoreId(ThreadContext.getStaffStoreId());
        dto.setMemberId(memberPo.getId());
        dto.setStatus(AppointmentStatusEnum.TO_DO.getKey());
        AppointmentPo po = CommonConverter.map(dto, AppointmentPo.class);
        return appointmentMapper.insert(po);
    }

    @Transactional(propagation = Propagation.REQUIRED)
    @Override
    public int update(AppointmentDto dto) {
        ProjectPo projectPo = projectMapper.selectById(dto.getProjectId());
        LogicHelper.ensureEntityExist(projectPo, ErrorMessage.projectNotFound);
        EmployeePo employeePo = employeeMapper.selectById(dto.getEmployeeId());
        LogicHelper.ensureEntityExist(employeePo, ErrorMessage.employeeNotFound);
        AppointmentPo po = appointmentMapper.selectById(dto.getId());
        appointmentMapper.deleteById(dto.getId());
        //判断是否时间上冲突
        int count = appointmentMapper.selectByCondition(ThreadContext.getStaffStoreId(), po.getMemberId(), null,
                AppointmentStatusEnum.TO_DO.getKey(), dto.getBeginTime(), dto.getEndTime());
        LogicHelper.ensureConditionSatisfied(count == 0, ErrorMessage.appointmentMemberTimeIsConflicted);
        count = appointmentMapper.selectByCondition(ThreadContext.getStaffStoreId(), null, dto.getEmployeeId(),
                AppointmentStatusEnum.TO_DO.getKey(), dto.getBeginTime(), dto.getEndTime());
        LogicHelper.ensureConditionSatisfied(count == 0, ErrorMessage.appointmentEmployeeTimeIsConflicted);
        po.setProjectId(dto.getProjectId());
        po.setEmployeeId(dto.getEmployeeId());
        po.setBeginTime(dto.getBeginTime());
        po.setEndTime(dto.getEndTime());
        return appointmentMapper.updateSelective(po);
    }

    @Override
    public int cancel(Long id) {
        return appointmentMapper.updateForCancel(id, AppointmentStatusEnum.CANCEL.getKey());
    }

    @Transactional(propagation = Propagation.REQUIRED)
    @Synchronized
    @Override
    public int finish(Long id, BigDecimal charge, Integer chargeWay) {
        AppointmentPo po = appointmentMapper.selectById(id);
        ensureEntityExist(po, ErrorMessage.appointmentNotFound);
        ConsumeRecordPo recordPo = new ConsumeRecordPo();
        recordPo.setStoreId(po.getStoreId());
        recordPo.setProjectId(po.getProjectId());
        recordPo.setEmployeeId(po.getEmployeeId());
        recordPo.setMemberId(po.getEmployeeId());
        EmployeePo employeePo = employeeMapper.selectById(po.getEmployeeId());
        ensureEntityExist(employeePo, ErrorMessage.employeeNotFound);
        recordPo.setConsumerName(employeePo.getName());
        recordPo.setCharge(charge);
        recordPo.setChargeWay(chargeWay);
        recordPo.setConsumeTime(new Date());
        consumeRecordMapper.insert(recordPo);
        // 扣除会员卡储值
        if (chargeWay == ChargeWayEnum.CARD.getKey()) {
            ProjectQueryCondition condition = new ProjectQueryCondition();
            condition.setProjectId(po.getProjectId());
            ProjectUnion union = projectUnionMapper.listProjectUnionsByCondition(condition).get(0);
            ensureEntityExist(union, ErrorMessage.appointmentNotFound);
            MemberPo memberPo = memberMapper.selectById(po.getMemberId());
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
        int result = appointmentMapper.updateForFinish(id, AppointmentStatusEnum.DONE.getKey());
        return result;
    }

    private List<AppointmentVo> buildAppointmentVosByUnions(List<AppointmentUnion> unions) {
        List<AppointmentVo> vos = Lists.newArrayList();
        for (AppointmentUnion union : unions) {
            AppointmentVo vo = buildAppointmentVoByUnion(union);
            vos.add(vo);
        }
        return vos;
    }

    private AppointmentVo buildAppointmentVoByUnion(AppointmentUnion union) {
        AppointmentPo appointmentPo = union.getAppointmentPo();
        EmployeePo employeePo = union.getEmployeePo();
        MemberPo memberPo = union.getMemberPo();
        ProjectPo projectPo = union.getProjectPo();
        ProjectTypePo projectTypePo = union.getProjectTypePo();
        AppointmentVo vo = CommonConverter.map(appointmentPo, AppointmentVo.class);
        vo.setStatus(EnumUtils.getDescByKey(AppointmentStatusEnum.class, appointmentPo.getStatus()));
        vo.setMemberName(memberPo.getName());
        vo.setEmployeeName(employeePo.getName());
        vo.setProjectName(projectPo.getName());
        vo.setTopTypeName(EnumUtils.getDescByKey(ProjectTypeEnum.class, projectTypePo.getTopType()));
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
        List<AppointmentUnion> unions = appointmentUnionMapper.listAppointmentUnionsByCondition(queryCondition);
        if (CollectionUtils.isEmpty(unions)) {
            return PagedLists.newPagedList(condition.getPageNum(), condition.getPageSize());
        }
        PageInfo<AppointmentUnion> info = new PageInfo<>(unions);
        List<AppointmentVo> vos = buildAppointmentVosByUnions(unions);
        return PagedLists.newPagedList(info.getPageNum(), info.getPageSize(), info.getTotal(), vos);
    }

}
