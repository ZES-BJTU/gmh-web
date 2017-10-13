package com.zes.squad.gmh.web.controller;

import static com.zes.squad.gmh.web.helper.LogicHelper.ensureParameterExist;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.zes.squad.gmh.web.common.JsonResult;
import com.zes.squad.gmh.web.entity.vo.PrintSingleVo;
import com.zes.squad.gmh.web.service.ConsumeService;

@RequestMapping("/print")
@Controller
public class PrintController {

    @Autowired
    private ConsumeService consumeService;

    @RequestMapping("/record")
    @ResponseBody
    public JsonResult<PrintSingleVo> doPrintConsumeRecord(Long id) {
        ensureParameterExist(id, "消费记录标识为空");
        PrintSingleVo vo = consumeService.queryById(id);
        return JsonResult.success(vo);
    }

    @RequestMapping("/records")
    @ResponseBody
    public JsonResult<List<PrintSingleVo>> doPrintConsumeRecords(String mobile, Long memberId, Long startTime,
                                                                 Long endTime) {
        ensureParameterExist(mobile, "手机号为空");
        ensureParameterExist(memberId, "会员标识为空");
        ensureParameterExist(startTime, "开始时间不能为空");
        ensureParameterExist(endTime, "结束时间不能为空");
        return JsonResult.success(consumeService.listConsumeRecords(mobile, memberId, startTime, endTime));
    }

}
