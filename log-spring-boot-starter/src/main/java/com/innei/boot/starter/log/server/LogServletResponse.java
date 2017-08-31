package com.innei.boot.starter.log.server;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpServletResponseWrapper;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;

/**
 * Created by LIUMI969 on 2017/6/29.
 */
public class LogServletResponse extends HttpServletResponseWrapper{


    private LogServletOutputStream servletOutputStream;

    private LogPrintWriter logPrintWriter;


    public LogServletResponse(HttpServletResponse response) {

        super(response);
    }

    @Override
    public PrintWriter getWriter() throws IOException{

        if(logPrintWriter == null){
            logPrintWriter = new LogPrintWriter(getWriter());
        }



        return logPrintWriter;
    }

    @Override
    public ServletOutputStream getOutputStream() throws IOException{

        if(servletOutputStream == null){
            servletOutputStream = new LogServletOutputStream(super.getOutputStream());
        }

        return servletOutputStream;
    }

    public String responseStr() throws UnsupportedEncodingException {

        String responseStr = null;

        if(null != servletOutputStream){
            responseStr = servletOutputStream.responseStr();
        }else if(null != logPrintWriter){
            responseStr = logPrintWriter.responseStr();
        }


        return responseStr;

    }
}
