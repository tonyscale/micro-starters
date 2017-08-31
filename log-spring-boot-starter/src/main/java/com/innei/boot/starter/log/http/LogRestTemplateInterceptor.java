package com.innei.boot.starter.log.http;


import com.google.common.base.Charsets;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.IOUtils;
import org.springframework.http.HttpRequest;
import org.springframework.http.MediaType;
import org.springframework.http.client.ClientHttpRequestExecution;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.http.client.ClientHttpResponse;

import java.io.IOException;
import java.nio.charset.Charset;

/**
 * RestTemplate 拦截器, 监控开启则生效
 * Created by chenxuanyu126 on 2017/2/27.
 */
@Slf4j
public class LogRestTemplateInterceptor implements ClientHttpRequestInterceptor {

    @Override
    public ClientHttpResponse intercept(HttpRequest request, byte[] body, ClientHttpRequestExecution execution) throws IOException {


        MediaType reqContentType = request.getHeaders().getContentType();


        Charset charset = reqContentType == null ? Charsets.UTF_8 : reqContentType.getCharset();

        log.info("RestTemplate Request URI [{}],body [{}]",request.getURI(),new String(body,charset) );

        HttpComponentsClientHttpResponseExtend responseWrapper;

        try {


            responseWrapper = new HttpComponentsClientHttpResponseExtend(execution.execute(request, body));

            MediaType contentType = responseWrapper.getHeaders().getContentType();
            String responseBody = IOUtils.toString(responseWrapper.getBody(), null == contentType ? Charsets.UTF_8 : contentType.getCharset());

            log.info("RestTemplate Response status code [{}], body {}",responseWrapper.getRawStatusCode(),responseBody);

            return responseWrapper;

        } catch (Throwable e) {

            log.error("RestTemplate Response occur exception.",e);

            throw e;
        }
    }

}
