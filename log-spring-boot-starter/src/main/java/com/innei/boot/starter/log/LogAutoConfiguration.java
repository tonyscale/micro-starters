package com.innei.boot.starter.log;


import com.innei.boot.starter.log.http.LogRestTemplateInterceptor;
import com.innei.boot.starter.log.server.LogConfig;
import com.innei.boot.starter.log.server.LoggerFilter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.web.client.RestTemplate;

import javax.servlet.DispatcherType;

/**
 * Created by LIUMI969 on 2017/6/28.
 */
@Configuration
@Slf4j
@EnableConfigurationProperties(LogConfig.class)
public class LogAutoConfiguration {

    @Autowired
    private LogConfig logConfig;

    /**
     * server中Filter的注入
     */
    @Bean(name = "AutoLoggerFilter")
    public FilterRegistrationBean filterRegistration() {

        log.info("start to LoggerFilter to the server");

        FilterRegistrationBean registration = new FilterRegistrationBean();
        registration.setFilter(new LoggerFilter(logConfig));
        registration.setOrder(Ordered.HIGHEST_PRECEDENCE);
        registration.setName("loggerFilter");
        registration.setDispatcherTypes(DispatcherType.REQUEST, DispatcherType.FORWARD);

        log.info("LoggerFilter named LoggerFilter was added FilterRegistrationBean");

        return registration;
    }


    /**
     * RestTemplate监控注入
     */
    @Configuration
    @ConditionalOnClass(RestTemplate.class)
    @ConditionalOnBean(RestTemplate.class)
    public class LogResetTemplateAutoConfiguration implements BeanPostProcessor {

        @Override
        public Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException {
            return bean;
        }

        @Override
        public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {

            if(bean instanceof RestTemplate){

                RestTemplate restTemplate = (RestTemplate) bean;

                restTemplate.getInterceptors()
                            .add(new LogRestTemplateInterceptor());

                log.info("The RestTemplate [{}] have added a interceptor named [{}].",bean,"LogRestTemplateInterceptor");
            }

            return bean;
        }
    }
}
