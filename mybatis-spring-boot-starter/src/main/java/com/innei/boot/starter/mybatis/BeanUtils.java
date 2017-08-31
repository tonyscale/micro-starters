package com.innei.boot.starter.mybatis;

import java.lang.reflect.Field;
import java.util.Properties;

/**
 * Created by LIUMI969 on 2017/4/14.
 *
 */
public class BeanUtils {



    public static Properties beanToProperties(Object obj) throws IllegalAccessException {

        Class<?> clazz = obj.getClass();
        Field[] declaredFields = clazz.getDeclaredFields();
        Properties properties = new Properties();

        for (Field declaredField : declaredFields) {
            declaredField.setAccessible(true);
            Object o = declaredField.get(obj);
            if(null != o){
                properties.setProperty(declaredField.getName(),o.toString());
            }


        }

        return properties;


    }

}
