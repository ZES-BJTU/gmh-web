package com.zes.squad.gmh.web.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.zes.squad.gmh.common.converter.CommonConverter;
import com.zes.squad.gmh.common.entity.PagedList;
import com.zes.squad.gmh.web.common.JsonResult;
import com.zes.squad.gmh.web.entity.dto.ShopDto;
import com.zes.squad.gmh.web.entity.param.ShopParam;
import com.zes.squad.gmh.web.entity.vo.ShopVo;
import com.zes.squad.gmh.web.service.ShopService;

@RequestMapping("/shop")
@Controller
public class ShopController {

    @Autowired
    private ShopService shopService;

    @RequestMapping("/getAll")
    @ResponseBody
    public JsonResult<?> getAll() {

        List<ShopVo> voList = new ArrayList<ShopVo>();
        voList = shopService.getAll();
        return JsonResult.success(voList);
    }

    @RequestMapping("/listByPage")
    @ResponseBody
    public JsonResult<PagedList<ShopVo>> doListByPage(Integer pageNum, Integer pageSize) {

        PagedList<ShopDto> pagedListDto = shopService.listByPage(pageNum, pageSize);
        PagedList<ShopVo> pagedListVo = CommonConverter.mapPageList(pagedListDto, ShopVo.class);

        return JsonResult.success(pagedListVo);
    }

    @RequestMapping("/search")
    @ResponseBody
    public JsonResult<PagedList<ShopVo>> search(Integer pageNum, Integer pageSize, String searchString) {

        PagedList<ShopDto> pagedListDto = shopService.searchListByPage(pageNum, pageSize, searchString);
        PagedList<ShopVo> pagedListVo = CommonConverter.mapPageList(pagedListDto, ShopVo.class);

        return JsonResult.success(pagedListVo);
    }

    @RequestMapping("/insert")
    @ResponseBody
    public JsonResult<?> insert(ShopParam param) {

        ShopDto dto = CommonConverter.map(param, ShopDto.class);
        int i = shopService.insert(dto);
        return JsonResult.success(i);
    }

    @RequestMapping("/update")
    @ResponseBody
    public JsonResult<?> update(ShopParam param) {

        ShopDto dto = CommonConverter.map(param, ShopDto.class);
        int i = shopService.update(dto);
        return JsonResult.success(i);
    }

    @RequestMapping("/delByIds")
    @ResponseBody
    public JsonResult<?> delByIds(Long[] id) {
        int i = shopService.delByIds(id);
        return JsonResult.success(i);
    }

}
