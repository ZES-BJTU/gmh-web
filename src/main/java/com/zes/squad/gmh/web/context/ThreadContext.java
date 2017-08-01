package com.zes.squad.gmh.web.context;

import com.zes.squad.gmh.common.enums.StaffLevelEnum;
import com.zes.squad.gmh.common.exception.ErrorCodeEnum;
import com.zes.squad.gmh.common.exception.ErrorMessage;
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
            throw new GmhException(ErrorCodeEnum.BUSINESS_EXCEPTION_ENTITY_NOT_FOUND, ErrorMessage.staffIsNull);
        }
        return staff;
    }

    public static Long getStaffStoreId() {
        StaffDto staff = getCurrentStaff();
        if (staff.getStaffLevel() == StaffLevelEnum.ADMINISTRATOR.getKey()) {
            throw new GmhException(ErrorCodeEnum.BUSINESS_EXCEPTION_OPERATION_NOT_ALLOWED,
                    ErrorMessage.administratorBelongsToNoStore);
        }
        if (staff.getStaffLevel() != StaffLevelEnum.ADMINISTRATOR.getKey() && staff.getStoreId() == null) {
            throw new GmhException(ErrorCodeEnum.BUSINESS_EXCEPTION_ILLEGAL_STATUS, ErrorMessage.storeIsNull);
        }
        return staff.getStoreId();
    }

}
