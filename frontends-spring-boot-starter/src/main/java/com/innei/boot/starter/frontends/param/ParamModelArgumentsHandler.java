package com.innei.boot.starter.frontends.param;


import org.springframework.beans.BeanUtils;
import org.springframework.core.MethodParameter;
import org.springframework.core.convert.ConversionService;
import org.springframework.core.convert.support.DefaultConversionService;
import org.springframework.util.ClassUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;


public class ParamModelArgumentsHandler implements HandlerMethodArgumentResolver {

    private final ConversionService conversionService;

    public ParamModelArgumentsHandler(){
        this.conversionService =  new DefaultConversionService();
    }

    @Override
    public boolean supportsParameter(MethodParameter parameter) {

        Class<?> parameterType = parameter.getParameterType();


        Annotation[] annotations = parameter.getParameterAnnotations();

        for (Annotation annotation : annotations) {

            boolean isHaveConvertAnno = annotation.annotationType()
                                                  .getPackage().getName()
                                                  .startsWith("org.springframework.server.bind.annotation");
            if(isHaveConvertAnno){
                return false;
            }

        }

        if(String.class.equals(parameterType) || ClassUtils.isPrimitiveOrWrapper(parameterType)){

            return false;

        }

        Field[] declaredFields = parameterType.getDeclaredFields();

        for (Field declaredField : declaredFields) {

            Annotation[] fieldAnnos = declaredField.getAnnotations();

            for (Annotation fieldAnno : fieldAnnos) {
                boolean assignable = isAssignable(fieldAnno.getClass());

                if(assignable){
                    return true;
                }


            }
        }

        return false;

    }

    @Override
    public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer,
                                  NativeWebRequest webRequest, WebDataBinderFactory binderFactory) throws Exception {

        Class<?> parameterType = parameter.getParameterType();

        Object o = BeanUtils.instantiate(parameterType);

        Field[] fields = parameterType.getDeclaredFields();

        for (Field field : fields) {

            ReqHeader requestHeader = field.getAnnotation(ReqHeader.class);
            ReqParam requestParam = field.getAnnotation(ReqParam.class);
            field.setAccessible(true);

            String paramValStr;


            if(null != requestHeader && !StringUtils.isEmpty(requestHeader.value())){

                paramValStr = webRequest.getHeader(requestHeader.value());

            }else if (null != requestParam && !StringUtils.isEmpty(requestParam.value())){

               paramValStr = webRequest.getParameter(requestParam.value());

            }else {
                paramValStr = webRequest.getParameter(field.getName());
            }

            field.set(o,conversionService.convert(paramValStr, field.getType()));

        }

        return o;
    }


    private boolean isAssignable(Class clazz){

        return ClassUtils.isAssignable(ReqParam.class, clazz) || ClassUtils.isAssignable(ReqHeader.class, clazz);

    }


}
