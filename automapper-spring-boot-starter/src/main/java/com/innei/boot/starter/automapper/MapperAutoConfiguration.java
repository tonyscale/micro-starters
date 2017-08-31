/*
 * The MIT License (MIT)
 *
 * Copyright (c) 2017 abel533@gmail.com
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */

package com.innei.boot.starter.automapper;

import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.session.SqlSession;
import org.mybatis.spring.mapper.MapperFactoryBean;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanCreationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.util.CollectionUtils;
import tk.mybatis.mapper.common.IdsMapper;
import tk.mybatis.mapper.common.Mapper;
import tk.mybatis.mapper.common.MySqlMapper;
import tk.mybatis.mapper.entity.Config;
import tk.mybatis.mapper.mapperhelper.MapperHelper;

import javax.annotation.PostConstruct;

@Configuration
@EnableConfigurationProperties(MapperProperties.class)
@ConditionalOnClass(value = MapperHelper.class)
@Slf4j
public class MapperAutoConfiguration implements BeanPostProcessor {

    @Autowired
    private MapperProperties mapperProperties;

    @PostConstruct
    public void logConfig(){
        log.info("MapperProperties are {}.",mapperProperties);
    }

    @Override
    public Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException {
        return bean;
    }

    @Override
    public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
        if(bean instanceof MapperFactoryBean){
            MapperFactoryBean mapperFactoryBean = (MapperFactoryBean) bean;

            log.info("MapperInterface [{}]",mapperFactoryBean.getMapperInterface());

            addPageInterceptor(mapperFactoryBean.getSqlSession());

        }
        return bean;
    }

    public void addPageInterceptor(SqlSession sqlSession) throws BeansException {
        //创建一个MapperHelper
        MapperHelper mapperHelper = new MapperHelper();
        //设置配置
        Config config = new Config();

        try {
            BeanUtils.copyProperties(mapperProperties,config,true);

        }catch (Exception e){
            throw new BeanCreationException("inject automapper occur exception.",e);
        }

        mapperHelper.setConfig(config);

        // 注册通用Mapper接口 - 可以自动注册继承的接口
        if(CollectionUtils.isEmpty(mapperProperties.getMappers())){
            //默认注册mysql的mapper
            mapperHelper.registerMapper(Mapper.class);
            mapperHelper.registerMapper(MySqlMapper.class);
            mapperHelper.registerMapper(IdsMapper.class);
        }

        //配置完成后，执行下面的操作
        mapperHelper.processConfiguration(sqlSession.getConfiguration());

    }

}
