package com.springbootbasepackage.configuration;

import com.alibaba.druid.pool.DruidDataSource;

import javax.sql.DataSource;

public class BaseDataSourceConfiguration {

    public DataSource getDataSourcd(String driverClassName,String url,String username,String password){
        DruidDataSource dataSource = new DruidDataSource();
        dataSource.setDriverClassName(driverClassName);
        dataSource.setUrl(url);
        dataSource.setUsername(username);
        dataSource.setPassword(password);
        return dataSource;
    }
}
