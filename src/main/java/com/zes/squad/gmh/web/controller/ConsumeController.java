package com.zes.squad.gmh.web.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.zes.squad.gmh.common.converter.CommonConverter;
import com.zes.squad.gmh.web.common.JsonResult;
import com.zes.squad.gmh.web.entity.dto.ConsumeRecordDto;
import com.zes.squad.gmh.web.entity.dto.StaffDto;
import com.zes.squad.gmh.web.entity.param.ConsumeRecordParams;
import com.zes.squad.gmh.web.service.ConsumeService;

@RequestMapping(path = "/consume")
@Controller
public class ConsumeController extends BaseController {

    @Autowired
    private ConsumeService consumeService;

    @RequestMapping("/add")
    @ResponseBody
    public JsonResult<Void> doAddConsumeRecord(ConsumeRecordParams params) {
        ConsumeRecordDto dto = CommonConverter.map(params, ConsumeRecordDto.class);
        StaffDto staff = getStaff();
        dto.setStoreId(staff.getStoreId());
        consumeService.addConsumeRecord(dto);
        return JsonResult.success();
    }

}
