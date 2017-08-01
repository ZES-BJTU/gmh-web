package com.zes.squad.gmh.web.service.impl;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.google.common.collect.Lists;
import com.zes.squad.gmh.common.converter.CommonConverter;
import com.zes.squad.gmh.common.enums.AppointmentStatusEnum;
import com.zes.squad.gmh.common.enums.ChargeWayEnum;
import com.zes.squad.gmh.common.enums.ProjectTypeEnum;
import com.zes.squad.gmh.common.enums.YesOrNoEnum;
import com.zes.squad.gmh.common.exception.ErrorCodeEnum;
import com.zes.squad.gmh.common.exception.GmhException;
import com.zes.squad.gmh.web.context.ThreadContext;
import com.zes.squad.gmh.web.entity.dto.AppointmentDto;
import com.zes.squad.gmh.web.entity.po.AppointmentPo;
import com.zes.squad.gmh.web.entity.po.ConsumeRecordPo;
import com.zes.squad.gmh.web.entity.po.EmployeePo;
import com.zes.squad.gmh.web.entity.po.MemberPo;
import com.zes.squad.gmh.web.entity.po.ProjectPo;
import com.zes.squad.gmh.web.entity.union.AppointmentUnion;
import com.zes.squad.gmh.web.entity.union.ProjectUnion;
import com.zes.squad.gmh.web.entity.vo.AppointmentVo;
import com.zes.squad.gmh.web.mapper.AppointmentMapper;
import com.zes.squad.gmh.web.mapper.AppointmentUnionMapper;
import com.zes.squad.gmh.web.mapper.ConsumeRecordMapper;
import com.zes.squad.gmh.web.mapper.EmployeeMapper;
import com.zes.squad.gmh.web.mapper.MemberMapper;
import com.zes.squad.gmh.web.mapper.ProjectUnionMapper;
import com.zes.squad.gmh.web.service.AppointmentService;

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
    private MemberMapper           memberMapper;

    @Override
    public List<AppointmentVo> getAll() {
        List<AppointmentUnion> unions = appointmentUnionMapper
                .listAppointmentUnionsByCondition(ThreadContext.getStaffStoreId(), null);
        List<AppointmentVo> vos = buildAppointmentVosByUnions(unions);
        return vos;
    }

    @Override
    public AppointmentVo getByPhone(String phone) {
        List<AppointmentUnion> unions = appointmentUnionMapper.listAppointmentUnionsByCondition(null, phone);
        List<AppointmentVo> vos = buildAppointmentVosByUnions(unions);
        return vos.get(0);
    }

    @Override
    public int insert(AppointmentDto dto) {
        AppointmentPo po = CommonConverter.map(dto, AppointmentPo.class);
        return appointmentMapper.insert(po);
    }

    @Override
    public int update(AppointmentDto dto) {
        AppointmentPo po = CommonConverter.map(dto, AppointmentPo.class);
        return appointmentMapper.update(po);
    }

    @Override
    public int cancel(Long id) {
        return appointmentMapper.cancel(id, AppointmentStatusEnum.CANCEL.getKey());
    }

    @Transactional(propagation = Propagation.REQUIRED)
    @Override
    public int finish(Long id, BigDecimal charge, Integer chargeWay) {
        AppointmentPo po = appointmentMapper.selectById(id);
        if (po == null) {
            throw new GmhException(ErrorCodeEnum.BUSINESS_EXCEPTION_ENTITY_NOT_FOUND, "未找到预约记录");
        }
        ConsumeRecordPo recordPo = new ConsumeRecordPo();
        recordPo.setStoreId(po.getStoreId());
        recordPo.setProjectId(po.getProjectId());
        recordPo.setEmployeeId(po.getEmployeeId());
        recordPo.setMemberId(po.getEmployeeId());
        EmployeePo employeePo = employeeMapper.selectById(po.getStoreId(), po.getEmployeeId());
        if (employeePo == null) {
            throw new GmhException(ErrorCodeEnum.BUSINESS_EXCEPTION_ENTITY_NOT_FOUND.getCode(), "未找到美容师信息");
        }
        recordPo.setConsumerName(employeePo.getName());
        recordPo.setCharge(charge);
        recordPo.setChargeWay(chargeWay);
        recordPo.setConsumeTime(new Date());
        consumeRecordMapper.insert(recordPo);
        // 扣除会员卡储值
        if (chargeWay == ChargeWayEnum.CARD.getKey()) {
            ProjectUnion union = projectUnionMapper.listProjectUnions(po.getProjectId()).get(0);
            if (union == null) {
                throw new GmhException(ErrorCodeEnum.BUSINESS_EXCEPTION_ENTITY_NOT_FOUND, "获取美容项目信息失败");
            }
            MemberPo memberPo = memberMapper.selectById(po.getMemberId());
            BigDecimal nailMoney = memberPo.getNailMoney();
            BigDecimal beautyMoney = memberPo.getBeautyMoney();
            Integer projectType = union.getProjectTypePo().getTopType();
            if (projectType == ProjectTypeEnum.NAIL.getKey() || projectType == ProjectTypeEnum.LIDS.getKey()) {
                synchronized (this) {
                    if (nailMoney.compareTo(charge) == -1) {
                        throw new GmhException(ErrorCodeEnum.BUSINESS_EXCEPTION_OPERATION_NOT_ALLOWED, "美甲美睫储值不足");
                    }
                    //扣除美甲美睫储值
                    memberMapper.updateNailMoney(po.getMemberId(), nailMoney.subtract(charge));
                }
            } else if (projectType == ProjectTypeEnum.BEAUTY.getKey()
                    || projectType == ProjectTypeEnum.PRODUCT.getKey()) {
                synchronized (this) {
                    if (beautyMoney.compareTo(charge) == -1) {
                        throw new GmhException(ErrorCodeEnum.BUSINESS_EXCEPTION_OPERATION_NOT_ALLOWED, "美容储值不足");
                    }
                    //扣除美容储值
                    memberMapper.updateBeautyMoney(po.getMemberId(), beautyMoney.subtract(charge));
                }
            } else {
                throw new GmhException(ErrorCodeEnum.BUSINESS_EXCEPTION_ILLEGAL_STATUS, "美容项目分类不合法");
            }
        }
        int result = appointmentMapper.finish(id, AppointmentStatusEnum.DONE.getKey());
        return result;
    }

    @Override
    public boolean isConflict(Long storeId, Long EmployeeId, Date beginTime, Date endTime) {
        AppointmentPo po = appointmentMapper.selectByCondition(storeId, EmployeeId,
                AppointmentStatusEnum.TO_DO.getKey(), beginTime, endTime);
        if (po != null) {
            return false;
        }
        return true;
    }

    private List<AppointmentVo> buildAppointmentVosByUnions(List<AppointmentUnion> unions) {
        List<AppointmentVo> vos = Lists.newArrayList();
        for (AppointmentUnion union : unions) {
            AppointmentPo appointmentPo = union.getAppointmentPo();
            EmployeePo employeePo = union.getEmployeePo();
            MemberPo memberPo = union.getMemberPo();
            ProjectPo projectPo = union.getProjectPo();
            AppointmentVo vo = CommonConverter.map(appointmentPo, AppointmentVo.class);
            vo.setMemberName(memberPo.getName());
            vo.setEmployeeName(employeePo.getName());
            vo.setProjectName(projectPo.getName());
            vo.setIsLine(appointmentPo.getLine().booleanValue() ? YesOrNoEnum.YES.getDesc() : YesOrNoEnum.NO.getDesc());
            vos.add(vo);
        }
        return vos;
    }

}
