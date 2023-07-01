package com.example.demo.config;

import com.alibaba.druid.spring.boot.autoconfigure.DruidDataSourceBuilder;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.sql.DataSource;

/**
 * @Author: heal
 * @Date: 2023/7/1 10:44
 */
@Configuration
public class DruidDataSourceConfig {

    /**
     * 添加 DruidDataSource 组件到容器中，并绑定属性
     */
    @Bean
    @ConfigurationProperties(prefix = "spring.datasource.druid")
    public DataSource getDataSource() {
        return DruidDataSourceBuilder.create().build();
    }
}
