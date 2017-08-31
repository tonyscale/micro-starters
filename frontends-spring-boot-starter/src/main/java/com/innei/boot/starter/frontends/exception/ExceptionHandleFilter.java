package com.innei.boot.starter.frontends.exception;


import com.innei.boot.starter.frontends.response.DefaultResultEnum;
import com.innei.boot.starter.frontends.response.ResponseUtil;
import com.innei.boot.starter.frontends.response.ResultInfo;
import lombok.extern.slf4j.Slf4j;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Slf4j
public class ExceptionHandleFilter implements Filter {
    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        log.info("ExceptionHandleFilter was added to the server");
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {

        if (request instanceof HttpServletRequest && response instanceof HttpServletResponse) {

            try {

                chain.doFilter(request, response);

                HttpServletResponse  hsr = (HttpServletResponse) response;

                if (!response.isCommitted()) {
                    ResponseUtil.toResponse(hsr, DefaultResultEnum.OK.resultInfo());
                }


            } catch (Throwable e) {

                //Filter得到Controller异常会包装成NestException
                Throwable cause = e.getCause() == null ? e : e.getCause();

                ResultInfo resultInfo = DefaultResultEnum.SERVER_INNER_EXCEPTION.resultInfo();
                if(cause instanceof BizException){
                    resultInfo = ((BizException) cause).getResultInfo();
                }else if(e instanceof BizException){
                    resultInfo = ((BizException) e).getResultInfo();
                }

                ResponseUtil.toResponse((HttpServletResponse)response,resultInfo);
            }
        }
    }

    @Override
    public void destroy() {

    }
}
