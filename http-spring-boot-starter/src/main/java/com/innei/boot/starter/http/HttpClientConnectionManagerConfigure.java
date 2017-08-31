package com.innei.boot.starter.http;

import org.apache.http.config.Registry;
import org.apache.http.config.RegistryBuilder;
import org.apache.http.conn.socket.ConnectionSocketFactory;
import org.apache.http.conn.socket.LayeredConnectionSocketFactory;
import org.apache.http.conn.socket.PlainConnectionSocketFactory;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.ssl.SSLContexts;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.net.ssl.SSLContext;

/**
 * Created by LIUMI969 on 2017/4/20.
 *
 */
@Configuration
@ConditionalOnClass(PoolingHttpClientConnectionManager.class)
@ConditionalOnMissingBean(PoolingHttpClientConnectionManager.class)
public class HttpClientConnectionManagerConfigure {

    @Autowired
    private HttpConfig httpConfig;

    @Bean
    public PoolingHttpClientConnectionManager poolingHttpClientConnectionManager() {

        ConnectionSocketFactory plainsf = new PlainConnectionSocketFactory();
        SSLContext sslContext = SSLContexts.createSystemDefault();
        LayeredConnectionSocketFactory sslsf = new SSLConnectionSocketFactory(sslContext);
        Registry<ConnectionSocketFactory> r = RegistryBuilder.<ConnectionSocketFactory>create()
                .register("http", plainsf)
                .register("https", sslsf)
                .build();
        PoolingHttpClientConnectionManager poolingHttpClientConnectionManager = new
                PoolingHttpClientConnectionManager(r);
        poolingHttpClientConnectionManager.setMaxTotal(httpConfig.getPoolHttpClientMaxTotal());
        poolingHttpClientConnectionManager.setDefaultMaxPerRoute(httpConfig.getMaxPerRout());

        return poolingHttpClientConnectionManager;
    }
}
