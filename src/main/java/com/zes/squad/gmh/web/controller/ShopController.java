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
import com.zes.squad.gmh.web.entity.dto.ShopDto;
import com.zes.squad.gmh.web.entity.param.ShopParams;
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
    public JsonResult<List<ShopVo>> doListAllShops() {
        List<ShopVo> vos = shopService.listAllShops();
        return JsonResult.success(vos);
    }

    @RequestMapping("/listByPage")
    @ResponseBody
    public JsonResult<PagedList<ShopVo>> doListByPage(Integer pageNum, Integer pageSize) {
        if (!isValidPageNum(pageNum)) {
            return JsonResult.fail(ErrorCodeEnum.BUSINESS_EXCEPTION_INVALID_PARAMETERS.getCode(),
                    ErrorMessage.pageNumIsError);
        }
        if (!isValidPageSize(pageSize)) {
            return JsonResult.fail(ErrorCodeEnum.BUSINESS_EXCEPTION_INVALID_PARAMETERS.getCode(),
                    ErrorMessage.pageSizeIsError);
        }
        PagedList<ShopDto> pagedDtos = shopService.listByPage(pageNum, pageSize);
        PagedList<ShopVo> pagedVos = CommonConverter.mapPageList(pagedDtos, ShopVo.class);

        return JsonResult.success(pagedVos);
    }

    @RequestMapping("/search")
    @ResponseBody
    public JsonResult<PagedList<ShopVo>> search(Integer pageNum, Integer pageSize, String searchString) {
        if (!isValidPageNum(pageNum)) {
            return JsonResult.fail(ErrorCodeEnum.BUSINESS_EXCEPTION_INVALID_PARAMETERS.getCode(),
                    ErrorMessage.pageNumIsError);
        }
        if (!isValidPageSize(pageSize)) {
            return JsonResult.fail(ErrorCodeEnum.BUSINESS_EXCEPTION_INVALID_PARAMETERS.getCode(),
                    ErrorMessage.pageSizeIsError);
        }
        PagedList<ShopDto> pagedDtos = shopService.searchListByPage(pageNum, pageSize, searchString);
        PagedList<ShopVo> pagedVos = CommonConverter.mapPageList(pagedDtos, ShopVo.class);

        return JsonResult.success(pagedVos);
    }

    @RequestMapping("/insert")
    @ResponseBody
    public JsonResult<Integer> insert(ShopParams params) {
        String error = checkCreateShopParam(params);
        if (!Strings.isNullOrEmpty(error)) {
            return JsonResult.fail(ErrorCodeEnum.BUSINESS_EXCEPTION_INVALID_PARAMETERS.getCode(), error);
        }
        ShopDto dto = CommonConverter.map(params, ShopDto.class);
        int i = shopService.insert(dto);
        return JsonResult.success(i);
    }

    @RequestMapping("/update")
    @ResponseBody
    public JsonResult<Integer> update(ShopParams params) {
        String error = checkModifyShopParam(params);
        if (!Strings.isNullOrEmpty(error)) {
            return JsonResult.fail(ErrorCodeEnum.BUSINESS_EXCEPTION_INVALID_PARAMETERS.getCode(), error);
        }
        ShopDto dto = CommonConverter.map(params, ShopDto.class);
        int i = shopService.update(dto);
        return JsonResult.success(i);
    }

    @RequestMapping("/delByIds")
    @ResponseBody
    public JsonResult<?> delByIds(Long[] id) {
        if (id == null || id.length == 0) {
            return JsonResult.fail(ErrorCodeEnum.BUSINESS_EXCEPTION_INVALID_PARAMETERS.getCode(), "请选择要删除的店铺");
        }
        int i = shopService.deleteByIds(id);
        return JsonResult.success(i);
    }

    private String checkModifyShopParam(ShopParams params) {
        String error = checkShopParam(params);
        if (!Strings.isNullOrEmpty(error)) {
            return error;
        }
        if (params.getId() == null) {
            return "店铺标识不能为空";
        }
        return null;
    }

    private String checkCreateShopParam(ShopParams params) {
        return checkShopParam(params);
    }

    private String checkShopParam(ShopParams params) {
        if (params == null) {
            return "店铺信息不能为空";
        }
        if (Strings.isNullOrEmpty(params.getShopName())) {
            return "店铺名不能为空";
        }
        if (Strings.isNullOrEmpty(params.getManager())) {
            return "店铺负责人姓名不能为空";
        }
        if (Strings.isNullOrEmpty(params.getPhone())) {
            return "店铺负责人电话不能为空";
        }
        if (!CheckHelper.isValidMobile(params.getPhone())) {
            return "店铺负责人电话格式错误";
        }
        if (Strings.isNullOrEmpty(params.getAddress())) {
            return "店铺地址不能为空";
        }
        return null;
    }

}
