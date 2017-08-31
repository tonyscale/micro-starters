package com.innei.boot.starter.frontends.response;

import com.alibaba.fastjson.support.spring.FastJsonHttpMessageConverter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpOutputMessage;
import org.springframework.http.converter.HttpMessageNotWritableException;
import org.springframework.http.server.ServletServerHttpResponse;

import java.io.IOException;

/**
 *
 * Created by LIUMI969 on 2016-11-14.
 */
@Slf4j
public class JsonHttpMessageConverter extends FastJsonHttpMessageConverter {



    @Override
    protected void writeInternal(Object obj, HttpOutputMessage outputMessage) throws IOException,
            HttpMessageNotWritableException {

        if (outputMessage instanceof ServletServerHttpResponse && !(obj instanceof ResultInfo) && !(obj instanceof CustomResult)) {

            obj = new ResultInfo(obj);

        }

        super.writeInternal(obj, outputMessage);
    }


}
