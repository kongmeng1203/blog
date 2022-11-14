package com.gzx.blog.controller;


import com.gzx.blog.service.SysUserService;
import com.gzx.blog.vo.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("users")
public class UsersController {

    @Autowired
    private SysUserService sysUserService;

    @GetMapping("currentUser")
    public Result currentUser(@RequestHeader("Authorization") String token){
        Result user = sysUserService.findUserByToken(token);
        return user;
    }
}
