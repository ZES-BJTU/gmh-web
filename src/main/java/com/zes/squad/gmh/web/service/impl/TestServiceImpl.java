package com.zes.squad.gmh.web.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.zes.squad.gmh.web.mapper.StaffMapper;
import com.zes.squad.gmh.web.service.TestService;

@Service("testService")
public class TestServiceImpl implements TestService {
    
    @Autowired
    private StaffMapper staffMapper;
    
    public void test(){
        staffMapper.insert(null);
    }

}
