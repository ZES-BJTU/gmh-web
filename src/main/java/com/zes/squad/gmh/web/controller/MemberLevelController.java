package com.zes.squad.gmh.web.controller;

import static com.zes.squad.gmh.web.helper.CheckHelper.isValidPageNum;
import static com.zes.squad.gmh.web.helper.CheckHelper.isValidPageSize;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.google.common.base.Strings;
import com.zes.squad.gmh.common.converter.CommonConverter;
import com.zes.squad.gmh.common.entity.PagedList;
import com.zes.squad.gmh.common.exception.ErrorCodeEnum;
import com.zes.squad.gmh.common.exception.ErrorMessage;
import com.zes.squad.gmh.web.common.JsonResult;
import com.zes.squad.gmh.web.context.ThreadContext;
import com.zes.squad.gmh.web.entity.dto.MemberLevelDto;
import com.zes.squad.gmh.web.entity.vo.MemberLevelVo;
import com.zes.squad.gmh.web.service.MemberLevelService;

@RequestMapping("/memberLevel")
@Controller
public class MemberLevelController {
    
    @Autowired
    private MemberLevelService memberLevelService;

    @RequestMapping("/listAll")
    @ResponseBody
    public JsonResult<List<MemberLevelVo>> doListMemberLevels() {
        List<MemberLevelVo> vos = memberLevelService.listAllMemberLevels();
        return JsonResult.success(vos);
    }

    @RequestMapping("/listByPage")
    @ResponseBody
    public JsonResult<PagedList<MemberLevelVo>> doListByPage(Integer pageNum, Integer pageSize) {
        if (!isValidPageNum(pageNum)) {
            return JsonResult.fail(ErrorCodeEnum.BUSINESS_EXCEPTION_INVALID_PARAMETERS.getCode(),
                    ErrorMessage.pageNumIsError);
        }
        if (!isValidPageSize(pageSize)) {
            return JsonResult.fail(ErrorCodeEnum.BUSINESS_EXCEPTION_INVALID_PARAMETERS.getCode(),
                    ErrorMessage.pageSizeIsError);
        }
        PagedList<MemberLevelDto> pagedDtos = memberLevelService.listByPage(pageNum, pageSize);
        PagedList<MemberLevelVo> pagedVos = CommonConverter.mapPageList(pagedDtos, MemberLevelVo.class);

        return JsonResult.success(pagedVos);
    }

    @RequestMapping("/insert")
    @ResponseBody
    public JsonResult<Integer> insert(MemberLevelDto dto) {
        String error = checkMemberLevelDto(dto);
        if (!Strings.isNullOrEmpty(error)) {
            return JsonResult.fail(ErrorCodeEnum.BUSINESS_EXCEPTION_INVALID_PARAMETERS.getCode(), error);
        }
        dto.setStoreId(ThreadContext.getStaffStoreId());
        int i = memberLevelService.insert(dto);
        return JsonResult.success(i);
    }

    @RequestMapping("/update")
    @ResponseBody
    public JsonResult<Integer> update(MemberLevelDto dto) {
        String error = checkMemberLevelDto(dto);
        if (!Strings.isNullOrEmpty(error)) {
            return JsonResult.fail(ErrorCodeEnum.BUSINESS_EXCEPTION_INVALID_PARAMETERS.getCode(), error);
        }
        if (dto.getId() == null) {
            return JsonResult.fail(ErrorCodeEnum.BUSINESS_EXCEPTION_INVALID_PARAMETERS.getCode(),
                    ErrorMessage.memberLevelIdIsNull);
        }
        int i = memberLevelService.update(dto);
        return JsonResult.success(i);
    }

    @RequestMapping("/delete")
    @ResponseBody
    public JsonResult<Integer> delete(Long[] id) {
        if (id == null || id.length == 0) {
            return JsonResult.fail(ErrorCodeEnum.BUSINESS_EXCEPTION_INVALID_PARAMETERS.getCode(),
                    ErrorMessage.memberLevelNotSelectedForDelete);
        }
        int i = memberLevelService.deleteByIds(id);
        return JsonResult.success(i);
    }

    private String checkMemberLevelDto(MemberLevelDto dto) {
        if (dto == null) {
            return ErrorMessage.paramIsNull;
        }
        if (Strings.isNullOrEmpty(dto.getLevelName())) {
            return ErrorMessage.memberLevelNameIsNull;
        }
        return null;
    }

}
