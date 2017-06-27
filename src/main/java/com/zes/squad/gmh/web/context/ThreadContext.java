package com.zes.squad.gmh.web.context;

import com.zes.squad.gmh.web.entity.dto.StaffDto;

public class ThreadContext {

    public static ThreadLocal<StaffDto> threadLocal = new ThreadLocal<StaffDto>() {

        @Override
        public StaffDto initialValue() {
            return new StaffDto();
        }

    };

    public static StaffDto getCurrentStaff() {
        return threadLocal.get();
    }

}
