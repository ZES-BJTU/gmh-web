package com.zes.squad.gmh.web.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.zes.squad.gmh.common.converter.CommonConverter;
import com.zes.squad.gmh.common.entity.PagedList;
import com.zes.squad.gmh.web.context.ThreadContext;
import com.zes.squad.gmh.web.entity.dto.ProjectTypeDto;
import com.zes.squad.gmh.web.entity.dto.StaffDto;
import com.zes.squad.gmh.web.entity.dto.StockTypeDto;
import com.zes.squad.gmh.web.entity.po.ProjectTypePo;
import com.zes.squad.gmh.web.entity.po.StockTypePo;
import com.zes.squad.gmh.web.entity.vo.ProjectTypeVo;
import com.zes.squad.gmh.web.mapper.ProjectTypeMapper;
import com.zes.squad.gmh.web.service.ProjectTypeService;
@Service("projectTypeService")
public class ProjectTypeServiceImpl implements ProjectTypeService{

	@Autowired
	private ProjectTypeMapper ptMapper;
	
	public List<ProjectTypeVo> getAll(){
		List<ProjectTypePo> poList = new ArrayList<ProjectTypePo>();
		StaffDto staffDto = ThreadContext.getCurrentStaff();
		poList = ptMapper.getAll(staffDto.getStoreId());
		List<ProjectTypeVo> voList = new ArrayList<ProjectTypeVo>();
		if(poList.size()==0)
			return null;
		for(int i=0;i<poList.size();i++){
			voList.add(CommonConverter.map(poList.get(i),ProjectTypeVo.class));
		}
		return voList;
	}
	public int insert(ProjectTypeDto dto){
		ProjectTypePo po = CommonConverter.map(dto, ProjectTypePo.class);
		int i = 0;
		i = ptMapper.insert(po);
		return i;
	}
	public int update(ProjectTypeDto dto){
		ProjectTypePo po = CommonConverter.map(dto, ProjectTypePo.class);
		int i = 0;
		i = ptMapper.update(po);
		return i;
	}
	public int delByIds(Long[] id){
		int i = 0;
		for(int j=0;j<id.length;j++){
			i = i + ptMapper.delById(id[j]);
		}
		return i;
		
	}
	@Override
	public List<ProjectTypeVo> getByTopType(int topType) {
		List<ProjectTypePo> poList = new ArrayList<ProjectTypePo>();
		StaffDto staffDto = ThreadContext.getCurrentStaff();
		Map map = new HashMap();
		map.put("topType", topType);
		map.put("storeId", staffDto.getStoreId());
		poList = ptMapper.getByTopType(map);
		List<ProjectTypeVo> voList = new ArrayList<ProjectTypeVo>();
		if(poList.size()==0)
			return null;
		for(int i=0;i<poList.size();i++){
			voList.add(CommonConverter.map(poList.get(i),ProjectTypeVo.class));
		}
		return voList;
	}
	@Override
	public PagedList<ProjectTypeDto> searchListByPage(Integer pageNum, Integer pageSize,Long topType, String searchString) {
		PageHelper.startPage(pageNum, pageSize);
		StaffDto staffDto = ThreadContext.getCurrentStaff();
		List<ProjectTypePo> projectTypePos = new ArrayList<ProjectTypePo>();
		Map map = new HashMap();
		map.put("storeId", staffDto.getStoreId());
		map.put("searchString", searchString);
		if(topType==null||topType==0L){
			projectTypePos = ptMapper.search(map);
		}else{
			map.put("topType", topType);
			projectTypePos = ptMapper.searchWithTop(map);
		}
        
        PageInfo<ProjectTypePo> info = new PageInfo<>(projectTypePos);
        PagedList<ProjectTypeDto> pagedList = CommonConverter.mapPageList(
                PagedList.newMe(info.getPageNum(), info.getPageSize(), info.getTotal(), projectTypePos),
                ProjectTypeDto.class);
        return pagedList;
	}
}
