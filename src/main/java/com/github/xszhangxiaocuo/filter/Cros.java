package com.github.xszhangxiaocuo.filter;

import javax.servlet.*;
import javax.servlet.annotation.*;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/*
允许跨域请求
 */
@WebFilter(filterName = "cros",urlPatterns = "/*")
public class Cros implements Filter {
    public void init(FilterConfig config) throws ServletException {
    }

    public void destroy() {
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws ServletException, IOException {
        HttpServletResponse res = (HttpServletResponse)response;
        res.setHeader("Access-Control-Allow-Origin", "*"); // 允许所有源
        res.setHeader("Access-Control-Allow-Methods", "GET, POST, PUT, DELETE, OPTIONS");
        res.setHeader("Access-Control-Allow-Headers", "Content-Type");
        chain.doFilter(request, res);
    }
}
