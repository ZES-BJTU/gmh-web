package com.zes.squad.gmh.web.controller;

import static com.zes.squad.gmh.web.helper.CheckHelper.isValidMobile;
import static com.zes.squad.gmh.web.helper.LogicHelper.ensureParameterExist;
import static com.zes.squad.gmh.web.helper.LogicHelper.ensureParameterValid;

import java.io.PrintWriter;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.google.common.base.Strings;
import com.google.common.collect.Lists;
import com.zes.squad.gmh.common.converter.CommonConverter;
import com.zes.squad.gmh.common.entity.PagedList;
import com.zes.squad.gmh.common.entity.PagedLists;
import com.zes.squad.gmh.common.enums.ChargeWayEnum;
import com.zes.squad.gmh.common.enums.SexEnum;
import com.zes.squad.gmh.common.exception.ErrorCodeEnum;
import com.zes.squad.gmh.common.exception.ErrorMessage;
import com.zes.squad.gmh.common.util.EnumUtils;
import com.zes.squad.gmh.common.util.JsonUtils;
import com.zes.squad.gmh.web.common.JsonResult;
import com.zes.squad.gmh.web.context.ThreadContext;
import com.zes.squad.gmh.web.entity.condition.ConsumeRecordQueryCondition;
import com.zes.squad.gmh.web.entity.dto.ConsumeRecordDto;
import com.zes.squad.gmh.web.entity.dto.ConsumeRecordProjectDto;
import com.zes.squad.gmh.web.entity.dto.StaffDto;
import com.zes.squad.gmh.web.entity.param.ConsumeRecordExportParams;
import com.zes.squad.gmh.web.entity.param.ConsumeRecordParams;
import com.zes.squad.gmh.web.entity.param.ConsumeRecordQueryParams;
import com.zes.squad.gmh.web.entity.vo.ConsumeRecordVo;
import com.zes.squad.gmh.web.entity.vo.MemberVo;
import com.zes.squad.gmh.web.service.ConsumeService;
import com.zes.squad.gmh.web.service.StaffService;
import com.zes.squad.gmh.web.view.ExcelView;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequestMapping(path = "/consume")
@Controller
public class ConsumeController extends BaseController {

    private static final String MEMBER   = "会员";
    private static final String CONSUMER = "普通消费者";

    @Autowired
    private ConsumeService      consumeService;
    @Autowired
    private StaffService        staffService;

    @RequestMapping("/create")
    @ResponseBody
    public JsonResult<Void> doCreateConsumeRecord(ConsumeRecordParams params) {
        checkConsumeRecordParams(params);
        ConsumeRecordDto dto = CommonConverter.map(params, ConsumeRecordDto.class);
        dto.setConsumeRecordProjectDtos(buildConsumeRecordProjectDtoByProjects(params.getProjects()));
        StaffDto staff = getStaff();
        dto.setStoreId(staff.getStoreId());
        consumeService.createConsumeRecord(dto);
        return JsonResult.success();
    }

    @RequestMapping("/listByPhone")
    @ResponseBody
    public JsonResult<List<MemberVo>> doListMemberCardsByPhone(String phone) {
        ensureParameterExist(phone, "会员手机号为空");
        List<MemberVo> vos = consumeService.listMemberCardsByPhone(phone);
        return JsonResult.success(vos);
    }

    @RequestMapping("/modify")
    @ResponseBody
    public JsonResult<Void> doModifyConsumeRecordByRecord(ConsumeRecordParams params) {
        checkConsumeRecordParams(params);
        ensureParameterExist(params.getId(), "消费记录标识为空");
        ConsumeRecordDto dto = CommonConverter.map(params, ConsumeRecordDto.class);
        dto.setConsumeRecordProjectDtos(buildConsumeRecordProjectDtoByProjects(params.getProjects()));
        StaffDto staff = getStaff();
        dto.setStoreId(staff.getStoreId());
        consumeService.createConsumeRecord(dto);
        return JsonResult.success();
    }

    private void checkConsumeRecordParams(ConsumeRecordParams params) {
        ensureParameterExist(params, ErrorMessage.paramIsNull);
        //        ensureParameterExist(params.getProjectId(), ErrorMessage.projectNotSelected);
        //        ensureParameterExist(params.getEmployeeId(), ErrorMessage.employeeNotSelected);
        ensureParameterExist(params.getMobile(), ErrorMessage.consumeRecordMobileIsNull);
        ensureParameterValid(isValidMobile(params.getMobile()), ErrorMessage.consumeRecordMobileIsError);
        ensureParameterExist(params.getConsumerName(), ErrorMessage.consumerNameIsError);
        ensureParameterExist(params.getCharge(), ErrorMessage.consumeRecordChargeIsNull);
        ensureParameterExist(params.getChargeWay(), ErrorMessage.consumeRecordChargeWayIsNull);
        ensureParameterValid(!Strings.isNullOrEmpty(EnumUtils.getDescByKey(ChargeWayEnum.class, params.getChargeWay())),
                ErrorMessage.consumeRecordChargeWayIsError);
        if (params.getChargeWay() != null) {
            ensureParameterExist(params.getMemberId(), "会员卡为空");
        }
        ensureParameterExist(params.getSex(), ErrorMessage.consumerSexIsNull);
        ensureParameterValid(!Strings.isNullOrEmpty(EnumUtils.getDescByKey(SexEnum.class, params.getSex())),
                ErrorMessage.consumerSexIsError);
    }

