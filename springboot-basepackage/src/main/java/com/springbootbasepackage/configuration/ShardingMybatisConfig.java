package com.springbootbasepackage.configuration;

import com.alibaba.druid.pool.DruidDataSource;
import com.springbootbasepackage.algorithm.DatabaseShardingAlgorithm;
import com.springbootbasepackage.algorithm.TablePreciseShardingAlgorithm;
import com.springbootbasepackage.algorithm.TableRangeShardingAlgorithm;

import org.apache.shardingsphere.api.config.sharding.strategy.StandardShardingStrategyConfiguration;
import org.apache.shardingsphere.shardingjdbc.api.ShardingDataSourceFactory;
import org.apache.shardingsphere.api.config.sharding.TableRuleConfiguration;

import org.apache.shardingsphere.api.config.sharding.KeyGeneratorConfiguration;
import org.apache.shardingsphere.api.config.sharding.ShardingRuleConfiguration;

import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.SqlSessionTemplate;
import org.mybatis.spring.mapper.MapperScannerConfigurer;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.EnvironmentAware;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.core.env.Environment;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;

import javax.sql.DataSource;
import java.sql.SQLException;
import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

/**
 * @author 鼠品味
 */
@Configuration
@Slf4j
public class ShardingMybatisConfig implements EnvironmentAware {

    private Environment environment;
    /**
     * 配置数据源1，数据源的名称最好要有一定的规则，方便配置分库的计算规则
     *
     * @return
     */
    @Bean
    public DataSource ds0() {
        DruidDataSource dataSource = new DruidDataSource();
        String dataUrl = environment.getProperty("spring.datasource.ds0.url");
        String username = environment.getProperty("spring.datasource.ds0.username");
        String password = environment.getProperty("spring.datasource.ds0.password");
        String driverClassName = environment.getProperty("spring.datasource.ds0.driver-class-name");
        String filters = environment.getProperty("spring.datasource.filters");
        dataSource.setUrl(dataUrl);
        dataSource.setDriverClassName(driverClassName);
        dataSource.setPassword(password);
        dataSource.setUsername(username);
        dataSource.setMinIdle(10);
        dataSource.setMaxActive(500);
        dataSource.setValidationQuery("select 1 from dual");
        dataSource.setMaxWait(10000);
        dataSource.setRemoveAbandoned(true);
        dataSource.setRemoveAbandonedTimeout(1800);
        try {
            dataSource.setFilters(filters);
        } catch (SQLException e) {
            log.error("durid filter fail", e);
        }
        return dataSource;
    }

    /**
     * 配置数据源1，数据源的名称最好要有一定的规则，方便配置分库的计算规则
     *
     * @return
     */
    @Bean
    public DataSource ds1() {
        DruidDataSource dataSource = new DruidDataSource();
        String dataUrl = environment.getProperty("spring.datasource.ds1.url");
        String username = environment.getProperty("spring.datasource.ds1.username");
        String password = environment.getProperty("spring.datasource.ds1.password");
        String driverClassName = environment.getProperty("spring.datasource.ds1.driver-class-name");
        String filters = environment.getProperty("spring.datasource.filters");
        dataSource.setUrl(dataUrl);
        dataSource.setDriverClassName(driverClassName);
        dataSource.setPassword(password);
        dataSource.setUsername(username);
        dataSource.setMinIdle(10);
        dataSource.setMaxActive(500);
        dataSource.setValidationQuery("select 1 from dual");
        dataSource.setMaxWait(10000);
        dataSource.setRemoveAbandoned(true);
        dataSource.setRemoveAbandonedTimeout(1800);
        try {
            dataSource.setFilters(filters);
        } catch (SQLException e) {
            log.error("durid filter fail", e);
        }
        return dataSource;
    }



    /**
     * 创建sharding-jdbc的数据源DataSource，MybatisAutoConfiguration会使用此数据源
     *
     * @param
     * @return
     * @throws SQLException
     */
    @Bean(name = "shardingDataSource")
    public DataSource shardingDataSource(@Qualifier("ds0") DataSource ds0,
                                         @Qualifier("ds1") DataSource ds1) throws SQLException {
        ShardingRuleConfiguration shardingRuleConfig = new ShardingRuleConfiguration();
        shardingRuleConfig.getTableRuleConfigs().add(getCommonTableRuleConfiguration("wallet_flow"));
        shardingRuleConfig.getTableRuleConfigs().add(getCommonTableRuleConfiguration("fund_flow"));
        shardingRuleConfig.getTableRuleConfigs().add(getCommonTableRuleConfiguration("fund_flow_desc"));
        shardingRuleConfig.getTableRuleConfigs().add(getCommonTableRuleConfiguration("service_settlement_money_flow"));
//        shardingRuleConfig.getTableRuleConfigs().add(getCommonTableRuleConfiguration("nuts_order_detail"));
//        shardingRuleConfig.getTableRuleConfigs().add(getCommonTableRuleConfiguration("nuts_change_order_logging"));
//        shardingRuleConfig.getTableRuleConfigs().add(getCommonTableRuleConfiguration("nuts_change_order",true,Integer.parseInt(environment.getProperty("spring.datasource.default-tableNum")),"order_no"));
//        shardingRuleConfig.getTableRuleConfigs().add(getCommonTableRuleConfiguration("nuts_cancel_order",true,Integer.parseInt(environment.getProperty("spring.datasource.default-tableNum")),"order_no"));
//        shardingRuleConfig.getTableRuleConfigs().add(getCommonTableRuleConfiguration("nuts_order_index",true,Integer.parseInt(environment.getProperty("spring.datasource.default-tableNum")),"order_no"));
        // 绑定组，关联查询同一组的表 只需要定位一个表 其他表不需再次定位
        shardingRuleConfig.getBindingTableGroups().add("wallet_flow,fund_flow,fund_flow_desc,service_settlement_money_flow");
        shardingRuleConfig.setDefaultDatabaseShardingStrategyConfig(new StandardShardingStrategyConfiguration("wallet_id", new DatabaseShardingAlgorithm()));
        shardingRuleConfig.setDefaultTableShardingStrategyConfig(new StandardShardingStrategyConfiguration("wallet_id", new TablePreciseShardingAlgorithm(), new TableRangeShardingAlgorithm()));
        // 默认id生成算法 雪花算法
        KeyGeneratorConfiguration keyGeneratorConfiguration = new KeyGeneratorConfiguration("SNOWFLAKE","id");
        shardingRuleConfig.setDefaultKeyGeneratorConfig(keyGeneratorConfiguration);
        shardingRuleConfig.setDefaultDataSourceName("ds0");
        Map<String, DataSource> dataSourceMap = new HashMap<>();
        dataSourceMap.put("ds0", ds0);
        dataSourceMap.put("ds1", ds1);

        Properties properties = new Properties();
        // 显示分库分表后的sql
//         properties.setProperty("sql.show", environment.getProperty("spring.datasource.sql.show"));
        return ShardingDataSourceFactory.createDataSource(dataSourceMap, shardingRuleConfig, properties);


    }

