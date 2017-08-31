package com.innei.boot.starter.frontends.param;

import java.lang.annotation.*;

/**
 * Created by LIUMI969 on 2017/5/11.
 */
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface ReqHeader {

    /**
     * Alias for requestHeader to adjust model
     */

    String value() default "";
}
