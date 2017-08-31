package com.innei.boot.starter.frontends.response;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpOutputMessage;
import org.springframework.http.server.ServletServerHttpResponse;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.Charset;

/**
 * Created by LIUMI969 on 2016-11-21.
 *
 */
@Slf4j
public class StringHttpMessageConverter extends org.springframework.http.converter.StringHttpMessageConverter{

    private static final Charset UTF_8 = Charset.forName("UTF-8");

    public StringHttpMessageConverter() {
        super(UTF_8);
    }

    @Override
    protected void writeInternal(String str, HttpOutputMessage outputMessage) throws IOException {

        if(outputMessage instanceof ServletServerHttpResponse){

            ResultInfo resultInfo = new ResultInfo(str);
            str = JSON.toJSONString(resultInfo, SerializerFeature.WriteNullListAsEmpty);

        }

        byte[] bytes = str.getBytes("UTF-8");
        HttpHeaders headers = outputMessage.getHeaders();
        headers.setContentLength(bytes.length);
        OutputStream out = outputMessage.getBody();
        out.write(bytes);

    }

}
