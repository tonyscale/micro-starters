package com.innei.boot.starter.http;

import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.ClientHttpRequestFactory;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;

/**
 * Created by LIUMI969 on 2017/4/19.
 *
 */
@Configuration
@ConditionalOnClass(RestTemplate.class)
@ConditionalOnMissingBean(RestTemplate.class)
@ConditionalOnBean(PoolingHttpClientConnectionManager.class)
public class RestTemplateConfigure {

    @Autowired
    private HttpConfig httpConfig;

    @Bean
    public RestTemplate restTemplate(ClientHttpRequestFactory factory) {
        return new RestTemplate(factory);
    }

    @Bean
    public ClientHttpRequestFactory clientHttpRequestFactory(PoolingHttpClientConnectionManager pccm) {

        CloseableHttpClient client = HttpClientBuilder.create().setConnectionManager(pccm).build();
        HttpComponentsClientHttpRequestFactory factory = new HttpComponentsClientHttpRequestFactory(client);
        factory.setReadTimeout(httpConfig.getMaxSocketTimeoutInMillis()); // ms
        factory.setConnectTimeout(httpConfig.getMaxConnectionTimeoutInMillis()); // ms

        return factory;
    }

}
