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
import com.zes.squad.gmh.web.entity.dto.StockDto;
import com.zes.squad.gmh.web.entity.po.StockPo;
import com.zes.squad.gmh.web.entity.po.StockTypePo;
import com.zes.squad.gmh.web.entity.vo.StockVo;
import com.zes.squad.gmh.web.mapper.StockMapper;
import com.zes.squad.gmh.web.mapper.StockTypeMapper;
import com.zes.squad.gmh.web.service.StockService;

@Service("stockService")
public class StockServiceImpl implements StockService{
	@Autowired
	private StockMapper stockMapper;
	@Autowired
	private StockTypeMapper stockTypeMapper;
	@Override
	public List<StockVo> getAll() {
		List<StockPo> poList = new ArrayList<StockPo>();
		List<StockVo> voList = new ArrayList<StockVo>();
		StaffDto staffDto = ThreadContext.getCurrentStaff();
		poList = stockMapper.getAll(staffDto.getStoreId());
		for(int i=0;i<poList.size();i++){
			voList.add(CommonConverter.map(poList.get(i),StockVo.class));
		}
		return voList;
	}

	@Override
	public List<StockVo> getBytype(Long typeId) {
		List<StockPo> poList = new ArrayList<StockPo>();
		List<StockVo> voList = new ArrayList<StockVo>();
		poList = stockMapper.getByType(typeId);
		for(int i=0;i<poList.size();i++){
			voList.add(CommonConverter.map(poList.get(i),StockVo.class));
		}
		return voList;
	}

	@Override
	public int insert(StockDto dto) {
		int i = 0;
		StockPo po = CommonConverter.map(dto, StockPo.class);
		i = stockMapper.insert(po);
		return i;
	}

	@Override
	public int update(StockDto dto) {
		int i = 0;
		StockPo po = CommonConverter.map(dto, StockPo.class);
		i = stockMapper.update(po);
		return i;
	}

	@Override
	public int delByIds(Long[] id) {
		int i = 0;
		for(int j=0;j<id.length;j++){
			i = i + stockMapper.delById(id[j]);
		}
		return 0;
	}
	@Override
	public PagedList<StockDto> searchListByPage(Integer pageNum, Integer pageSize,Long typeId, String searchString) {
		PageHelper.startPage(pageNum, pageSize);
		StaffDto staffDto = ThreadContext.getCurrentStaff();
		Map map = new HashMap();
		List<StockPo> stockPos = new ArrayList<StockPo>();
		List<StockTypePo> stockTypePos = stockTypeMapper.getAll(staffDto.getStoreId());
		List typeIds = new ArrayList();
		for(int i=0;i<stockTypePos.size();i++){
			typeIds.add(stockTypePos.get(i).getId());
		}
		map.put("searchString", searchString);
		if(typeId==null||typeId==0L){
			map.put("typeIds", typeIds);
			stockPos = stockMapper.searchWithoutType(map);
		}else{
			map.put("typeId", typeId);
			stockPos = stockMapper.search(map);
		}      
        PageInfo<StockPo> info = new PageInfo<>(stockPos);
        PagedList<StockDto> pagedList = CommonConverter.mapPageList(
                PagedList.newMe(info.getPageNum(), info.getPageSize(), info.getTotal(), stockPos),
                StockDto.class);
        return pagedList;
	}
}
