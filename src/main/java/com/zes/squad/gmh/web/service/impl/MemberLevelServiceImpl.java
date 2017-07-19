package com.zes.squad.gmh.web.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.zes.squad.gmh.common.converter.CommonConverter;
import com.zes.squad.gmh.common.entity.PagedList;
import com.zes.squad.gmh.web.context.ThreadContext;
import com.zes.squad.gmh.web.entity.dto.EmployeeDto;
import com.zes.squad.gmh.web.entity.dto.MemberLevelDto;
import com.zes.squad.gmh.web.entity.dto.StaffDto;
import com.zes.squad.gmh.web.entity.po.EmployeePo;
import com.zes.squad.gmh.web.entity.po.MemberLevelPo;
import com.zes.squad.gmh.web.entity.vo.MemberLevelVo;
import com.zes.squad.gmh.web.mapper.MemberLevelMapper;
import com.zes.squad.gmh.web.service.MemberLevelService;
@Service("memberLevelService")
public class MemberLevelServiceImpl implements MemberLevelService{

	@Autowired
	private MemberLevelMapper mlMapper;
	
	public List<MemberLevelVo> getAll(){
		List<MemberLevelPo> poList = new ArrayList<MemberLevelPo>();
		StaffDto staffDto = ThreadContext.getCurrentStaff();
		poList = mlMapper.getAll(staffDto.getStoreId());
		List<MemberLevelVo> voList = new ArrayList<MemberLevelVo>();
		if(poList.size()==0)
			return null;
		for(int i=0;i<poList.size();i++){
			voList.add(CommonConverter.map(poList.get(i),MemberLevelVo.class));
		}
		return voList;
	}
	public int insert(MemberLevelDto dto){
		MemberLevelPo po = CommonConverter.map(dto, MemberLevelPo.class);
		int i = 0;
		i = mlMapper.insert(po);
		return i;
	}
	public int update(MemberLevelDto dto){
		MemberLevelPo po = CommonConverter.map(dto, MemberLevelPo.class);
		int i = 0;
		i = mlMapper.update(po);
		return i;
	}
	public int delByIds(Long[] id){
		int i = 0;
		for(int j=0;j<id.length;j++){
			i = i + mlMapper.delById(id[j]);
		}
		return i;
		
	}
	@Override
	public PagedList<MemberLevelDto> listByPage(Integer pageNum, Integer pageSize) {
		StaffDto staff = ThreadContext.getCurrentStaff();
        PageHelper.startPage(pageNum, pageSize);
        List<MemberLevelPo> memberLevelPos = mlMapper.getAll(staff.getStoreId());
        PageInfo<MemberLevelPo> info = new PageInfo<>(memberLevelPos);
        PagedList<MemberLevelDto> pagedList = CommonConverter.mapPageList(
                PagedList.newMe(info.getPageNum(), info.getPageSize(), info.getTotal(), memberLevelPos),
                MemberLevelDto.class);
        return pagedList;
	}
}
