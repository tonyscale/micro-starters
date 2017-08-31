package com.innei.boot.starter.mybatis;

import lombok.Data;
import org.apache.commons.lang.StringUtils;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * Created by LIUMI969 on 2017/5/9.
 */
@Data
@ConfigurationProperties(prefix = "mybatis.datasource")
public class MybatisDatasourceProperties {

    private String driverClassName;
    private String url;
    private String username;
    private String password;
    private Integer initialSize = 10;
    private Integer minIdle = 10;
    private Integer maxActive;
    private Integer maxWait;
    private Long timeBetweenEvictionRunsMillis;
    private Long minEvictableIdleTimeMillis;

    private Boolean testWhileIdle;
    private Boolean testOnBorrow;
    private Boolean testOnReturn;
    private Boolean defaultAutoCommit;
    private String validationQuery;


    public void checkFields() {

       if(StringUtils.isEmpty(driverClassName)){
            throw new AutoMybatisException("The mybatis.datasource.driverClassName must not be empty");
       }

        try {
            Class.forName(driverClassName);
        } catch (ClassNotFoundException e) {
            throw new AutoMybatisException("The driver class doesn't exist.");
        }

        if(StringUtils.isEmpty(url)){
            throw new AutoMybatisException("The mybatis.datasource.url must not be empty");
        }

        if(StringUtils.isEmpty(username)){
            throw new AutoMybatisException("The mybatis.datasource.username must not be empty");
        }

        if(StringUtils.isEmpty(password)){
            throw new AutoMybatisException("The mybatis.datasource.password must not be empty");
        }

    }


}
