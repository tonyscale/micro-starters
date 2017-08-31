package com.innei.boot.starter.frontends.response;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;
import lombok.NonNull;
import org.springframework.util.StringUtils;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;


public class ResponseUtil {

    private static final String DEFAULT_CHARSET = "UTF-8";

    private final static String RESPONSE_CONTENT_TYPE_PARAM = "Content-Type";

    private final static String DEFAULT_RESPONSE_CONTENT_TYPE_VALUE = "application/json";

    private final static String RESPONSE_CONTENT_LENGTH_PARAM = "Content-Length";


    public static void toResponse(@NonNull HttpServletResponse response, @NonNull String content, String charset) throws IOException {

        if(StringUtils.isEmpty(charset)){
            charset = DEFAULT_CHARSET;
        }
        byte[] bytes = content.getBytes(charset);
        response.setHeader(RESPONSE_CONTENT_TYPE_PARAM,DEFAULT_RESPONSE_CONTENT_TYPE_VALUE);
        response.setHeader(RESPONSE_CONTENT_LENGTH_PARAM,bytes.length+"");
        response.getOutputStream().write(bytes);
        response.flushBuffer();

    }

    public static void toResponse(HttpServletResponse response, String content) throws IOException {

        toResponse(response,content,null);
    }


    public static void toResponse(HttpServletResponse response, @NonNull ResultInfo resultInfo, Object... args) throws IOException {

        if(args.length > 0){
            resultInfo.setResult(String.format(resultInfo.getResult(), args));
        }

        String res = JSON.toJSONString(resultInfo, SerializerFeature.WriteNullListAsEmpty);
        toResponse(response,res);

    }






}
