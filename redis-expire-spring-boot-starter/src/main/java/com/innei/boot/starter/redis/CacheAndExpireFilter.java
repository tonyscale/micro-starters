package com.innei.boot.starter.redis;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.core.type.classreading.MetadataReader;
import org.springframework.core.type.classreading.MetadataReaderFactory;
import org.springframework.core.type.filter.TypeFilter;
import org.springframework.util.ClassUtils;

import java.io.IOException;
import java.lang.annotation.Annotation;
import java.lang.reflect.AnnotatedElement;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * Created by LIUMI969 on 2017/5/18.
 *
 */
@Slf4j
public class CacheAndExpireFilter implements TypeFilter {

    private RedisKeysExpire redisKeysExpire;

    CacheAndExpireFilter(BeanDefinitionRegistry registry) {

        DefaultListableBeanFactory defaultRegistry = (DefaultListableBeanFactory) registry;
        redisKeysExpire = new RedisKeysExpire();

        defaultRegistry.registerSingleton("RedisKeysExpire",redisKeysExpire);
    }


    @Override
    public boolean match(MetadataReader metadataReader, MetadataReaderFactory metadataReaderFactory) throws IOException {

        String className = metadataReader.getClassMetadata().getClassName();
        try {

            handle(className, Cacheable.class,redisKeysExpire);
            handle(className, CacheConfig.class,redisKeysExpire);
            handle(className, CachePut.class,redisKeysExpire);

        } catch (Exception e) {
            log.error("Register RedisKeysExpire occur exception.",e);
        }


        return false;
    }


    private void handle(String className,Class<? extends Annotation> annotationType,RedisKeysExpire redisKeysExpire) throws ClassNotFoundException, NoSuchFieldException, NoSuchMethodException, InvocationTargetException, IllegalAccessException {

        Class<?> clazz = ClassUtils.forName(className, getClass().getClassLoader());
        Annotation cacheAnn = AnnotationUtils.getAnnotation(clazz, annotationType);
        if(null != cacheAnn){
            addKeyExpire(clazz,cacheAnn,redisKeysExpire);

        }else {
            Method[] declaredMethods = clazz.getDeclaredMethods();
            if(null != declaredMethods && declaredMethods.length > 0){
                for (Method method : declaredMethods) {
                    Annotation cacheAnnMethod = AnnotationUtils.getAnnotation(method, annotationType);
                    if(null != cacheAnnMethod){
                        addKeyExpire(method,cacheAnnMethod,redisKeysExpire);
                    }
                }
            }
        }

    }




    private void addKeyExpire(AnnotatedElement annotatedElement, Annotation cacheAnn, RedisKeysExpire redisKeysExpire) throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {

        Class<? extends Annotation> cacheAnnClazz = cacheAnn.annotationType();
        Method value = cacheAnnClazz.getDeclaredMethod("value");
        String[] cacheNames = (String[]) value.invoke(cacheAnn);
        if((null != cacheNames) && (cacheNames.length > 0)){
            CacheExpire cacheExpire = AnnotationUtils.getAnnotation(annotatedElement, CacheExpire.class);
            if(null != cacheExpire){
                long expireTime = cacheExpire.expireTime();
                //设置默认过期时间
                if(expireTime <= 0){
                    expireTime = CacheExpire.DEFAULT_EXPIRE_TIME_SECONDS;
                }
                for (String cacheName : cacheNames) {
                    redisKeysExpire.addKeyExpire(cacheName,expireTime);
                }

            }

        }
    }


}
