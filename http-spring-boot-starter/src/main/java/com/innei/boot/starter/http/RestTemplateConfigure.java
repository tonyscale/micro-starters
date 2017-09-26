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
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.web.client.RestTemplate;

import java.nio.charset.Charset;
import java.util.Iterator;
import java.util.List;

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
        RestTemplate restTemplate = new RestTemplate(factory);
        List<HttpMessageConverter<?>> messageConverters = restTemplate.getMessageConverters();

        messageConverters.removeIf(httpMessageConverter -> httpMessageConverter instanceof StringHttpMessageConverter);

        messageConverters.add(new StringHttpMessageConverter(Charset.forName("UTF-8")));

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
