package com.springbootbasepackage.algorithm;

import org.apache.shardingsphere.api.sharding.standard.PreciseShardingAlgorithm;
import org.apache.shardingsphere.api.sharding.standard.PreciseShardingValue;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Collection;

/**
 * @description: 分库算法
 * 如果使用分库分表结合使用的话，不能简单进行trade_no 取模操作，需要加一个中间变量用来打散到不同的子表
 * 中间变量 = shardingValue%（库数量*tableNum）
 * 库序号= 中间变量/tableNum
 */
@Component
public class DatabaseShardingAlgorithm implements PreciseShardingAlgorithm<String> {

    /**
     * 单个库的表数量
     */
    @Value("${spring.datasource.default-tableNum}")
    private int tableNum ;

    public int getTableNum() {
        return tableNum;
    }

    public void setTableNum(int tableNum) {
        this.tableNum = tableNum;
    }

    @Override
    public String doSharding(final Collection<String> availableTargetNames, final PreciseShardingValue<String> shardingValue) {
        int temp = shardingValue.getValue().hashCode()%(availableTargetNames.size()*getTableNum());
        temp = Math.abs(temp);
        for (String each : availableTargetNames) {
            if (each.endsWith(String.valueOf(temp/getTableNum()))) {
                return each;
            }
        }
        throw new UnsupportedOperationException();
    }
}