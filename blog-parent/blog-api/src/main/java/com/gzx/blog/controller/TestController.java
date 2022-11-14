package com.gzx.blog.controller;


import com.gzx.blog.pojo.SysUser;
import com.gzx.blog.utils.UserThreadLocal;
import com.gzx.blog.vo.Result;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("test")
public class TestController {

    @RequestMapping
    public Result test(){
        SysUser sysUser = UserThreadLocal.get();
        System.out.println("=============TestController=================");
        System.out.println("test :"+sysUser);
        System.out.println("=====================================");

        return Result.success(null);
    }
}

