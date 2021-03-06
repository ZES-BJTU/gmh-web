package com.zes.squad.gmh.web.filter;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletResponse;

public class CrossDomainFilter implements Filter {

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {

    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        HttpServletResponse hResponse = (HttpServletResponse) response;
        hResponse.addHeader("Access-Control-Allow-Origin", "*");
        hResponse.addHeader("Access-Control-Allow-Headers",
                "Content-Type, Authorization, Accept,X-Requested-With,X-token");
        chain.doFilter(request, hResponse);
    }

    @Override
    public void destroy() {

    }

}
