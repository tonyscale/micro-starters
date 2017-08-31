package com.innei.boot.starter.log.http;

import org.apache.commons.io.IOUtils;
import org.springframework.http.HttpHeaders;
import org.springframework.http.client.AbstractClientHttpResponse;
import org.springframework.http.client.ClientHttpResponse;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * Created by chenxuanyu126 on 2017/4/19.
 *
 */
public class HttpComponentsClientHttpResponseExtend extends AbstractClientHttpResponse {

    private ClientHttpResponse clientHttpResponse;

    private ByteArrayInputStream byteArrayInputStream;

    public HttpComponentsClientHttpResponseExtend(ClientHttpResponse clientHttpResponse) throws IOException {
        this.clientHttpResponse = clientHttpResponse;
        this.byteArrayInputStream = new ByteArrayInputStream(IOUtils.toByteArray(this.clientHttpResponse.getBody()));
    }

    @Override
    public int getRawStatusCode() throws IOException {
        return this.clientHttpResponse.getRawStatusCode();
    }

    @Override
    public String getStatusText() throws IOException {
        return this.clientHttpResponse.getStatusText();
    }

    @Override
    public HttpHeaders getHeaders() {
        return this.clientHttpResponse.getHeaders();
    }

    @Override
    public InputStream getBody() throws IOException {
        byteArrayInputStream.reset();
        return byteArrayInputStream;
    }

    @Override
    public void close() {
        clientHttpResponse.close();
    }

}
