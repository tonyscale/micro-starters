package com.innei.boot.starter.http;

import org.springframework.boot.autoconfigure.ImportAutoConfiguration;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * Created by LIUMI969 on 2017/4/19.
 *
 */
@Configuration
@EnableConfigurationProperties(HttpConfig.class)
@ImportAutoConfiguration(value = {RestTemplateConfigure.class,HttpClientConnectionManagerConfigure.class})
public class HttpClientAutoConfiguration {

}
