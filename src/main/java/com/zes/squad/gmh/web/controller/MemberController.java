package com.zes.squad.gmh.web.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.zes.squad.gmh.web.common.JsonResult;
import com.zes.squad.gmh.web.entity.vo.MemberVo;
import com.zes.squad.gmh.web.service.MemberService;

@RequestMapping("/member")
@Controller
public class MemberController {
	@Autowired
	private MemberService memberService;
	
	@RequestMapping("/getAll")
	@ResponseBody
	public JsonResult<?> getAll(){
		List<MemberVo> voList = new ArrayList<MemberVo>();
		voList = memberService.getAll();		
		return JsonResult.success(voList);
	}
}
