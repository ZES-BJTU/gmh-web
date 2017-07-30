package com.zes.squad.gmh.web.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.zes.squad.gmh.common.converter.CommonConverter;
import com.zes.squad.gmh.common.entity.PagedList;
import com.zes.squad.gmh.common.exception.ErrorCodeEnum;
import com.zes.squad.gmh.web.common.JsonResult;
import com.zes.squad.gmh.web.context.ThreadContext;
import com.zes.squad.gmh.web.entity.dto.StaffDto;
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
    public JsonResult<?> getAll() {
        List<StockTypeVo> stockTypeList = stockTypeService.getAll();
        return JsonResult.success(stockTypeList);
    }

    @RequestMapping("/search")
    @ResponseBody
    public JsonResult<?> search(Integer pageNum, Integer pageSize, String searchString) {
        if (pageNum == null || pageNum < 0) {
            return JsonResult.fail(ErrorCodeEnum.BUSINESS_EXCEPTION_INVALID_PARAMETERS.getCode(), "分页页码错误");
        }
        if (pageSize == null || pageSize < 0) {
            return JsonResult.fail(ErrorCodeEnum.BUSINESS_EXCEPTION_INVALID_PARAMETERS.getCode(), "分页大小错误");
        }
        PagedList<StockTypeDto> pagedListDto = stockTypeService.searchListByPage(pageNum, pageSize, searchString);
        PagedList<StockTypeVo> pagedListVo = CommonConverter.mapPageList(pagedListDto, StockTypeVo.class);

        return JsonResult.success(pagedListVo);
    }

    @RequestMapping("/insert")
    @ResponseBody
    public JsonResult<?> insert(StockTypeDto dto) {
        if (dto == null) {
            return JsonResult.fail(ErrorCodeEnum.BUSINESS_EXCEPTION_INVALID_PARAMETERS.getCode(), "库存分类不能为空");
        }
        StaffDto staffDto = ThreadContext.getCurrentStaff();
        dto.setStoreId(staffDto.getStoreId());
        int i = stockTypeService.insert(dto);
        if (i > 0) {
            return JsonResult.success(i);
        } else {
            return JsonResult.fail(ErrorCodeEnum.BUSINESS_EXCEPTION_OPERATION_FAILED.getCode(), "添加库存分类失败");
        }
    }

    @RequestMapping("/update")
    @ResponseBody
    public JsonResult<?> update(StockTypeDto dto) {
        if (dto == null) {
            return JsonResult.fail(ErrorCodeEnum.BUSINESS_EXCEPTION_INVALID_PARAMETERS.getCode(), "库存分类不能为空");
        }
        if (dto.getId() == null) {
            return JsonResult.fail(ErrorCodeEnum.BUSINESS_EXCEPTION_INVALID_PARAMETERS.getCode(), "库存分类标识不能为空");
        }
        int i = stockTypeService.update(dto);
        if (i > 0) {
            return JsonResult.success(i);
        } else {
            return JsonResult.fail(ErrorCodeEnum.BUSINESS_EXCEPTION_OPERATION_FAILED.getCode(), "修改库存分类失败");
        }
    }

    @RequestMapping("/delete")
    @ResponseBody
    public JsonResult<?> delete(Long[] id) {
        if (id == null || id.length == 0) {
            return JsonResult.fail(ErrorCodeEnum.BUSINESS_EXCEPTION_INVALID_PARAMETERS.getCode(), "请选择要删除的库存分类");
        }
        int i = stockTypeService.delByIds(id);
        if (i > 0) {
            return JsonResult.success(i);
        } else {
            return JsonResult.fail(ErrorCodeEnum.BUSINESS_EXCEPTION_OPERATION_FAILED.getCode(), "删除库存分类失败");
        }
    }
}
