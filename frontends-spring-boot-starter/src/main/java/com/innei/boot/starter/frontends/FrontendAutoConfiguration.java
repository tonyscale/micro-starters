package com.innei.boot.starter.frontends;

import com.alibaba.fastjson.serializer.SerializerFeature;
import com.innei.boot.starter.frontends.cors.CorsConfig;
import com.innei.boot.starter.frontends.exception.ExceptionConfig;
import com.innei.boot.starter.frontends.exception.ExceptionHandleFilter;
import com.innei.boot.starter.frontends.param.ParamModelArgumentsHandler;
import com.innei.boot.starter.frontends.response.JsonHttpMessageConverter;
import com.innei.boot.starter.frontends.response.StringHttpMessageConverter;
import com.innei.boot.starter.frontends.validation.ValidateInterceptor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.web.HttpMessageConverters;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

import javax.servlet.DispatcherType;
import java.util.List;
import java.util.Map;

@Configuration
@Slf4j
@EnableConfigurationProperties({CorsConfig.class, ExceptionConfig.class})
public class FrontendAutoConfiguration {

    @Autowired
    private ExceptionConfig exceptionConfig;

    @Autowired
    private CorsConfig corsConfig;

    /**
     * server中Filter的注入
     */
    @Bean(name = "AllExceptionHandler")
    public FilterRegistrationBean exceptionFilterRegistration() {

        FilterRegistrationBean registration = new FilterRegistrationBean();
        registration.setFilter(new ExceptionHandleFilter(exceptionConfig));
        registration.setOrder(Ordered.HIGHEST_PRECEDENCE + 20);
        registration.setName("ExceptionHandleFilter");
        registration.setDispatcherTypes(DispatcherType.REQUEST, DispatcherType.FORWARD);

        log.info(" ExceptionHandleFilter was added FilterChain");

        return registration;
    }


    @Bean(name = "corsFilter")
    @ConditionalOnProperty(name = "cors.enable")
    public FilterRegistrationBean corsFilter() {
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();

        Map<String, String> urlToAllowOrigins = corsConfig.getUrlToAllowOrigins();

        for (Map.Entry<String, String> entry : urlToAllowOrigins.entrySet()) {

            CorsConfiguration config = new CorsConfiguration();
            config.setAllowCredentials(true);
            String[] allowOrigins = entry.getValue().split(",");

            for (String allowOrigin : allowOrigins) {
                config.addAllowedOrigin(allowOrigin);
            }

            config.addAllowedHeader(CorsConfiguration.ALL);
            config.addAllowedMethod(CorsConfiguration.ALL);
            source.registerCorsConfiguration(entry.getKey(), config);
        }


        FilterRegistrationBean bean = new FilterRegistrationBean(new CorsFilter(source));
        bean.setOrder(Ordered.HIGHEST_PRECEDENCE + 19);
        return bean;
    }


    @Configuration
    @Order(Ordered.HIGHEST_PRECEDENCE)
    public class MvcConfigurer extends WebMvcConfigurerAdapter{

        private static final String ALL_URI = "/**";

        @Override
        public void addArgumentResolvers(List<HandlerMethodArgumentResolver> argumentResolvers) {
            argumentResolvers.add(new ParamModelArgumentsHandler());
        }

        @Override
        public void addInterceptors(InterceptorRegistry registry) {

            registry.addInterceptor(new ValidateInterceptor()).addPathPatterns(ALL_URI);
        }
    }


    @Configuration
    public static class HttpMessageConvertersConfig{


        @Bean
        public HttpMessageConverters jsonHttpMessageConverters(){
            JsonHttpMessageConverter httpMessageConverter = new JsonHttpMessageConverter();


            httpMessageConverter.setFeatures(SerializerFeature.WriteNullStringAsEmpty,
                                             SerializerFeature.WriteNullListAsEmpty,
                                             SerializerFeature.WriteDateUseDateFormat,
                                             SerializerFeature.WriteMapNullValue);


            StringHttpMessageConverter stringHttpMessageConverter = new StringHttpMessageConverter();

            return new HttpMessageConverters(httpMessageConverter,stringHttpMessageConverter);
        }
    }

}
