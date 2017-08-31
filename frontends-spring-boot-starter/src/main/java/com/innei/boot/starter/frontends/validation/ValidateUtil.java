package com.innei.boot.starter.frontends.validation;


import com.innei.boot.starter.frontends.response.DefaultResultEnum;
import com.innei.boot.starter.frontends.response.Result;
import lombok.NonNull;
import org.springframework.util.StringUtils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ValidateUtil {


    public static Result validate(@NonNull Validate validation , String parameterVal){

        if(validation.notEmpty() && StringUtils.isEmpty(parameterVal)){

           return DefaultResultEnum.PARAMETER_ILLEGAL;


        }

        String pattern = validation.regexp();


        if(!StringUtils.isEmpty(pattern)){

            if(StringUtils.isEmpty(parameterVal)){
                return DefaultResultEnum.PARAMETER_ILLEGAL;
            }

            Matcher matcher = Pattern.compile(pattern).matcher(parameterVal);

            if(!matcher.matches()){
                return DefaultResultEnum.PARAMETER_ILLEGAL;
            }

        }


        return null;


    }




}
