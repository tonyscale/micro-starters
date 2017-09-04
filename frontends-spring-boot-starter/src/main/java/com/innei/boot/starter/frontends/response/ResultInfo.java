package com.innei.boot.starter.frontends.response;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * Created by LIUMI969 on 2017/5/11.
 */
@Data
@AllArgsConstructor
public class ResultInfo {

    /**
     * 返回码
     */
    private Integer code;

    /**
     * 返回信息
     */
    private String msg;

    /**
     * 每个接口返回的具体数据
     */
    private Object data;

    public ResultInfo(){
        this.code = DefaultResultEnum.OK.getCode();
        this.msg = DefaultResultEnum.OK.getMsg();
    }
    public ResultInfo(Object data) {
        this();
        this.data = data;
    }

    public ResultInfo(Integer code, String result) {
        this.code = code;
        this.msg = result;
    }


}
