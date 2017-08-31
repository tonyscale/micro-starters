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

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import tk.mybatis.mapper.code.Style;

import java.util.ArrayList;
import java.util.List;


@ConfigurationProperties(prefix = "automapper")
@Data
public class MapperProperties{
    /**
     * 设置UUID生成策略
     * 配置UUID生成策略需要使用OGNL表达式
     * 默认值32位长度:@java.util.UUID@randomUUID().toString().replace("-", "")
     *
     */
    private String  uuid;

    /**
     *  获取主键自增回写SQL
     */
    private String  identity;

    /**
     * 主键自增回写方法执行顺序,默认AFTER,可选值为(BEFORE|AFTER)
     *
     */
    private boolean before;
    /**
     * 获取序列格式化模板
     *
     */
    private String  seqFormat;
    /**
     * 设置全局的catalog,默认为空，如果设置了值，操作表时的sql会是catalog.tablename
     *
     */
    private String  catalog;

    /**
     * 设置全局的schema,默认为空，如果设置了值，操作表时的sql会是schema.tablename
     * 如果同时设置了catalog,优先使用catalog.tablename
     *
     */
    private String  schema;
    /**
     * 校验调用Example方法时，Example(entityClass)和Mapper<EntityClass>是否一致
     */
    private boolean checkExampleEntityClass;

    /**
     * 使用简单类型
     */
    private boolean useSimpleType;
    /**
     * 是否支持方法上的注解，默认false
     */
    private boolean enableMethodAnnotation;
    /**
     * 对于一般的getAllIfColumnNode，是否判断!=''，默认不判断
     */
    private boolean notEmpty = false;

    /**
     * 字段转换风格，默认驼峰转下划线
     */
    private Style style;

    /**
     * 数据库Connection是否支持支持设置ResultSetType，默认为false，表示支持(Tddl中Tconnection不支持)
     */
    private boolean supportResultSetType = false;


    /**
     * 默认不能进行全表扫描
     */
    private boolean notScanAllTable = true;

    /**
     * 注册需要扫描的Mapper
     */
    private List<Class> mappers = new ArrayList<>();


}
