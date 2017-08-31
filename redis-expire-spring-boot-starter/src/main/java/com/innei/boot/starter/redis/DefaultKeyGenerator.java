package com.innei.boot.starter.redis;

import org.springframework.cache.interceptor.KeyGenerator;

import java.lang.reflect.Method;

/**
 * Created by LIUMI969 on 2017/6/8.
 */
public class DefaultKeyGenerator implements KeyGenerator {


    @Override
    public Object generate(Object target, Method method, Object... params) {

        return method.getName();
    }
}
