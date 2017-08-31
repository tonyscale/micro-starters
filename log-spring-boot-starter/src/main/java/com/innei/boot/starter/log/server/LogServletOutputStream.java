package com.innei.boot.starter.log.server;

import javax.servlet.ServletOutputStream;
import javax.servlet.WriteListener;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by LIUMI969 on 2017/6/29.
 */
public class LogServletOutputStream extends ServletOutputStream {


    private ServletOutputStream outputStream;
    private List<Byte> responseBytes;

    LogServletOutputStream(ServletOutputStream outputStream){
        this.outputStream = outputStream;
        responseBytes = new ArrayList<>();
    }

    @Override
    public boolean isReady() {
        return outputStream.isReady();
    }

    @Override
    public void setWriteListener(WriteListener writeListener) {
        outputStream.setWriteListener(writeListener);
    }

    @Override
    public void write(int b) throws IOException {
        outputStream.write(b);

        responseBytes.add((byte)b);
    }

    public String responseStr() throws UnsupportedEncodingException {

        byte[] bytes = new byte[responseBytes.size()];

        for(int i=0;i<responseBytes.size();i++){
            bytes[i] = responseBytes.get(i);
        }

        return new String(bytes,"UTF-8");
    }

}
