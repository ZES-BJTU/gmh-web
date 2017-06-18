package com.zes.squad.gmh.web.aop;

import java.util.Arrays;

import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;

import com.zes.squad.gmh.common.exception.ErrorCodeEnum;
import com.zes.squad.gmh.common.exception.GmhException;
import com.zes.squad.gmh.web.common.JsonResult;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class ControllerResultInterceptor implements MethodInterceptor {

    @Override
    public Object invoke(MethodInvocation invocation) throws Throwable {
        try {
            Object obj = invocation.proceed();
            return obj;
        } catch (GmhException e) {
            log.error("api is {}, args is {}, exception is ", getApiInfo(invocation),
                    Arrays.asList(invocation.getArguments()), e);
            return JsonResult.fail(e.getCode(), e.getError());
        } catch (Exception e) {
            log.error("api is {}, args is {}, exception is ", getApiInfo(invocation),
                    Arrays.asList(invocation.getArguments()), e);
            return JsonResult.fail(ErrorCodeEnum.BUSINESS_EXCEPTION.getCode(), "服务不可用");
        }
    }

    public String getApiInfo(MethodInvocation invocation) {
        StringBuilder builder = new StringBuilder();
        builder.append(invocation.getClass().getName());
        builder.append("#");
        builder.append(invocation.getMethod().getName());
        return builder.toString();
    }

}
