package com.zes.squad.gmh.web.context;

import com.zes.squad.gmh.web.entity.dto.StaffDto;

public class ThreadContext {

    public static ThreadLocal<StaffDto> threadLocalStaff = new ThreadLocal<StaffDto>() {

        @Override
        public StaffDto initialValue() {
            return new StaffDto();
        }

    };

    public StaffDto getCurrentStaff() {
        return threadLocalStaff.get();
    }

    public void unBind() {
        threadLocalStaff.remove();
    }

}
