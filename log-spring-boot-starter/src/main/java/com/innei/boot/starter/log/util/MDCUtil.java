package com.innei.boot.starter.log.util;

import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.MDC;

import javax.servlet.http.HttpServletRequest;

/**
 * Created by LIUMI969 on 2016-11-10.
 * SL4J进行 traceId 的注入
 */
@Slf4j
public class MDCUtil {

    private static final String MDC_TRACE_INFO = "traceId";

    public static String getTraceInfo() {
        return MDC.get(MDC_TRACE_INFO);
    }

    public static void clear() {
        MDC.clear();
    }


    public static void setTraceInfo(@NonNull String traceParam, String traceInfo) {
        MDC.put(traceParam, traceInfo);
    }

    public static void setTraceInfo(String traceInfo) {
        setTraceInfo(MDC_TRACE_INFO, traceInfo);
    }


    public static void setTraceInfo(HttpServletRequest request){

        setTraceInfo(TraceInfoFactory.createTraceInfo(request));
    }

    public static void setTraceInfo(@NonNull HttpServletRequest request,@NonNull String traceParam){

        setTraceInfo(traceParam,TraceInfoFactory.createTraceInfo(request));
    }
}
