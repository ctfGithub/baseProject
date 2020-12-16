package com.springbootbasepackage.shardingJdbcConfig;

import com.alibaba.druid.pool.DruidDataSource;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.shardingsphere.api.config.sharding.KeyGeneratorConfiguration;
import org.apache.shardingsphere.api.config.sharding.ShardingRuleConfiguration;
import org.apache.shardingsphere.api.config.sharding.TableRuleConfiguration;
import org.apache.shardingsphere.api.config.sharding.strategy.StandardShardingStrategyConfiguration;
import org.apache.shardingsphere.shardingjdbc.api.ShardingDataSourceFactory;
import org.springframework.context.EnvironmentAware;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.beans.factory.annotation.Value;


import javax.sql.DataSource;
import java.sql.SQLException;
import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.Set;

/**
 * sharding jdbc的配置
 */
@Configuration
@Slf4j
public class DataSourceConfig implements EnvironmentAware {

    private static Integer tableNum = 2;


    private static String kdSubKey = "goods_name";


    private Environment environment;


    @Value("${spring.datasource.ds0.url}")
    private String url;


    @Override
    public void setEnvironment(Environment environment) {
        this.environment = environment;
    }


    //开始配置数据源
    @Bean(name = "shardingDataSource")
    public DataSource shardingDataSource() throws SQLException {

        //创建数据库
        Map<String, DataSource> dataSourceMap = this.buildDataSourceMap();
        Set<String> nameSet = dataSourceMap.keySet();
        String firstDs = (String) nameSet.toArray()[0];

        ShardingRuleConfiguration shardingRuleConfig = new ShardingRuleConfiguration();
        shardingRuleConfig.getTableRuleConfigs().add(getCommonTableRuleConfiguration("goods_0",dataSourceMap));
        shardingRuleConfig.getTableRuleConfigs().add(getCommonTableRuleConfiguration("goods_1",dataSourceMap));
        // 绑定组，关联查询同一组的表 只需要定位一个表 其他表不需再次定位
        shardingRuleConfig.getBindingTableGroups().add("goods_0,goods_1");
        shardingRuleConfig.setDefaultDatabaseShardingStrategyConfig(new StandardShardingStrategyConfiguration(kdSubKey, new OrderDatabaseShardingAlgorithm()));
        shardingRuleConfig.setDefaultTableShardingStrategyConfig(new StandardShardingStrategyConfiguration(kdSubKey, new OrderTableShardingAlgorithm()));
        // 默认id生成算法 雪花算法
        shardingRuleConfig.setDefaultDataSourceName(firstDs);
        KeyGeneratorConfiguration keyGeneratorConfiguration = new KeyGeneratorConfiguration("SNOWFLAKE","id");
        shardingRuleConfig.setDefaultKeyGeneratorConfig(keyGeneratorConfiguration);
        Properties properties = new Properties();

        return ShardingDataSourceFactory.createDataSource(dataSourceMap, shardingRuleConfig, properties);

    }


    private Map<String, DataSource> buildDataSourceMap() {

        // 配置真实数据源
        Map<String, DataSource> dataSourceMap = new HashMap<>();

        int i = 0;
        while(true) {

            DruidDataSource dataSource = new DruidDataSource();
            String dataUrl = environment.getProperty("spring.datasource.ds"+ i +".url");
            if(StringUtils.isEmpty(dataUrl)) {
                break;
            }

            String username = environment.getProperty("spring.datasource.ds"  + i + ".username");
            String password = environment.getProperty("spring.datasource.ds"  + i + ".password");
            String driverClassName = environment.getProperty("spring.datasource.ds"  + i + ".driver-class-name");
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
            dataSourceMap.put("ds"+i, dataSource);
            i ++;
        }

        return dataSourceMap;
    }

    //表规则--用于路由
    private TableRuleConfiguration getCommonTableRuleConfiguration(String tableName, Map<String, DataSource> dataSourceMap) {
        return getCommonTableRuleConfiguration(tableName, dataSourceMap, true);
    }

    private TableRuleConfiguration getCommonTableRuleConfiguration(
            String tableName,
            Map<String, DataSource> dataSourceMap,
            Boolean keyGener
    ) {

        StringBuilder builder = new StringBuilder();//分片规则找到所有的表
        DecimalFormat format = new DecimalFormat("00");
        for (int i = 1; i <= tableNum; i++) {
            builder.append(",ds0." + tableName + "_" + format.format(i))
                    .append(",ds1." + tableName + "_" + format.format(i));
        }
//        for (int i = 1; i <= tableNum; i++) {
//            for(String year : dataSourceMap.keySet()) {
//                builder.append("," + year + "." + tableName + "_" + format.format(i));
//            }
//
//        }
        TableRuleConfiguration result = new TableRuleConfiguration(tableName,builder.toString().substring(1));
        KeyGeneratorConfiguration keyGeneratorConfiguration = new KeyGeneratorConfiguration("SNOWFLAKE", "id");
        result.setKeyGeneratorConfig(keyGeneratorConfiguration);

        StandardShardingStrategyConfiguration dataConfiguration = new StandardShardingStrategyConfiguration(kdSubKey,
                new OrderDatabaseShardingAlgorithm()
        );

        result.setTableShardingStrategyConfig(new StandardShardingStrategyConfiguration(kdSubKey,
                new OrderTableShardingAlgorithm()));
        result.setDatabaseShardingStrategyConfig(dataConfiguration);
        result.setKeyGeneratorConfig(getKeyGeneratorConfiguration());

        return result;
    }

    private static KeyGeneratorConfiguration getKeyGeneratorConfiguration() {
        KeyGeneratorConfiguration result = new KeyGeneratorConfiguration("SNOWFLAKE", kdSubKey);
        return result;
    }

}
