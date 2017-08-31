package com.innei.boot.starter.http;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * Created by LIUMI969 on 2017/4/19.
 *
 */
@Configuration
@ConfigurationProperties(prefix = "http")
@Data
public class HttpConfig {

    private int maxConnectionTimeoutInMillis = 3000;

    private int maxSocketTimeoutInMillis = 300000;

    private int poolHttpClientMaxTotal = 100;

    private int maxPerRout = 50;
}
