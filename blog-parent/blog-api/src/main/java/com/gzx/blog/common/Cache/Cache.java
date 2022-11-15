package com.gzx.blog.common.Cache;

import java.lang.annotation.*;

//aop 定义了一个切面 ，切面定义了切点和通知的关系
@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Cache {

    long expire() default 1*60*1000;
    String name() default "";

}
