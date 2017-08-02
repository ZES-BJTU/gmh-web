package com.zes.squad.gmh.web.interceptor;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import com.google.common.base.Strings;
import com.zes.squad.gmh.common.constant.RequestConsts;
import com.zes.squad.gmh.common.exception.ErrorCodeEnum;
import com.zes.squad.gmh.common.util.JsonUtils;
import com.zes.squad.gmh.web.common.JsonResult;
import com.zes.squad.gmh.web.context.ThreadContext;
import com.zes.squad.gmh.web.entity.dto.StaffDto;
import com.zes.squad.gmh.web.service.StaffService;

import lombok.Cleanup;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class AuthInterceptor extends HandlerInterceptorAdapter {

    @Autowired
    private StaffService staffService;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
            throws Exception {
        try {
            String token = request.getHeader(RequestConsts.X_TOKEN);
            if (Strings.isNullOrEmpty(token)) {
                log.error("获取用户token失败, url is {}, token is {}.", request.getRequestURI(), token);
                sendJsonResponse(response, JsonResult.fail(ErrorCodeEnum.WEB_EXCEPTION_AUTH_FAIL.getCode(), "请先登录"));
                return false;
            }
            StaffDto staff = staffService.queryStaffByToken(token);
            ThreadContext.threadLocal.set(staff);
            return true;
        } catch (Exception e) {
            sendJsonResponse(response, JsonResult.fail(ErrorCodeEnum.WEB_EXCEPTION_AUTH_FAIL.getCode(), "登录已过期，请重新登录"));
            return false;
        }
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler,
                           ModelAndView modelAndView)
            throws Exception {
        ThreadContext.threadLocal.remove();
    }

    private void sendJsonResponse(HttpServletResponse response, Object obj) {
        response.setCharacterEncoding("UTF-8");
        response.setContentType("application/json; charset=utf-8");
        try {
            @Cleanup
            PrintWriter out = response.getWriter();
            out.write(JsonUtils.toJson(obj));
            out.flush();
        } catch (IOException e) {
            log.error("生成json返回数据失败, response is {}", JsonUtils.toJson(obj), e);
        }
    }

}
