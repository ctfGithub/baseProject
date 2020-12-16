package com.springbootbasepackage.shardingJdbcConfig;

import com.alibaba.fastjson.JSON;
import com.google.common.collect.Range;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.shardingsphere.api.sharding.standard.PreciseShardingAlgorithm;
import org.apache.shardingsphere.api.sharding.standard.PreciseShardingValue;
import org.apache.shardingsphere.api.sharding.standard.RangeShardingAlgorithm;
import org.apache.shardingsphere.api.sharding.standard.RangeShardingValue;

import java.util.ArrayList;
import java.util.Collection;


/**
 * 分表算法
 */
@Slf4j
public class OrderTableShardingAlgorithm implements PreciseShardingAlgorithm<Long>, RangeShardingAlgorithm<Long> {

    /**
     * 用于处理=和IN的分片
     */
    @Override
    public String doSharding(Collection<String> tableNames, PreciseShardingValue<Long> shardingValue) {
        log.info("tableNames:{}", JSON.toJSONString(tableNames));
        log.info("shardingValue:{}", JSON.toJSONString(shardingValue));

        String tableName = "";

        // 只做单库分表
//        if ("t_user".equalsIgnoreCase(shardingValue.getLogicTableName())) {
//            for (String each : tableNames) {
//                if (each.endsWith(shardingValue.getValue() % tableNames.size() + "")) {
//                    log.info("table:{}", each);
//                    tableName = each;
//                    break;
//                }
//            }
//        }

        // 分库分表
        if ("t_order".equalsIgnoreCase(shardingValue.getLogicTableName()) ||
                "t_order_item".equalsIgnoreCase(shardingValue.getLogicTableName())) {
            for (String each : tableNames) {
                if (each.endsWith(shardingValue.getValue() % tableNames.size() + "")) {
                    tableName = each;
                    break;
                }
            }
        }

        log.info("tableName:{}", tableName);
        if (StringUtils.isNotEmpty(tableName)) {
            return tableName;
        }

        throw new UnsupportedOperationException();
    }

    /**
     * 实现between and查询
     */
    @Override
    public Collection<String> doSharding(Collection<String> collection, RangeShardingValue<Long> rangeShardingValue) {

        log.info("collection:{}", JSON.toJSONString(collection));
        log.info("rangeShardingValue:{}", JSON.toJSONString(rangeShardingValue));

        Collection<String> collect = new ArrayList<>();
        Range<Long> valueRange = rangeShardingValue.getValueRange();
        for (Long i = valueRange.lowerEndpoint(); i <= valueRange.upperEndpoint(); i++) {
            for (String each : collection) {
                if (each.endsWith(i % collection.size() + "")) {
                    collect.add(each);
                }
            }
        }
        return collect;
    }
}
