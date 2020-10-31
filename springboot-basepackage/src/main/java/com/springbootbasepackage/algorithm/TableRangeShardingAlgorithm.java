package com.springbootbasepackage.algorithm;


import com.google.common.collect.Range;
import org.apache.shardingsphere.api.sharding.standard.RangeShardingAlgorithm;
import org.apache.shardingsphere.api.sharding.standard.RangeShardingValue;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Collection;

/**
 * @description: Between的分表算法实现
 */
public class TableRangeShardingAlgorithm implements RangeShardingAlgorithm<Long> {

    @Override
    public Collection<String> doSharding(Collection<String> collection, RangeShardingValue<Long> rangeShardingValue) {
        Collection<String> collect = new ArrayList<>();
        Range<Long> valueRange = rangeShardingValue.getValueRange();
        String tableIndex;
        for (Long i = valueRange.lowerEndpoint(); i <= valueRange.upperEndpoint(); i++) {
            int temp = (i + "").hashCode() % (TablePreciseShardingAlgorithm.DATA_NODE_NUM * collection.size());
            temp = Math.abs(temp);
            for (String each : collection) {
                tableIndex = each.substring(each.lastIndexOf("_") + 1);
                long num = temp % collection.size() + 1L;
                if (tableIndex.equals(new DecimalFormat("000").format(num))) {
                    collect.add(each);
                }
            }
        }
        return collect;
    }
}