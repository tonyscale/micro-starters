package com.innei.boot.starter.log.server;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.List;

/**
 * Created by LIUMI969 on 2017/8/30.
 */
@Data
@ConfigurationProperties(prefix = "log")
public class LogConfig {

    /**
     * 用于Log4J中配置MDC参数
     */
    private String traceParam;
    private List<String> serverHeaderParams;
    
}
