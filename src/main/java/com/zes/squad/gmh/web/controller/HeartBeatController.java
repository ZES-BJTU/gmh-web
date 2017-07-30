package com.zes.squad.gmh.web.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.zes.squad.gmh.web.common.JsonResult;

/**
 * 心跳检测
 * 
 * @author zhouyuhuan 2017年7月29日 下午8:16:55
 */
@RestController
public class HeartBeatController {

    @RequestMapping(path = "/alive", method = RequestMethod.HEAD)
    public JsonResult<Void> doCheckHealth() {
        return JsonResult.success();
    }

}
