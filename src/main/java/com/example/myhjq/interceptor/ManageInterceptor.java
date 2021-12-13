package com.example.myhjq.interceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import java.io.PrintWriter;

@Component
public class ManageInterceptor implements HandlerInterceptor {
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
            throws Exception {
        try {
            HttpSession session = request.getSession();
            if (session.getAttribute("permission") != null && session.getAttribute("permission").equals("m")) {
                return true;
            }
            response.setHeader("content-type", "text/html;charset=utf-8");
            PrintWriter pw = response.getWriter();
            pw.write("<script>" +
                    "alert('请先登录管理员再进行此操作！');" +
                    "location.replace('/login');" +
                    "</script>");
//            response.sendRedirect("/login");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }
    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
    }
    @Override
    public void afterCompletion
            (HttpServletRequest request, HttpServletResponse response, Object handler, Exception exception) throws Exception {
    }
}


