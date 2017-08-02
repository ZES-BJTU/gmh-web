package com.zes.squad.gmh.web.controller;

import java.util.List;

import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.google.common.base.Strings;
import com.google.common.collect.Lists;
import com.zes.squad.gmh.common.converter.CommonConverter;
import com.zes.squad.gmh.common.entity.PagedList;
import com.zes.squad.gmh.common.entity.PagedLists;
import com.zes.squad.gmh.common.enums.ChargeWayEnum;
import com.zes.squad.gmh.common.exception.ErrorCodeEnum;
import com.zes.squad.gmh.common.exception.ErrorMessage;
import com.zes.squad.gmh.common.util.EnumUtils;
import com.zes.squad.gmh.web.common.JsonResult;
import com.zes.squad.gmh.web.entity.condition.ConsumeRecordQueryCondition;
import com.zes.squad.gmh.web.entity.dto.ConsumeRecordDto;
import com.zes.squad.gmh.web.entity.dto.StaffDto;
import com.zes.squad.gmh.web.entity.param.ConsumeRecordExportParams;
import com.zes.squad.gmh.web.entity.param.ConsumeRecordParams;
import com.zes.squad.gmh.web.entity.param.ConsumeRecordQueryParams;
import com.zes.squad.gmh.web.entity.vo.ConsumeRecordVo;
import com.zes.squad.gmh.web.service.ConsumeService;

@RequestMapping(path = "/consume")
@Controller
public class ConsumeController extends BaseController {

    private static final String MEMBER   = "会员";
    private static final String CONSUMER = "普通消费者";

    @Autowired
    private ConsumeService      consumeService;

    @RequestMapping("/add")
    @ResponseBody
    public JsonResult<Void> doAddConsumeRecord(ConsumeRecordParams params) {
        ConsumeRecordDto dto = CommonConverter.map(params, ConsumeRecordDto.class);
        StaffDto staff = getStaff();
        dto.setStoreId(staff.getStoreId());
        consumeService.addConsumeRecord(dto);
        return JsonResult.success();
    }

    @RequestMapping("/listByPage")
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
            return JsonResult.success(
                    PagedLists.newPagedList(pagedDtos.getPageNum(), pagedDtos.getPageSize()));
        }
        PagedList<ConsumeRecordVo> pagedVos = buildPagedConsumeRecordVosByDtos(pagedDtos);
        return JsonResult.success(pagedVos);
    }

    @RequestMapping("/export")
    public JsonResult<Void> doExportToExcel(ConsumeRecordExportParams params) {
        String error = checkConsumeRecordExportParams(params);
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
        consumeService.exportToExcel(condition);
        return JsonResult.success();
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
            vo.setChargeWay(EnumUtils.getDescByKey(ChargeWayEnum.class, dto.getChargeWay()));
            if (dto.getMember().booleanValue()) {
                vo.setMemberDesc(MEMBER);
            } else {
                vo.setMemberDesc(CONSUMER);
            }
            vos.add(vo);
        }
        return PagedLists.newPagedList(pagedDtos.getPageNum(), pagedDtos.getPageSize(), pagedDtos.getTotalCount(), vos);
    }

    private String checkConsumeRecordExportParams(ConsumeRecordExportParams params) {
        return null;
    }
}
