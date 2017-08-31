package com.innei.boot.starter.redis;

import org.springframework.beans.factory.config.BeanDefinitionHolder;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.context.annotation.ClassPathBeanDefinitionScanner;

import java.util.Set;

/**
 * Created by LIUMI969 on 2017/5/18.
 */
public class RedisExpireScanner extends ClassPathBeanDefinitionScanner {

    public RedisExpireScanner(BeanDefinitionRegistry registry) {
        super(registry);
        addExcludeFilter(new CacheAndExpireFilter(getRegistry()));
    }



    public Set<BeanDefinitionHolder> doScan(String... basePackages) {
        return super.doScan(basePackages);
    }

}
