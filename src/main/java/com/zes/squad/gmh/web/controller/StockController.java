package com.zes.squad.gmh.web.controller;

import static com.zes.squad.gmh.web.helper.CheckHelper.isValidPageNum;
import static com.zes.squad.gmh.web.helper.CheckHelper.isValidPageSize;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.google.common.base.Strings;
import com.zes.squad.gmh.common.entity.PagedList;
import com.zes.squad.gmh.common.exception.ErrorCodeEnum;
import com.zes.squad.gmh.common.exception.ErrorMessage;
import com.zes.squad.gmh.web.common.JsonResult;
import com.zes.squad.gmh.web.entity.dto.StockDto;
import com.zes.squad.gmh.web.entity.vo.StockVo;
import com.zes.squad.gmh.web.service.StockService;

@RequestMapping("/stock")
@Controller
public class StockController {
    @Autowired
    private StockService stockService;

    @RequestMapping("/search")
    @ResponseBody
    public JsonResult<PagedList<StockVo>> search(Integer pageNum, Integer pageSize, Long typeId, String searchString) {
        if (!isValidPageNum(pageNum)) {
            return JsonResult.fail(ErrorCodeEnum.BUSINESS_EXCEPTION_INVALID_PARAMETERS.getCode(),
                    ErrorMessage.pageNumIsError);
        }
        if (!isValidPageSize(pageSize)) {
            return JsonResult.fail(ErrorCodeEnum.BUSINESS_EXCEPTION_INVALID_PARAMETERS.getCode(),
                    ErrorMessage.pageSizeIsError);
        }
        PagedList<StockVo> pagedVos = stockService.searchListByPage(pageNum, pageSize, typeId, searchString);

        return JsonResult.success(pagedVos);
    }

    @RequestMapping("/getByType")
    @ResponseBody
    public JsonResult<List<StockVo>> getByType(Long typeId) {
        if (typeId == null) {
            return JsonResult.fail(ErrorCodeEnum.BUSINESS_EXCEPTION_INVALID_PARAMETERS.getCode(),
                    ErrorMessage.stockTypeIdIsNull);
        }
        List<StockVo> vos = stockService.listStockVosByType(typeId);
        return JsonResult.success(vos);
    }

    @RequestMapping("/insert")
    @ResponseBody
    public JsonResult<Integer> insert(StockDto dto) {
        String error = checkStock(dto);
        if (!Strings.isNullOrEmpty(error)) {
            return JsonResult.fail(ErrorCodeEnum.BUSINESS_EXCEPTION_INVALID_PARAMETERS.getCode(), error);
        }
        int i = stockService.insert(dto);
        return JsonResult.success(i);
    }

    private String checkStock(StockDto dto) {
        if (dto == null) {
            return ErrorMessage.paramIsNull;
        }
        if (dto.getTypeId() == null) {
            return ErrorMessage.stockTypeIdIsNull;
        }
        if (Strings.isNullOrEmpty(dto.getStockName())) {
            return ErrorMessage.stockNameIsNull;
        }
        if (Strings.isNullOrEmpty(dto.getUnit())) {
            return ErrorMessage.stockUnitIsNull;
        }
        if (dto.getAmount() < 0) {
            return ErrorMessage.stockAmountIsError;
        }
        return null;
    }

    @RequestMapping("/update")
    @ResponseBody
    public JsonResult<Integer> update(StockDto dto) {
        String error = checkStock(dto);
        if (!Strings.isNullOrEmpty(error)) {
            return JsonResult.fail(ErrorCodeEnum.BUSINESS_EXCEPTION_INVALID_PARAMETERS.getCode(), error);
        }
        if (dto.getId() == null) {
            return JsonResult.fail(ErrorCodeEnum.BUSINESS_EXCEPTION_INVALID_PARAMETERS.getCode(),
                    ErrorMessage.stockIdIsError);
        }
        int i = stockService.update(dto);
        return JsonResult.success(i);
    }

    @RequestMapping("/delete")
    @ResponseBody
    public JsonResult<Integer> delete(Long[] id) {
        if (id == null || id.length == 0) {
            return JsonResult.fail(ErrorCodeEnum.BUSINESS_EXCEPTION_INVALID_PARAMETERS.getCode(), "请选择要删除的库存");
        }
        int i = stockService.deleteByIds(id);
        return JsonResult.success(i);
    }

}
