package com.innei.boot.starter.redis;

import java.lang.annotation.*;

/**
 * Created by LIUMI969 on 2017/5/18.
 *
 */
@Target({ElementType.METHOD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Inherited
@Documented
public @interface CacheExpire {

     int DEFAULT_EXPIRE_TIME_SECONDS = 3600;

    /**
     * 单位为Seconds
     * @return
     */
    int expireTime() default DEFAULT_EXPIRE_TIME_SECONDS;


}
