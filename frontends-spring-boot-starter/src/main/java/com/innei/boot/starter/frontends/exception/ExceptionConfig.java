package com.innei.boot.starter.frontends.exception;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "front.exp")
@Data
public class ExceptionConfig {


    private boolean logEnable;
}
