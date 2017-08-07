package com.zes.squad.gmh.web.controller;

import static com.zes.squad.gmh.web.helper.CheckHelper.isValidMobile;
import static com.zes.squad.gmh.web.helper.LogicHelper.ensureParameterExist;
import static com.zes.squad.gmh.web.helper.LogicHelper.ensureParameterValid;

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
import com.zes.squad.gmh.web.common.JsonResult;
import com.zes.squad.gmh.web.context.ThreadContext;
import com.zes.squad.gmh.web.entity.condition.ConsumeRecordQueryCondition;
import com.zes.squad.gmh.web.entity.dto.ConsumeRecordDto;
import com.zes.squad.gmh.web.entity.dto.StaffDto;
import com.zes.squad.gmh.web.entity.param.ConsumeRecordExportParams;
import com.zes.squad.gmh.web.entity.param.ConsumeRecordParams;
import com.zes.squad.gmh.web.entity.param.ConsumeRecordQueryParams;
import com.zes.squad.gmh.web.entity.vo.ConsumeRecordVo;
import com.zes.squad.gmh.web.service.ConsumeService;
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

    @RequestMapping("/create")
    @ResponseBody
    public JsonResult<Void> doCreateConsumeRecord(ConsumeRecordParams params) {
        checkConsumeRecordParams(params);
        ConsumeRecordDto dto = CommonConverter.map(params, ConsumeRecordDto.class);
        StaffDto staff = getStaff();
        dto.setStoreId(staff.getStoreId());
        consumeService.createConsumeRecord(dto);
        return JsonResult.success();
    }

    private void checkConsumeRecordParams(ConsumeRecordParams params) {
        ensureParameterExist(params, ErrorMessage.paramIsNull);
        ensureParameterExist(params.getProjectId(), ErrorMessage.projectNotSelected);
        ensureParameterExist(params.getEmployeeId(), ErrorMessage.employeeNotSelected);
        ensureParameterExist(params.getMobile(), ErrorMessage.consumeRecordMobileIsNull);
        ensureParameterValid(isValidMobile(params.getMobile()), ErrorMessage.consumeRecordMobileIsError);
        ensureParameterExist(params.getConsumerName(), ErrorMessage.consumerNameIsError);
        ensureParameterExist(params.getCharge(), ErrorMessage.consumeRecordChargeIsNull);
        ensureParameterValid(params.getCharge().compareTo(BigDecimal.ZERO) == 1,
                ErrorMessage.consumeRecordChargeIsError);
        if (params.getDiscount() != null) {
            ensureParameterValid(params.getCharge().compareTo(BigDecimal.ZERO) == 1,
                    ErrorMessage.consumeRecordChargeIsError);
        }
        ensureParameterExist(params.getChargeWay(), ErrorMessage.consumeRecordChargeWayIsNull);
        ensureParameterValid(!Strings.isNullOrEmpty(EnumUtils.getDescByKey(ChargeWayEnum.class, params.getChargeWay())),
                ErrorMessage.consumeRecordChargeWayIsError);
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
        checkConsumeRecordExportParams(params);
        ConsumeRecordQueryCondition condition = CommonConverter.map(params, ConsumeRecordQueryCondition.class);
        condition.setStoreId(ThreadContext.getStaffStoreId());
        HSSFWorkbook workbook = consumeService.exportToExcel(condition);
        ExcelView excelView = new ExcelView();
        try {
            excelView.buildExcelDocument(null, workbook, request, response);
        } catch (Exception e) {
            log.error("构建excel文档对象异常", e);
        }
        return new ModelAndView(excelView, map);
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
        if (params != null && params.getStartTime() == null && params.getEndTime() != null) {
            ensureParameterValid(params.getEndTime().before(new Date()), ErrorMessage.consumeRecordEndTimeIsError);
        }
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
}
