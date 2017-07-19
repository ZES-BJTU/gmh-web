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
import com.zes.squad.gmh.web.entity.dto.ShopDto;
import com.zes.squad.gmh.web.entity.dto.StaffDto;
import com.zes.squad.gmh.web.entity.po.EmployeePo;
import com.zes.squad.gmh.web.entity.po.ShopPo;
import com.zes.squad.gmh.web.entity.vo.ShopVo;
import com.zes.squad.gmh.web.mapper.ShopMapper;
import com.zes.squad.gmh.web.service.ShopService;

@Service("shopService")
public class ShopServiceImpl implements ShopService{

	@Autowired
    private ShopMapper shopMapper;
	
	public int insert(ShopDto dto){	
    	ShopPo po = CommonConverter.map(dto, ShopPo.class);
    	int i = shopMapper.insert(po);
    	return i;
    }
	
	public int update(ShopDto dto){	
    	ShopPo po = CommonConverter.map(dto, ShopPo.class);
    	int i = shopMapper.update(po);
    	return i;
    }
	
	public List<ShopVo> getAll(){

		List<ShopPo> poList = shopMapper.getAll();
		List<ShopVo> voList = new ArrayList<ShopVo>();
		if(poList.size()==0)
			return null;
		for(int i=0;i<poList.size();i++){
			voList.add(CommonConverter.map(poList.get(i), ShopVo.class));
		}
		return voList;
	}
	public int delByIds(Long[] id){
		int i = 0;
		for(int j=0;j<id.length;j++){
			int x = shopMapper.delById(id[j]);
			i = i + x;
		}
		return i;
	}
	
	@Override
    public PagedList<ShopDto> listByPage(Integer pageNum, Integer pageSize) {
        PageHelper.startPage(pageNum, pageSize);
        List<ShopPo> shopPos = shopMapper.getAll();
        PageInfo<ShopPo> info = new PageInfo<>(shopPos);
        PagedList<ShopDto> pagedList = CommonConverter.mapPageList(
                PagedList.newMe(info.getPageNum(), info.getPageSize(), info.getTotal(), shopPos),
                ShopDto.class);
        return pagedList;
    }

	@Override
	public PagedList<ShopDto> searchListByPage(Integer pageNum, Integer pageSize, String searchString) {
		PageHelper.startPage(pageNum, pageSize);
        List<ShopPo> shopPos = shopMapper.search(searchString);
        PageInfo<ShopPo> info = new PageInfo<>(shopPos);
        PagedList<ShopDto> pagedList = CommonConverter.mapPageList(
                PagedList.newMe(info.getPageNum(), info.getPageSize(), info.getTotal(), shopPos),
                ShopDto.class);
        return pagedList;
	}
}
