package com.springbootbasepackage.configuration;

import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.SqlSessionTemplate;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;

import javax.sql.DataSource;


@Configuration
//@MapperScan(basePackages = {"com.springbootbasepackage.dao","com.songshu.snt.ufc.wallet.dao","com.songshu.snt.ufc.loan.dao"}, sqlSessionTemplateRef = "ufcSqlSessionTemplate")
@MapperScan(basePackages = {"com.springbootbasepackage.dao"}, sqlSessionTemplateRef = "ufcSqlSessionTemplate")
public class UfcDataSourceConfiguration extends BaseDataSourceConfiguration {

    @Value("${spring.datasource.ufc.driver-class-name}")
    private String driverClassName;

    @Value("${spring.datasource.ufc.url}")
    private String url;

    @Value("${spring.datasource.ufc.username}")
    private String username;

    @Value("${spring.datasource.ufc.password}")
    private String password;

    @Bean(name = "ufcDataSource")
    public DataSource dataSource() {
        return getDataSourcd(driverClassName,url,username,password);
    }

    @Bean(name = "ufcSqlSessionFactory")
    public SqlSessionFactory sqlSessionFactory(@Qualifier("ufcDataSource") DataSource dataSource) throws Exception {
        SqlSessionFactoryBean bean = new SqlSessionFactoryBean();
        bean.setDataSource(dataSource);
        bean.setConfigLocation(new PathMatchingResourcePatternResolver().getResource("classpath:mybatis/mybatis-config.xml"));
        bean.setMapperLocations(new PathMatchingResourcePatternResolver().getResources("classpath*:mybatis/ufc/*.xml"));
        return bean.getObject();
    }

    @Primary
    @Bean(name = "transactionManager")
    public DataSourceTransactionManager transactionManager(@Qualifier("ufcDataSource") DataSource dataSource) {
        return new DataSourceTransactionManager(dataSource);
    }

    @Bean(name = "ufcSqlSessionTemplate")
    public SqlSessionTemplate sqlSessionTemplate(@Qualifier("ufcSqlSessionFactory") SqlSessionFactory sqlSessionFactory){
        return new SqlSessionTemplate(sqlSessionFactory);
    }


}
