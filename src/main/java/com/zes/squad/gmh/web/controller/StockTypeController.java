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
import com.zes.squad.gmh.web.entity.dto.StockTypeDto;
import com.zes.squad.gmh.web.entity.vo.StockTypeVo;
import com.zes.squad.gmh.web.service.StockTypeService;

@RequestMapping("/stockType")
@Controller
public class StockTypeController {

    @Autowired
    private StockTypeService stockTypeService;

    @RequestMapping("/getAll")
    @ResponseBody
    public JsonResult<List<StockTypeVo>> doListStockTypeVos() {
        List<StockTypeVo> vos = stockTypeService.listStockTypeVos();
        return JsonResult.success(vos);
    }

    @RequestMapping("/search")
    @ResponseBody
    public JsonResult<?> search(Integer pageNum, Integer pageSize, String searchString) {
        if (!isValidPageNum(pageNum)) {
            return JsonResult.fail(ErrorCodeEnum.BUSINESS_EXCEPTION_INVALID_PARAMETERS.getCode(),
                    ErrorMessage.pageNumIsError);
        }
        if (!isValidPageSize(pageSize)) {
            return JsonResult.fail(ErrorCodeEnum.BUSINESS_EXCEPTION_INVALID_PARAMETERS.getCode(),
                    ErrorMessage.pageSizeIsError);
        }
        PagedList<StockTypeDto> pagedDtos = stockTypeService.searchListByPage(pageNum, pageSize, searchString);
        PagedList<StockTypeVo> pagedVos = CommonConverter.mapPageList(pagedDtos, StockTypeVo.class);

        return JsonResult.success(pagedVos);
    }

    @RequestMapping("/insert")
    @ResponseBody
    public JsonResult<Integer> insert(StockTypeDto dto) {
        if (dto == null) {
            return JsonResult.fail(ErrorCodeEnum.BUSINESS_EXCEPTION_INVALID_PARAMETERS.getCode(),
                    ErrorMessage.paramIsNull);
        }
        if (Strings.isNullOrEmpty(dto.getTypeName())) {
            return JsonResult.fail(ErrorCodeEnum.BUSINESS_EXCEPTION_INVALID_PARAMETERS.getCode(),
                    ErrorMessage.stockTypeNameIsNull);
        }
        dto.setStoreId(ThreadContext.getStaffStoreId());
        int i = stockTypeService.insert(dto);
        return JsonResult.success(i);
    }

    @RequestMapping("/update")
    @ResponseBody
    public JsonResult<Integer> update(StockTypeDto dto) {
        if (dto == null) {
            return JsonResult.fail(ErrorCodeEnum.BUSINESS_EXCEPTION_INVALID_PARAMETERS.getCode(),
                    ErrorMessage.paramIsNull);
        }
        if (Strings.isNullOrEmpty(dto.getTypeName())) {
            return JsonResult.fail(ErrorCodeEnum.BUSINESS_EXCEPTION_INVALID_PARAMETERS.getCode(),
                    ErrorMessage.stockTypeNameIsNull);
        }
        if (dto.getId() == null) {
            return JsonResult.fail(ErrorCodeEnum.BUSINESS_EXCEPTION_INVALID_PARAMETERS.getCode(),
                    ErrorMessage.stockTypeIdIsNull);
        }
        int i = stockTypeService.update(dto);
        return JsonResult.success(i);
    }

    @RequestMapping("/delete")
    @ResponseBody
    public JsonResult<Integer> delete(Long[] id) {
        if (id == null || id.length == 0) {
            return JsonResult.fail(ErrorCodeEnum.BUSINESS_EXCEPTION_INVALID_PARAMETERS.getCode(),
                    ErrorMessage.stockTypeNotSelectedForDelete);
        }
        int i = stockTypeService.deleteByIds(id);
        return JsonResult.success(i);
    }
}
