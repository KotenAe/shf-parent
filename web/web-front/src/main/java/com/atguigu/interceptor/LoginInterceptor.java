package com.atguigu.interceptor;

import com.atguigu.entity.UserInfo;
import com.atguigu.result.Result;
import com.atguigu.result.ResultCodeEnum;
import com.atguigu.util.WebUtil;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

//定义拦截器实现 HandlerInterceptor接口，点开HandlerInterceptor，里面有三个固定方法,ctrl+o快速重写
public class LoginInterceptor implements HandlerInterceptor {
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        //获取session域里面的UserInfo对象
        UserInfo userInfo = (UserInfo) request.getSession().getAttribute("USER");
        //做判断
        if (userInfo == null) {
            //证明还没有登录,    LOGIN_AUTH(208, "未登陆"),
            Result<String> result = Result.build("还没有登录", ResultCodeEnum.LOGIN_AUTH);
            //使用工具类,将result对象响应到前端
            WebUtil.writeJSON(response, result);
            return false;
        } else { //登录了，就放行
            return true;
        }
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
        HandlerInterceptor.super.postHandle(request, response, handler, modelAndView);
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        HandlerInterceptor.super.afterCompletion(request, response, handler, ex);
    }
}
