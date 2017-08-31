package com.innei.boot.starter.mybatis;

import com.alibaba.druid.pool.DruidDataSourceFactory;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.mapper.ClassPathMapperScanner;
import org.mybatis.spring.mapper.MapperFactoryBean;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.BeanFactoryAware;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.boot.autoconfigure.AutoConfigurationPackages;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.ResourceLoaderAware;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.ImportBeanDefinitionRegistrar;
import org.springframework.core.io.ResourceLoader;
import org.springframework.core.type.AnnotationMetadata;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.PlatformTransactionManager;

import javax.annotation.PostConstruct;
import javax.sql.DataSource;
import java.util.List;

/**
 * Created by LIUMI969 on 2017/5/10.
 *
 */
@Configuration
@ConditionalOnProperty(name = "mybatis.datasource.driverClassName")
@ConditionalOnClass(value = {SqlSessionFactory.class,MapperFactoryBean.class})
@EnableConfigurationProperties(MybatisDatasourceProperties.class)
@Slf4j
public class MybatisAutoConfiguration {

    @Autowired
    private MybatisDatasourceProperties datasourceProperties;


    @Bean
    @ConditionalOnMissingBean
    public DataSource dataSource() throws Exception {
        datasourceProperties.checkFields();
        return DruidDataSourceFactory.createDataSource(BeanUtils.beanToProperties(datasourceProperties));
    }

    @Bean
    @ConditionalOnMissingBean
    public PlatformTransactionManager transactionManager(DataSource dataSource) {
        return new DataSourceTransactionManager(dataSource);
    }

    @Bean
    @ConditionalOnMissingBean
    public SqlSessionFactory sqlSessionFactory(DataSource dataSource) throws Exception {
        SqlSessionFactoryBean factory = new SqlSessionFactoryBean();
        factory.setDataSource(dataSource);

        return factory.getObject();
    }

    @Configuration
    @Import({AutoConfiguredMapperScannerRegistrar.class })
    @ConditionalOnMissingBean(MapperFactoryBean.class)
    public static class MapperScannerRegistrarNotFoundConfiguration {

        @PostConstruct
        public void afterPropertiesSet() {
            log.info("No {} found.", MapperFactoryBean.class.getName());
        }
    }


    public static class AutoConfiguredMapperScannerRegistrar
            implements BeanFactoryAware, ImportBeanDefinitionRegistrar, ResourceLoaderAware {

        private BeanFactory beanFactory;

        private ResourceLoader resourceLoader;

        @Override
        public void registerBeanDefinitions(AnnotationMetadata importingClassMetadata, BeanDefinitionRegistry registry) {

            log.info("Searching for mappers annotated with @Mapper");

            ClassPathMapperScanner scanner = new ClassPathMapperScanner(registry);

            try {
                if (this.resourceLoader != null) {
                    scanner.setResourceLoader(this.resourceLoader);
                }

                List<String> packages = AutoConfigurationPackages.get(this.beanFactory);
                if (log.isInfoEnabled()) {
                    for (String pkg : packages) {
                        log.info("Using auto-configuration base package '{}'", pkg);
                    }
                }

                scanner.setAnnotationClass(MybatisMapper.class);
                scanner.registerFilters();

                //to remove the packages of @ImportAutoConfiguration
                scanner.doScan(packages.get(0));

            } catch (IllegalStateException ex) {
                log.error("Could not determine auto-configuration package, automatic automapper scanning disabled.", ex);
            }
        }

        @Override
        public void setBeanFactory(BeanFactory beanFactory) throws BeansException {
            this.beanFactory = beanFactory;
        }

        @Override
        public void setResourceLoader(ResourceLoader resourceLoader) {
            this.resourceLoader = resourceLoader;
        }
    }

}