    @RequestMapping("/search")
    @ResponseBody
    public JsonResult<PagedList<ConsumeRecordVo>> doListPagedConsumeRecords(ConsumeRecordQueryParams params) {
        String error = checkConsumeRecordQueryParams(params);
        if (!Strings.isNullOrEmpty(error)) {
            return JsonResult.fail(ErrorCodeEnum.BUSINESS_EXCEPTION_INVALID_PARAMETERS.getCode(), error);
        }
        ConsumeRecordQueryCondition condition = CommonConverter.map(params, ConsumeRecordQueryCondition.class);
        StaffDto staff = getStaff();
        if (staff == null) {
            return JsonResult.fail(ErrorCodeEnum.BUSINESS_EXCEPTION_ENTITY_NOT_FOUND.getCode(),
                    ErrorMessage.staffIsNull);
        }
        condition.setStoreId(staff.getStoreId());
        PagedList<ConsumeRecordDto> pagedDtos = consumeService.listPagedConsumeRecords(condition);
        if (pagedDtos == null || CollectionUtils.isEmpty(pagedDtos.getData())) {
            return JsonResult.success(PagedLists.newPagedList(pagedDtos.getPageNum(), pagedDtos.getPageSize()));
        }
        PagedList<ConsumeRecordVo> pagedVos = buildPagedConsumeRecordVosByDtos(pagedDtos);
        return JsonResult.success(pagedVos);
    }

    @RequestMapping("/export")
    public ModelAndView doExportToExcel(ConsumeRecordExportParams params, ModelMap map, HttpServletRequest request,
                                        HttpServletResponse response) {
        try {
            checkConsumeRecordExportParams(params);
            log.info("获取用户token成功, url is {}, token is {}.", request.getRequestURI(), params.getToken());
            StaffDto staff = staffService.queryStaffByToken(params.getToken());
            ThreadContext.threadLocal.set(staff);
            ConsumeRecordQueryCondition condition = CommonConverter.map(params, ConsumeRecordQueryCondition.class);
            condition.setStoreId(ThreadContext.getStaffStoreId());
            HSSFWorkbook workbook = consumeService.exportToExcel(condition);
            ExcelView excelView = new ExcelView();
            try {
                excelView.buildExcelDocument(null, workbook, request, response);
            } catch (Exception e) {
                log.error("构建excel文档对象异常", e);
            }
            if (ThreadContext.threadLocal != null) {
                ThreadContext.threadLocal.remove();
            }
            return new ModelAndView(excelView, map);
        } catch (Exception e) {
            log.error("导出消费记录异常", e);
            if (ThreadContext.threadLocal != null) {
                ThreadContext.threadLocal.remove();
            }
            try {
                response.setContentType("application/json;charset=UTF-8");
                PrintWriter writer = response.getWriter();
                writer.write(
                        JsonUtils.toJson(JsonResult.fail(ErrorCodeEnum.BUSINESS_EXCEPTION.getCode(), e.getMessage())));
                writer.flush();
                writer.close();
            } catch (Exception exception) {
                log.error("输出json数据到客户端异常", e);
            }
        }
        return null;
    }

    private String checkConsumeRecordQueryParams(ConsumeRecordQueryParams params) {
        if (params == null) {
            return ErrorMessage.paramIsNull;
        }
        if (params.getPageNum() == null || params.getPageNum() < 0) {
            return ErrorMessage.pageNumIsError;
        }
        if (params.getPageSize() == null || params.getPageSize() < 0) {
            return ErrorMessage.pageSizeIsError;
        }
        return null;
    }

    private PagedList<ConsumeRecordVo> buildPagedConsumeRecordVosByDtos(PagedList<ConsumeRecordDto> pagedDtos) {
        List<ConsumeRecordVo> vos = Lists.newArrayList();
        for (ConsumeRecordDto dto : pagedDtos.getData()) {
            ConsumeRecordVo vo = CommonConverter.map(dto, ConsumeRecordVo.class);
            vo.setSex(EnumUtils.getDescByKey(SexEnum.class, dto.getSex()));
            vo.setChargeWay(EnumUtils.getDescByKey(ChargeWayEnum.class, dto.getChargeWay()));
            if (dto.getMember().booleanValue()) {
                vo.setConsumerDesc(MEMBER);
            } else {
                vo.setConsumerDesc(CONSUMER);
            }
            vos.add(vo);
        }
        return PagedLists.newPagedList(pagedDtos.getPageNum(), pagedDtos.getPageSize(), pagedDtos.getTotalCount(), vos);
    }

    private String checkConsumeRecordExportParams(ConsumeRecordExportParams params) {
        ensureParameterExist(params.getToken(), ErrorMessage.loginAgain);
        if (params != null && params.getStartTime() != null && params.getEndTime() == null) {
            ensureParameterValid(params.getStartTime().before(new Date()), ErrorMessage.consumeRecordStartTimeIsError);
        }
        if (params != null && params.getStartTime() != null && params.getEndTime() != null) {
            ensureParameterValid(params.getStartTime().before(params.getEndTime()),
                    ErrorMessage.consumeRecordStartTimeIsAfterEndTime);
            ensureParameterValid(params.getEndTime().before(new Date()), ErrorMessage.consumeRecordEndTimeIsError);
        }
        return null;
    }

    private List<ConsumeRecordProjectDto> buildConsumeRecordProjectDtoByProjects(String projects) {
        ensureParameterExist(projects, "消费记录项目信息为空");
        List<ConsumeRecordProjectDto> dtos = Lists.newArrayList();
        for (String project : projects.split(";")) {
            String[] details = project.split(",");
            ConsumeRecordProjectDto dto = new ConsumeRecordProjectDto();
            dto.setProjectId(Long.valueOf(details[0]));
            dto.setEmployeeId(Long.valueOf(details[1]));
            dto.setCharge(new BigDecimal(details[2]));
            dtos.add(dto);
        }
        return dtos;
    }
}
