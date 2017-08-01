package com.zes.squad.gmh.web.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.google.common.base.Strings;
import com.zes.squad.gmh.common.converter.CommonConverter;
import com.zes.squad.gmh.common.entity.PagedList;
import com.zes.squad.gmh.common.exception.ErrorCodeEnum;
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

    @RequestMapping("/getAll")
    @ResponseBody
    public JsonResult<?> getAll() {
        List<MemberLevelVo> vos = memberLevelService.getAll();
        return JsonResult.success(vos);
    }

    @RequestMapping("/listByPage")
    @ResponseBody
    public JsonResult<PagedList<MemberLevelVo>> doListByPage(Integer pageNum, Integer pageSize) {
        if (pageNum == null || pageNum < 0) {
            return JsonResult.fail(ErrorCodeEnum.BUSINESS_EXCEPTION_INVALID_PARAMETERS.getCode(), "分页页码错误");
        }
        if (pageSize == null || pageSize < 0) {
            return JsonResult.fail(ErrorCodeEnum.BUSINESS_EXCEPTION_INVALID_PARAMETERS.getCode(), "分页页码错误");
        }
        PagedList<MemberLevelDto> pagedListDto = memberLevelService.listByPage(pageNum, pageSize);
        PagedList<MemberLevelVo> pagedListVo = CommonConverter.mapPageList(pagedListDto, MemberLevelVo.class);

        return JsonResult.success(pagedListVo);
    }

    @RequestMapping("/insert")
    @ResponseBody
    public JsonResult<?> insert(MemberLevelDto dto) {
        if (dto == null || Strings.isNullOrEmpty(dto.getLevelName())) {
            return JsonResult.fail(ErrorCodeEnum.BUSINESS_EXCEPTION_INVALID_PARAMETERS.getCode(), "会员等级信息不能为空");
        }
        dto.setStoreId(ThreadContext.getStaffStoreId());
        int i = memberLevelService.insert(dto);
        if (i > 0) {
            return JsonResult.success(i);
        } else {
            return JsonResult.fail(10006, "新增失败");
        }
    }

    @RequestMapping("/update")
    @ResponseBody
    public JsonResult<?> update(MemberLevelDto dto) {
        if (dto == null || Strings.isNullOrEmpty(dto.getLevelName())) {
            return JsonResult.fail(ErrorCodeEnum.BUSINESS_EXCEPTION_INVALID_PARAMETERS.getCode(), "会员等级信息不能为空");
        }
        if (dto.getId() == null) {
            return JsonResult.fail(ErrorCodeEnum.BUSINESS_EXCEPTION_INVALID_PARAMETERS.getCode(), "会员等级信息标识不能为空");
        }
        if (dto.getStoreId() == null) {
            return JsonResult.fail(ErrorCodeEnum.BUSINESS_EXCEPTION_INVALID_PARAMETERS.getCode(), "会员等级所属门店信息不能为空");
        }
        int i = memberLevelService.update(dto);
        if (i > 0) {
            return JsonResult.success(i);
        } else {
            return JsonResult.fail(10006, "修改失败");
        }
    }

    @RequestMapping("/delete")
    @ResponseBody
    public JsonResult<?> delete(Long[] id) {
        if (id == null || id.length == 0) {
            return JsonResult.fail(ErrorCodeEnum.BUSINESS_EXCEPTION_INVALID_PARAMETERS.getCode(), "请选择要删除的会员等级信息");
        }
        int i = 0;
        i = memberLevelService.delByIds(id);
        if (i > 0) {
            return JsonResult.success(i);
        } else {
            return JsonResult.fail(10006, "发生错误，没有数据被修改");
        }
    }
}
