package com.zes.squad.gmh.web.controller;

import static com.zes.squad.gmh.web.helper.CheckHelper.isValidMobile;
import static com.zes.squad.gmh.web.helper.CheckHelper.isValidPageNum;
import static com.zes.squad.gmh.web.helper.CheckHelper.isValidPageSize;
import static com.zes.squad.gmh.web.helper.LogicHelper.ensureParameterExist;
import static com.zes.squad.gmh.web.helper.LogicHelper.ensureParameterValid;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.google.common.base.Strings;
import com.zes.squad.gmh.common.entity.PagedList;
import com.zes.squad.gmh.common.enums.SexEnum;
import com.zes.squad.gmh.common.exception.ErrorCodeEnum;
import com.zes.squad.gmh.common.exception.ErrorMessage;
import com.zes.squad.gmh.common.util.EnumUtils;
import com.zes.squad.gmh.web.common.JsonResult;
import com.zes.squad.gmh.web.entity.dto.MemberDto;
import com.zes.squad.gmh.web.entity.vo.MemberVo;
import com.zes.squad.gmh.web.service.MemberService;

@RequestMapping("/member")
@Controller
public class MemberController {

    @Autowired
    private MemberService memberService;

    @RequestMapping("/listAll")
    @ResponseBody
    public JsonResult<List<MemberVo>> doListMembers() {
        List<MemberVo> vos = memberService.listMembers();
        return JsonResult.success(vos);
    }

    @RequestMapping("/search")
    @ResponseBody
    public JsonResult<PagedList<MemberVo>> doSearch(Integer pageNum, Integer pageSize, Long memberLevelId,
                                                    String searchString) {
        if (!isValidPageNum(pageNum)) {
            return JsonResult.fail(ErrorCodeEnum.BUSINESS_EXCEPTION_INVALID_PARAMETERS.getCode(),
                    ErrorMessage.pageNumIsError);
        }
        if (!isValidPageSize(pageSize)) {
            return JsonResult.fail(ErrorCodeEnum.BUSINESS_EXCEPTION_INVALID_PARAMETERS.getCode(),
                    ErrorMessage.pageSizeIsError);
        }
        PagedList<MemberVo> pagedVos = memberService.search(pageNum, pageSize, memberLevelId, searchString);

        return JsonResult.success(pagedVos);
    }

    @RequestMapping("/insert")
    @ResponseBody
    public JsonResult<Integer> insert(MemberDto dto) {
        checkMemberDto(dto);
        BigDecimal zero = new BigDecimal("0.00");
        dto.setBeautyMoney(zero);
        dto.setNailMoney(zero);
        int i = memberService.insert(dto);
        return JsonResult.success(i);
    }

    @RequestMapping("/update")
    @ResponseBody
    public JsonResult<Integer> update(MemberDto dto) {
        checkMemberDto(dto);
        ensureParameterExist(dto.getId(), ErrorMessage.memberIdIsNull);
        int i = memberService.update(dto);
        return JsonResult.success(i);
    }

    @RequestMapping("/delete")
    @ResponseBody
    public JsonResult<Integer> delete(Long[] id) {
        int i = memberService.deleteByIds(id);
        return JsonResult.success(i);
    }

    @RequestMapping("/recharge")
    @ResponseBody
    public JsonResult<Void> doRecharge(Long id, Long employeeId,Long consultantId, BigDecimal nailMoney, BigDecimal beautyMoney,String source,String remark) {
        ensureParameterExist(id, ErrorMessage.memberIdIsNull);
        if (nailMoney == null && beautyMoney == null) {
            return JsonResult.fail(ErrorCodeEnum.BUSINESS_EXCEPTION_INVALID_PARAMETERS.getCode(), "美甲美睫和美容储值不能同时为空");
        }
        if (nailMoney != null) {
            ensureParameterValid(nailMoney.compareTo(BigDecimal.ZERO) != -1, "美甲美睫储值输入错误");
        }
        if (beautyMoney != null) {
            ensureParameterValid(beautyMoney.compareTo(BigDecimal.ZERO) != -1, "美容储值输入错误");
        }
        memberService.recharge(id, employeeId, consultantId, nailMoney, beautyMoney, source, remark);
        return JsonResult.success();
    }

    @RequestMapping("/queryByPhone")
    @ResponseBody
    public JsonResult<MemberVo> doQueryByPhone(String phone) {
        MemberVo vo = memberService.queryByPhone(phone);
        return JsonResult.success(vo);
    }

    private void checkMemberDto(MemberDto dto) {
        ensureParameterExist(dto, ErrorMessage.paramIsNull);
        ensureParameterExist(dto.getPhone(), ErrorMessage.memberMobileIsNull);
        ensureParameterValid(isValidMobile(dto.getPhone()), ErrorMessage.memberMobileIsError);
        ensureParameterExist(dto.getName(), ErrorMessage.memberNameIsNull);
        ensureParameterExist(dto.getSex(), ErrorMessage.memberSexIsNull);
        ensureParameterValid(
                !Strings.isNullOrEmpty(
                        EnumUtils.getDescByKey(SexEnum.class, Integer.valueOf(String.valueOf(dto.getSex())))),
                ErrorMessage.memberSexIsError);
        ensureParameterExist(dto.getBirthday(), ErrorMessage.memberBirthdayIsNull);
        ensureParameterExist(dto.getValidDate(), ErrorMessage.memberCardValidDateIsNull);
        if (dto.getJoinDate() != null) {
            ensureParameterValid(dto.getJoinDate().before(dto.getValidDate()),
                    ErrorMessage.memberCardOpenDateAfterValidDate);
        }
        //        ensureParameterValid(dto.getNailMoney() != null && dto.getNailMoney().compareTo(BigDecimal.ZERO) != -1,
        //                ErrorMessage.memberNailMoneyIsError);
        //        ensureParameterValid(dto.getBeautyMoney() != null && dto.getBeautyMoney().compareTo(BigDecimal.ZERO) != -1,
        //                ErrorMessage.memberBeautyMoneyIsError);
    }

}
