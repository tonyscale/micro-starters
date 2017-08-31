package com.innei.boot.starter.frontends.response;

/**
 * Created by LIUMI969 on 2017/5/11.
 */
public enum DefaultResultEnum implements Result {

    PARAMETER_ILLEGAL(-1,"The param [%s] is illegal"),
    SERVER_INNER_EXCEPTION(-2,"服务器开小差"),
    OK(0,"操作成功"),

    ;


    private Integer code;
    private String result;


    DefaultResultEnum(Integer code,String result){
        this.code = code;
        this.result = result;
    }

    public Integer getCode(){
        return code;
    }

    public String getResult(){
        return result;
    }

    @Override
    public ResultInfo resultInfo() {
        return new ResultInfo(code,result,null);
    }
}
