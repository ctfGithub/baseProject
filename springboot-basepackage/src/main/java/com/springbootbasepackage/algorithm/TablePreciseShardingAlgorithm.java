package com.springbootbasepackage.algorithm;


import io.shardingsphere.api.algorithm.sharding.PreciseShardingValue;
import io.shardingsphere.api.algorithm.sharding.standard.PreciseShardingAlgorithm;
import lombok.extern.slf4j.Slf4j;

import java.text.DecimalFormat;
import java.util.Collection;

/**
 * @description: 分表算法：     ==和IN的分表算法实现
 * <p>
 * 中间变量=shardingKey %（库数量*单个库的表数量）
 * 表序号=中间变量%单个库的表数量
 */
@Slf4j
public class TablePreciseShardingAlgorithm implements PreciseShardingAlgorithm<String> {

    /**
     * 数据库数量
     * 可定制
     */
    protected static final int DATA_NODE_NUM = 2;

    @Override
    public String doSharding(final Collection<String> availableTargetNames, final PreciseShardingValue<String> shardingValue) {

        int temp = shardingValue.getValue().hashCode() % (DATA_NODE_NUM * availableTargetNames.size());
        temp = Math.abs(temp);
        String tableIndex;
        for (String each : availableTargetNames) {
            tableIndex = each.substring(each.lastIndexOf("_") + 1);
            long num = temp % availableTargetNames.size() + 1L;
            if (tableIndex.equals(new DecimalFormat("000").format(num))) {
                return each;
            }
        }
        throw new UnsupportedOperationException();
    }


}