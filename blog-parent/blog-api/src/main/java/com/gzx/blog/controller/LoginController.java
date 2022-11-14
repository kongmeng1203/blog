package com.gzx.blog.controller;

import com.alibaba.fastjson.JSON;
import com.gzx.blog.pojo.SysUser;
import com.gzx.blog.service.LoginService;
import com.gzx.blog.service.SysUserService;
import com.gzx.blog.utils.JWTUtils;
import com.gzx.blog.vo.ErrorCode;
import com.gzx.blog.vo.Result;
import com.gzx.blog.vo.params.LoginParams;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.TimeoutUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.concurrent.TimeUnit;

@RestController
@RequestMapping("login")
public class LoginController {



    @Autowired
    private LoginService loginService;





    @PostMapping
    public Result login(@RequestBody LoginParams loginParams){

       Result result = loginService.login(loginParams);

       return  result;

    }
}
