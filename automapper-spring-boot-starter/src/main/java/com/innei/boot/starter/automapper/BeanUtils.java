package com.innei.boot.starter.automapper;

import lombok.NonNull;
import tk.mybatis.mapper.entity.Config;

import java.lang.reflect.Field;

/**
 * Created by LIUMI969 on 2017/4/14.
 *
 */
public class BeanUtils {


    public static void copyProperties(@NonNull Object src, @NonNull Object dest, boolean ignoreCase) throws NoSuchFieldException, IllegalAccessException {

        Class<?> srcClazz = src.getClass();
        Class<?> destClazz = dest.getClass();

        Field[] fields = srcClazz.getDeclaredFields();
        for (Field field : fields) {
            String name = field.getName();
            Field destField = getField(destClazz, name, ignoreCase);
            if(null != destField){
                destField.setAccessible(true);
                field.setAccessible(true);
                destField.set(dest,field.get(src));
            }
        }
    }

    public static Field getField(@NonNull Class<?> clazz, @NonNull String fieldName, boolean ignoreCase) throws NoSuchFieldException {


        if(!ignoreCase){
            return clazz.getDeclaredField(fieldName);
        }

        Field[] fields = clazz.getDeclaredFields();

        for (Field field : fields) {
            String name = field.getName();
            boolean match = name.equalsIgnoreCase(fieldName);
            if(match){
                return field;
            }
        }
        return null;

    }

    public static void main(String[] args) throws Exception{

        MapperProperties mapperProperties =  new MapperProperties();
        mapperProperties.setBefore(true);
        mapperProperties.setCatalog("aaa");
        mapperProperties.setIdentity("ccc");

        Config config = new Config();

        copyProperties(mapperProperties,config,true);

        System.out.println(config);


    }


}
