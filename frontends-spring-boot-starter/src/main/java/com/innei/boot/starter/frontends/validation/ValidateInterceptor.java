package com.innei.boot.starter.frontends.validation;


import com.innei.boot.starter.frontends.param.ReqHeader;
import com.innei.boot.starter.frontends.param.ReqParam;
import com.innei.boot.starter.frontends.response.ResponseUtil;
import com.innei.boot.starter.frontends.response.Result;
import org.springframework.core.DefaultParameterNameDiscoverer;
import org.springframework.core.ParameterNameDiscoverer;
import org.springframework.util.ClassUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.Parameter;

public class ValidateInterceptor extends HandlerInterceptorAdapter {

    private ParameterNameDiscoverer pd = new DefaultParameterNameDiscoverer();

    @Override
    public boolean preHandle(HttpServletRequest request,
                             HttpServletResponse response, Object handler) throws Exception {

        if (handler instanceof HandlerMethod) {

            HandlerMethod hm = (HandlerMethod) handler;
            Parameter[] parameters = hm.getMethod().getParameters();

            for (int i = 0; i < parameters.length; i++) {
                Parameter parameter = parameters[i];

                Class<?> parameterType = parameter.getType();


                if(String.class.equals(parameterType) || ClassUtils.isPrimitiveOrWrapper(parameterType)){

                    Validate validation = parameter.getAnnotation(Validate.class);
                    if(null == validation){
                        continue;
                    }


                    RequestParam requestParam = parameter.getAnnotation(RequestParam.class);
                    RequestHeader requestHeader = parameter.getAnnotation(RequestHeader.class);


                    String paramName = pd.getParameterNames(hm.getMethod())[i];
                    String  paramVal = request.getParameter(paramName);

                    if(null != requestHeader){

                        if(!StringUtils.isEmpty(requestHeader.value())){
                            paramName = requestHeader.value();
                        }

                        paramVal = request.getHeader(paramName);

                    }else if(null != requestParam && !StringUtils.isEmpty(requestParam.value())){

                        paramName = requestParam.value();
                        paramVal = request.getParameter(paramName);

                    }


                     if(!validate(validation,paramName,paramVal,response)){
                        return false;
                     }


                }else {

                    Field[] fields = parameter.getType().getDeclaredFields();
                    for (Field field : fields) {

                        Validate validation = parameter.getAnnotation(Validate.class);
                        if(null == validation){
                            continue;
                        }

                        ReqHeader reqHeader = field.getAnnotation(ReqHeader.class);
                        ReqParam reqParam = field.getAnnotation(ReqParam.class);

                        String paramVal;

                        String paramName;

                        if(null != reqHeader && !StringUtils.isEmpty(reqHeader.value())){

                            paramName = reqHeader.value();
                            paramVal = request.getHeader(paramName);

                        }else if(null != reqParam && !StringUtils.isEmpty(reqParam.value())){

                            paramName = reqParam.value();
                            paramVal = request.getParameter(paramName);

                        }else {

                            paramName = field.getName();
                            paramVal = request.getParameter(paramName);

                        }

                        if(!validate(validation,paramName,paramVal,response)){
                            return false;
                        }
                    }

                }


            }
        }


        return true;
    }



    private boolean validate(Validate validation,String paramName,String paramVal,HttpServletResponse response) throws IOException {


        Result validate = ValidateUtil.validate(validation, paramVal);


        //Validate successfully
        if(null == validate){
            return true;
        }

        ResponseUtil.toResponse(response,validate.resultInfo(),paramName);

        return false;

    }


}
