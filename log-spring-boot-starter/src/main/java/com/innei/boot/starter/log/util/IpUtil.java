package com.innei.boot.starter.log.util;

import lombok.extern.slf4j.Slf4j;

import javax.servlet.http.HttpServletRequest;
import java.net.InetAddress;
import java.net.UnknownHostException;

/**
 * Created by chenxuanyu126 on 2017/2/27.
 */
@Slf4j
public class IpUtil {

    private static final String VALUE_LOCAL_IP = "127.0.0.1";

    public static String getServerIp() {

        String localIp;
        try {
            localIp = InetAddress.getLocalHost().getHostAddress();
        } catch (UnknownHostException e) {
            log.error("require local IP occur eventException", e);
            localIp = VALUE_LOCAL_IP;
        }
        return localIp;
    }

    /**
     * 从request对象中获取客户端真实的ip地址
     *
     * @param request
     * @return 客户端的IP地址
     */
    public static String getSourceIpChain(HttpServletRequest request) {

        String ip = request.getHeader("x-forwarded-for");
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("Proxy-Client-IP");
        }
        if (ip == null || ip.length() == 0 || "unknow".equalsIgnoreCase(ip)) {
            ip = request.getHeader("WL-Proxy-Client-IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getRemoteAddr();
        }

        return ip;
    }

}
