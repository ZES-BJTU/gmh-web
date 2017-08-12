package com.zes.squad.gmh.web.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.zes.squad.gmh.web.common.JsonResult;
import com.zes.squad.gmh.web.entity.vo.StatisticsVo;
import com.zes.squad.gmh.web.service.StatisticsService;

@RequestMapping("/statistics")
@RestController
public class StatisticsController extends BaseController{
    
    @Autowired
    private StatisticsService statisticsService;
    
    @RequestMapping("/record")
    public JsonResult<List<StatisticsVo>> doListAll(){
        List<StatisticsVo> vos = statisticsService.countConsumeRecord();
        return JsonResult.success(vos);
    }

}
