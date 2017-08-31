package com.innei.boot.starter.frontends.cors;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.Map;

/**
 * Created by LIUMI969 on 2017/6/27.
 */
@ConfigurationProperties(prefix = "cors")
@Data
public class CorsConfig {


    private String enable;

    private Map<String,String> urlToAllowOrigins;
}
