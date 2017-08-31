package com.innei.boot.starter.redis;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.BeanFactoryAware;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.boot.autoconfigure.AutoConfigurationPackages;
import org.springframework.context.ResourceLoaderAware;
import org.springframework.context.annotation.ImportBeanDefinitionRegistrar;
import org.springframework.core.io.ResourceLoader;
import org.springframework.core.type.AnnotationMetadata;

import java.util.List;

/**
 * Created by LIUMI969 on 2017/5/19.
 */
public class RedisKeyExpireScannerRegistrar implements ImportBeanDefinitionRegistrar,BeanFactoryAware,ResourceLoaderAware {

    private BeanFactory beanFactory;

    private ResourceLoader resourceLoader;

    @Override
    public void registerBeanDefinitions(AnnotationMetadata importingClassMetadata, BeanDefinitionRegistry registry) {

        RedisExpireScanner scanner = new RedisExpireScanner(registry);
        if (this.resourceLoader != null) {
            scanner.setResourceLoader(this.resourceLoader);
        }

        List<String> packages = AutoConfigurationPackages.get(this.beanFactory);
        scanner.doScan(packages.get(0));

    }

    @Override
    public void setBeanFactory(BeanFactory beanFactory) throws BeansException {
        this.beanFactory = beanFactory;
    }

    @Override
    public void setResourceLoader(ResourceLoader resourceLoader) {
        this.resourceLoader = resourceLoader;
    }
}
