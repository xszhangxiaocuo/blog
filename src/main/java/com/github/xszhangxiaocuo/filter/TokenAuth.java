//package com.github.xszhangxiaocuo.filter;
//
//import com.fasterxml.jackson.core.JsonToken;
//import com.github.xszhangxiaocuo.dao.UserInfoDao;
//import com.github.xszhangxiaocuo.entity.Err.ErrCode;
//import com.github.xszhangxiaocuo.entity.Err.ErrMessage;
//import com.github.xszhangxiaocuo.entity.Result;
//import com.github.xszhangxiaocuo.utils.JsonUtil;
//import com.github.xszhangxiaocuo.utils.JwtUtil;
//import io.jsonwebtoken.Claims;
//import io.jsonwebtoken.ExpiredJwtException;
//import io.jsonwebtoken.MalformedJwtException;
//import io.jsonwebtoken.security.SignatureException;
//
//import javax.servlet.*;
//import javax.servlet.annotation.*;
//import javax.servlet.http.HttpServletRequest;
//import javax.servlet.http.HttpServletResponse;
//import java.io.IOException;
//
//@WebFilter(filterName = "TokenAuth",urlPatterns = "/*")
//public class TokenAuth implements Filter {
//    public void init(FilterConfig config) throws ServletException {
//    }
//
//    public void destroy() {
//    }
//
//    @Override
//    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws ServletException, IOException {
//        response.setContentType("application/json");
//        response.setCharacterEncoding("UTF-8");
//        HttpServletRequest req = (HttpServletRequest)request;
//        HttpServletResponse resp = (HttpServletResponse) response;
//
//        String path = req.getRequestURI();
//        if (path.equals("/blog/login")||path.equals("/blog/logout")||path.startsWith("/blog/articles")){
//            chain.doFilter(req,resp);
//            return;
//        }
//
//        int CODE = ErrCode.OK.getCode();
//        Result result = new Result();
//
//        String token = req.getHeader("Authorization");
//        System.out.println("Authorization:"+token);
//
//        try {
//                Claims claims = JwtUtil.getClaims(token, 10);
//                Integer userId = (Integer) claims.get("userId");
//                System.out.println("userId: " + userId);
//
//                CODE = ErrCode.OK.getCode();
//
//        } catch (ExpiredJwtException e) {
//            CODE = ErrCode.ERROR_TOKEN_RUNTIME.getCode();
//        } catch (SignatureException e) {
//            CODE = ErrCode.ERROR_TOKEN_WRONG.getCode();
//        } catch (MalformedJwtException e) {
//            CODE = ErrCode.ERROR_TOKEN_NOT_EXIST.getCode();
//        } catch (Exception e) {
//            CODE = ErrCode.ERROR_TOKEN_PARSE.getCode();
//        }finally {
//            if (CODE!=ErrCode.OK.getCode()){
//                resp.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
//                result.failure(CODE,ErrMessage.getMsg(CODE));
//                JsonUtil.returnJSON(resp,JsonUtil.beanToJson(result));
//                return;
//            }
//            chain.doFilter(req, resp);
//        }
//    }
//}
