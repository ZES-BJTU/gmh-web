package com.zes.squad.gmh.web.context;

import com.zes.squad.gmh.common.exception.ErrorCodeEnum;
import com.zes.squad.gmh.common.exception.GmhException;
import com.zes.squad.gmh.web.entity.dto.StaffDto;

public class ThreadContext {

    public static ThreadLocal<StaffDto> threadLocal = new ThreadLocal<StaffDto>() {

        @Override
        public StaffDto initialValue() {
            return new StaffDto();
        }

    };

    public static StaffDto getCurrentStaff() {
        StaffDto staff = threadLocal.get();
        if (staff == null) {
            throw new GmhException(ErrorCodeEnum.BUSINESS_EXCEPTION_ENTITY_NOT_FOUND, "获取用户信息失败");
        }
        return staff;
    }

}
