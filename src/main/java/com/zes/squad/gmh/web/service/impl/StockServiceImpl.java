package com.zes.squad.gmh.web.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.zes.squad.gmh.common.converter.CommonConverter;
import com.zes.squad.gmh.web.context.ThreadContext;
import com.zes.squad.gmh.web.entity.dto.StaffDto;
import com.zes.squad.gmh.web.entity.dto.StockDto;
import com.zes.squad.gmh.web.entity.po.StockPo;
import com.zes.squad.gmh.web.entity.vo.StockVo;
import com.zes.squad.gmh.web.mapper.StockMapper;
import com.zes.squad.gmh.web.service.StockService;

@Service("stockService")
public class StockServiceImpl implements StockService{
	@Autowired
	private StockMapper stockMapper;
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
}
