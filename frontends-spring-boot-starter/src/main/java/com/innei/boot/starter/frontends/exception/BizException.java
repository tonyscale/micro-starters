package com.innei.boot.starter.frontends.exception;


import com.innei.boot.starter.frontends.response.Result;
import com.innei.boot.starter.frontends.response.ResultInfo;
import lombok.Getter;
import lombok.Setter;

/**
 * Created by LIUMI969 on 2016-10-31.
 *
 */
@Setter
@Getter
public class BizException extends RuntimeException{

    private ResultInfo resultInfo;

    public BizException(ResultInfo resultInfo){
        this.resultInfo = resultInfo;
    }

	/**
     * 在业务中捕获的异常，带上错误码（code） 错误原因（msg） 捕获到异常（cause）
     * @param resultInfo
     * @param cause
     * @param cause
     */
    public BizException(ResultInfo resultInfo,Throwable cause){
        super(cause);
        this.resultInfo = resultInfo;
    }

    public BizException(Result result){
        this(result.resultInfo());
    }

    public BizException(Result result,Throwable cause){
        super(cause);
        this.resultInfo = result.resultInfo();
    }

}
