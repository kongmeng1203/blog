package com.gzx.blog.handler;

import com.alibaba.fastjson.JSON;
import com.gzx.blog.pojo.SysUser;
import com.gzx.blog.service.LoginService;
import com.gzx.blog.utils.UserThreadLocal;
import com.gzx.blog.vo.ErrorCode;
import com.gzx.blog.vo.Result;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


@Component
@Slf4j
public class LoginInterceptor implements HandlerInterceptor {

    @Autowired
    private LoginService loginService;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {

        if (!(handler instanceof HandlerMethod))
        {
            return true;
        }
        String token = request.getHeader("Oauth-Token");


        log.info("=================request start===========================");
        String requestURI = request.getRequestURI();
        log.info("request uri:{}",requestURI);
        log.info("request method:{}",request.getMethod());
        log.info("token:{}", token);
        log.info("=================request end===========================");


        if (StringUtils.isBlank(token)){
            Result fail = Result.fail(ErrorCode.NO_LOGIN.getCode(), ErrorCode.NO_LOGIN.getMsg());
            response.setContentType("application/json;charset=utf-8");
            response.getWriter().print(JSON.toJSONString(fail));
            return false;
        }
        SysUser sysUser = loginService.checkToken(token);
        if (sysUser == null){
            Result fail = Result.fail(ErrorCode.NO_LOGIN.getCode(), ErrorCode.NO_LOGIN.getMsg());
            response.setContentType("application/json;charset=utf-8");
            response.getWriter().print(JSON.toJSONString(fail));
            return false;
        }

        UserThreadLocal.put(sysUser);

    //验证成功，放行
        return true;
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        //如果不删除将会有内存泄漏的风险
        SysUser sysUser = UserThreadLocal.get();
        System.out.println("==============LoginInterceptor前================");
        System.out.println("test :"+sysUser);
        System.out.println("=====================================");
        UserThreadLocal.remove();
        SysUser sysUser1 = UserThreadLocal.get();
        System.out.println("==============LoginInterceptor后================");
        System.out.println("test :"+sysUser1);
        System.out.println("=====================================");
    }
}