    private TableRuleConfiguration getCommonTableRuleConfiguration(String tableName) {
        return getCommonTableRuleConfiguration(tableName, true);
    }

    private TableRuleConfiguration getCommonTableRuleConfiguration(String tableName, Boolean keyGener) {
        /**
         * 测试和本地环境默认2个表
         * 其他环境默认32张表
         * 方便开发
         */
        return getCommonTableRuleConfiguration(tableName, keyGener, Integer.parseInt(environment.getProperty("spring.datasource.default-tableNum")));
    }

    private TableRuleConfiguration getCommonTableRuleConfiguration(String tableName, Boolean keyGener, int tableNum) {
        return getCommonTableRuleConfiguration(tableName, true, tableNum);

    }

    private TableRuleConfiguration getCommonTableRuleConfiguration(String tableName, Boolean keyGener, int tableNum, String shardingColumn) {
        StringBuilder builder = new StringBuilder();
        DecimalFormat format = new DecimalFormat("000");
        for (int i = 1; i <= tableNum; i++) {
            builder.append(",ds0." + tableName + "_" + format.format(i))
                    .append(",ds1." + tableName + "_" + format.format(i)) ;

        }
        TableRuleConfiguration result = new TableRuleConfiguration(tableName,builder.toString().substring(1));

        // 如果不是32 则不是默认的分库分表规则 重新定义分库分表规则
        DatabaseShardingAlgorithm databaseShardingAlgorithm = new DatabaseShardingAlgorithm();
        databaseShardingAlgorithm.setTableNum(tableNum);
//        if(tableNum != ShardingConstant.SUB_SINGLE_TABLE_COUNT){
//            databaseShardingAlgorithm.setTableNum(tableNum);
//        }
        StandardShardingStrategyConfiguration dataConfiguration = new StandardShardingStrategyConfiguration(shardingColumn, databaseShardingAlgorithm);
        result.setDatabaseShardingStrategyConfig(dataConfiguration);
        result.setKeyGeneratorConfig(getKeyGeneratorConfiguration());

        // 需要自动生成的列 默认生成算法DefaultKeyGenerator()，如果选择了id自动生成，则mapper中的insert语句中不要包含id列。

        return result;
    }


    private static KeyGeneratorConfiguration getKeyGeneratorConfiguration() {
        KeyGeneratorConfiguration result = new KeyGeneratorConfiguration("SNOWFLAKE", kdSubKey);
        return result;
    }

    private static String kdSubKey = "id";// 主键

    /**
     * 需要手动配置事务管理器
     *
     * @param dataSource
     * @return
     */
    @Bean("sdTransactionManager")
    public DataSourceTransactionManager transactionManager(@Qualifier("shardingDataSource") DataSource dataSource) {
        return new DataSourceTransactionManager(dataSource);
    }

    @Bean(name = "subSqlSessionFactory")
    @Primary
    public SqlSessionFactory subSqlSessionFactory(@Qualifier("shardingDataSource") DataSource dataSource) throws Exception {
        SqlSessionFactoryBean bean = new SqlSessionFactoryBean();
        bean.setDataSource(dataSource);
        bean.setMapperLocations(new PathMatchingResourcePatternResolver().getResources("classpath:mybatis/sub/*.xml"));
        //设置别名包
        bean.setTypeAliasesPackage("com.songshu.snt.ufc.wallet.sub.entity");
        return bean.getObject();
    }

    @Bean(name = "subSqlSessionTemplate")
    public SqlSessionTemplate testSqlSessionTemplate(@Qualifier("subSqlSessionFactory") SqlSessionFactory sqlSessionFactory) throws Exception {
        return new SqlSessionTemplate(sqlSessionFactory);
    }


    @Bean(name = "shardingMapperScannerConfigurer")
    public MapperScannerConfigurer shardingMapperScannerConfigurer() {
        MapperScannerConfigurer mapperScannerConfigurer = new MapperScannerConfigurer();
        mapperScannerConfigurer.setSqlSessionFactoryBeanName("subSqlSessionFactory");
        mapperScannerConfigurer.setBasePackage("com.songshu.snt.ufc.wallet.sub.dao");
        return mapperScannerConfigurer;
    }

    @Override
    public void setEnvironment(Environment environment) {
        this.environment = environment;
    }
}
