package com.innei.boot.starter.log.server;


import com.innei.boot.starter.log.util.MDCUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;


@Slf4j
public class LoggerFilter implements Filter {

    private LogConfig logConfig;

    public LoggerFilter(LogConfig logConfig){
        this.logConfig = logConfig;
    }

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {

    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {

        if (request instanceof HttpServletRequest && response instanceof HttpServletResponse) {



            try {

                HttpServletRequest hsr = (HttpServletRequest) request;


                if(StringUtils.isEmpty(logConfig.getTraceParam())){
                    MDCUtil.setTraceInfo(hsr);
                }else {
                    MDCUtil.setTraceInfo(hsr,logConfig.getTraceParam());
                }



                StringBuilder headers = new StringBuilder();

                if(!CollectionUtils.isEmpty(logConfig.getServerHeaderParams())){

                    for (String headerParam : logConfig.getServerHeaderParams()) {
                        headers.append("[")
                               .append(headerParam)
                               .append(":")
                               .append(hsr.getHeader(headerParam))
                               .append("] ");
                    }

                }

                log.info("request headers {} parameters {}",headers.toString(),hsr.getParameterMap());

                LogServletResponse logServletResponse = new LogServletResponse((HttpServletResponse) response);

                chain.doFilter(request, logServletResponse);

                log.info("response {}",logServletResponse.responseStr());


            } catch (Throwable e) {

                log.error("request occur exception.",e);
                throw e;
            }finally {

                MDCUtil.clear();

            }

        }
    }

    @Override
    public void destroy() {

    }
}
