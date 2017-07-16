package com.zes.squad.gmh.web.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.zes.squad.gmh.web.common.JsonResult;
import com.zes.squad.gmh.web.entity.dto.MemberDto;
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
	
	@RequestMapping("/insert")
	@ResponseBody
	public JsonResult<?> insert(MemberDto dto){
		MemberVo vo = memberService.getByPhone(dto.getPhone());
		if(vo!=null){
			return JsonResult.fail(10006, "该手机号已注册");
		}
		int i = memberService.insert(dto);
		if(i>0)
			return JsonResult.success(i);
		else
			return JsonResult.fail(10006, "新增失败");	
	}
	@RequestMapping("/update")
	@ResponseBody
	public JsonResult<?> update(MemberDto dto){
		int i = memberService.update(dto);
		if(i>0)
			return JsonResult.success(i);
		else
			return JsonResult.fail(10006, "修改失败");
	}
	@RequestMapping("/delete")
	@ResponseBody
	public JsonResult<?> delete(Long[] id){
		int i = 0;
		i = memberService.delByIds(id);
		if(i>0)
			return JsonResult.success(i);
		else 
			return JsonResult.fail(10006, "发生错误，没有数据被修改");
	}
	@RequestMapping("/getByPhone")
	@ResponseBody
	public JsonResult<?> getByPhone(String phone){
		MemberVo vo = memberService.getByPhone(phone);
		if(vo==null)
			return JsonResult.fail(10002, "没有该会员");
		else
			return JsonResult.success(vo);
	}
}
