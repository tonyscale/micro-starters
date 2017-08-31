package com.innei.boot.starter.log.util;

import com.google.common.base.Joiner;
import lombok.extern.slf4j.Slf4j;

import javax.servlet.http.HttpServletRequest;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.ThreadLocalRandom;

/**
 * Created by chenxuanyu126 on 2017/4/17.
 */
@Slf4j
public class TraceInfoFactory {



    public static String createTraceInfo(HttpServletRequest request){
        String currTime = new SimpleDateFormat("dd-HH:mm:ss.SSS").format(new Date());
        String requestURI = request.getRequestURI();

        return concat(currTime,IpUtil.getServerIp(),requestURI,ThreadLocalRandom.current().nextInt(1000)+"");
    }

    private static String concat(String ... strs) {

        return Joiner.on("-").join(strs);


    }

    public static void main(String[] args) {

    }
}
