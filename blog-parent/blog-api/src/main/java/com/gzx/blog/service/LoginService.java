package com.gzx.blog.service;

import com.gzx.blog.pojo.SysUser;
import com.gzx.blog.vo.Result;
import com.gzx.blog.vo.params.LoginParams;

public interface LoginService {
    Result login(LoginParams loginParams);

    SysUser checkToken(String token);

    Result logout(String token);

    Result register(LoginParams loginParams);
}
