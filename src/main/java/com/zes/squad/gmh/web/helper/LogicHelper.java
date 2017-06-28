package com.zes.squad.gmh.web.helper;

import com.google.common.base.Strings;
import com.zes.squad.gmh.common.exception.ErrorCodeEnum;
import com.zes.squad.gmh.common.exception.GmhException;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class LogicHelper {

    public static void ensureParameterExist(Object parameter, String error) {
        if (parameter == null) {
            throw new GmhException(ErrorCodeEnum.BUSINESS_EXCEPTION_INVALID_PARAMETERS, error);
        }
        if (parameter instanceof String && Strings.isNullOrEmpty(parameter.toString())) {
            throw new GmhException(ErrorCodeEnum.BUSINESS_EXCEPTION_INVALID_PARAMETERS, error);
        }
    }

}
