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
import com.zes.squad.gmh.web.entity.dto.StaffDto;
import com.zes.squad.gmh.web.entity.dto.StockTypeDto;
import com.zes.squad.gmh.web.entity.po.StockTypePo;
import com.zes.squad.gmh.web.entity.vo.StockTypeVo;
import com.zes.squad.gmh.web.mapper.StockTypeMapper;
import com.zes.squad.gmh.web.service.StockTypeService;

@Service("stockTypeService")
public class StockTypeServiceImpl implements StockTypeService{
	@Autowired
	private StockTypeMapper stMapper;
	
	public List<StockTypeVo> getAll(){
		List<StockTypePo> poList = new ArrayList<StockTypePo>();
		StaffDto staffDto = ThreadContext.getCurrentStaff();
		poList = stMapper.getAll(staffDto.getStoreId());
		List<StockTypeVo> voList = new ArrayList<StockTypeVo>();
		if(poList.size()==0)
			return null;
		for(int i=0;i<poList.size();i++){
			voList.add(CommonConverter.map(poList.get(i),StockTypeVo.class));
		}
		return voList;
	}
	public int insert(StockTypeDto dto){
		StockTypePo po = CommonConverter.map(dto, StockTypePo.class);
		int i = 0;
		i = stMapper.insert(po);
		return i;
	}
	public int update(StockTypeDto dto){
		StockTypePo po = CommonConverter.map(dto, StockTypePo.class);
		int i = 0;
		i = stMapper.update(po);
		return i;
	}
	public int delByIds(Long[] id){
		int i = 0;
		for(int j=0;j<id.length;j++){
			i = i + stMapper.delById(id[j]);
		}
		return i;
		
	}
	@Override
	public PagedList<StockTypeDto> searchListByPage(Integer pageNum, Integer pageSize, String searchString) {
		PageHelper.startPage(pageNum, pageSize);
		StaffDto staffDto = ThreadContext.getCurrentStaff();
		Map map = new HashMap();
		map.put("storeId", staffDto.getStoreId());
		map.put("searchString", searchString);
        List<StockTypePo> stockTypePos = stMapper.search(map);
        PageInfo<StockTypePo> info = new PageInfo<>(stockTypePos);
        PagedList<StockTypeDto> pagedList = CommonConverter.mapPageList(
                PagedList.newMe(info.getPageNum(), info.getPageSize(), info.getTotal(), stockTypePos),
                StockTypeDto.class);
        return pagedList;
	}
}
