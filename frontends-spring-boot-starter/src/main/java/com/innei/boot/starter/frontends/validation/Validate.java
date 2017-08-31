package com.innei.boot.starter.frontends.validation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Created by LIUMI969 on 2016-11-23.
 *
 */

@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.PARAMETER,ElementType.FIELD})
public @interface Validate {


    /**
     * 一般对象不为Null,String不为empty
     *
     */
    boolean notEmpty() default true;

    /**
     * 进行参数的正则表达式匹配
     *
     */
    String regexp() default "";


}
