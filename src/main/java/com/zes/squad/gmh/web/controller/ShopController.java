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
import com.zes.squad.gmh.web.entity.dto.ShopDto;
import com.zes.squad.gmh.web.entity.param.ShopParam;
import com.zes.squad.gmh.web.entity.vo.ShopVo;
import com.zes.squad.gmh.web.helper.CheckHelper;
import com.zes.squad.gmh.web.service.ShopService;

@RequestMapping("/shop")
@Controller
public class ShopController {

    @Autowired
    private ShopService shopService;

    @RequestMapping("/getAll")
    @ResponseBody
    public JsonResult<List<ShopVo>> getAll() {
        List<ShopVo> voList = shopService.getAll();
        return JsonResult.success(voList);
    }

    @RequestMapping("/listByPage")
    @ResponseBody
    public JsonResult<PagedList<ShopVo>> doListByPage(Integer pageNum, Integer pageSize) {
        if (pageNum == null || pageNum < 0) {
            return JsonResult.fail(ErrorCodeEnum.BUSINESS_EXCEPTION_INVALID_PARAMETERS.getCode(), "分页页码错误");
        }
        if (pageSize == null || pageSize < 0) {
            return JsonResult.fail(ErrorCodeEnum.BUSINESS_EXCEPTION_INVALID_PARAMETERS.getCode(), "分页大小错误");
        }
        PagedList<ShopDto> pagedListDto = shopService.listByPage(pageNum, pageSize);
        PagedList<ShopVo> pagedListVo = CommonConverter.mapPageList(pagedListDto, ShopVo.class);

        return JsonResult.success(pagedListVo);
    }

    @RequestMapping("/search")
    @ResponseBody
    public JsonResult<PagedList<ShopVo>> search(Integer pageNum, Integer pageSize, String searchString) {
        if (pageNum == null || pageNum < 0) {
            return JsonResult.fail(ErrorCodeEnum.BUSINESS_EXCEPTION_INVALID_PARAMETERS.getCode(), "分页页码错误");
        }
        if (pageSize == null || pageSize < 0) {
            return JsonResult.fail(ErrorCodeEnum.BUSINESS_EXCEPTION_INVALID_PARAMETERS.getCode(), "分页大小错误");
        }
        PagedList<ShopDto> pagedListDto = shopService.searchListByPage(pageNum, pageSize, searchString);
        PagedList<ShopVo> pagedListVo = CommonConverter.mapPageList(pagedListDto, ShopVo.class);

        return JsonResult.success(pagedListVo);
    }

    @RequestMapping("/insert")
    @ResponseBody
    public JsonResult<?> insert(ShopParam param) {
        String error = checkCreateShopParam(param);
        if (!Strings.isNullOrEmpty(error)) {
            return JsonResult.fail(ErrorCodeEnum.BUSINESS_EXCEPTION_INVALID_PARAMETERS.getCode(), error);
        }
        ShopDto dto = CommonConverter.map(param, ShopDto.class);
        int i = shopService.insert(dto);
        return JsonResult.success(i);
    }

    private String checkCreateShopParam(ShopParam param) {
        return checkShopParam(param);
    }

    private String checkShopParam(ShopParam param) {
        if (param == null) {
            return "店铺信息不能为空";
        }
        if (Strings.isNullOrEmpty(param.getShopName())) {
            return "店铺名不能为空";
        }
        if (Strings.isNullOrEmpty(param.getManager())) {
            return "店铺负责人姓名不能为空";
        }
        if (Strings.isNullOrEmpty(param.getPhone())) {
            return "店铺负责人电话不能为空";
        }
        if (!CheckHelper.isValidMobile(param.getPhone())) {
            return "店铺负责人电话格式错误";
        }
        if (Strings.isNullOrEmpty(param.getAddress())) {
            return "店铺地址不能为空";
        }
        return null;
    }

    @RequestMapping("/update")
    @ResponseBody
    public JsonResult<?> update(ShopParam param) {
        String error = checkModifyShopParam(param);
        if (!Strings.isNullOrEmpty(error)) {
            return JsonResult.fail(ErrorCodeEnum.BUSINESS_EXCEPTION_INVALID_PARAMETERS.getCode(), error);
        }
        ShopDto dto = CommonConverter.map(param, ShopDto.class);
        int i = shopService.update(dto);
        return JsonResult.success(i);
    }

    private String checkModifyShopParam(ShopParam param) {
        String error = checkShopParam(param);
        if (!Strings.isNullOrEmpty(error)) {
            return error;
        }
        if (param.getId() == null) {
            return "店铺标识不能为空";
        }
        return null;
    }

    @RequestMapping("/delByIds")
    @ResponseBody
    public JsonResult<?> delByIds(Long[] id) {
        int i = shopService.delByIds(id);
        return JsonResult.success(i);
    }

}
