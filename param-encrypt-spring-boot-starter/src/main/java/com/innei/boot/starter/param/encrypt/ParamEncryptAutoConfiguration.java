package com.innei.boot.starter.param.encrypt;

import com.innei.boot.starter.param.encrypt.config.ParamEncrytConfig;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@Slf4j
@EnableConfigurationProperties(ParamEncrytConfig.class)
public class ParamEncryptAutoConfiguration {
}
