package com.innei.boot.starter.log.server;

import java.io.PrintWriter;
import java.io.Writer;

/**
 * Created by LIUMI969 on 2017/6/29.
 */
public class LogPrintWriter extends PrintWriter {

    private StringBuilder builder;

    public LogPrintWriter(Writer out) {
        super(out);
        builder = new StringBuilder();

    }

    @Override
    public void write(char buf[], int off, int len) {
        super.write(buf,off,len);

        char[] dest = new char[len];
        System.arraycopy(buf, off, dest, 0, len);
        builder.append(dest);

    }

    public String responseStr(){
        return builder.toString();
    }


}
