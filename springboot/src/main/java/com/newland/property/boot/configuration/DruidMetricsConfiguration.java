package com.newland.property.boot.configuration;

import org.apache.shardingsphere.shardingjdbc.jdbc.core.datasource.ShardingDataSource;
import com.alibaba.druid.pool.DruidDataSource;
import com.newland.property.boot.components.DruidCollector;
import io.micrometer.core.instrument.MeterRegistry;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.context.annotation.Configuration;

import javax.sql.DataSource;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

/**
 * @author simon feng
 * @date 2024/9/7 16:06
 * @description druid监控
 */
@Configuration
@ConditionalOnClass({DruidDataSource.class, MeterRegistry.class})
public class DruidMetricsConfiguration {
    private final MeterRegistry registry;

    public DruidMetricsConfiguration(MeterRegistry registry) {
        this.registry = registry;
    }

    @Autowired
    public void bindMetricsRegistryToDruidDataSources(Collection<DataSource> dataSources) throws SQLException {
        List<DruidDataSource> druidDataSources = new ArrayList<>();
        for (DataSource dataSource : dataSources) {
            if (dataSource instanceof ShardingDataSource) {
                ShardingDataSource shardingDataSource = dataSource.unwrap(ShardingDataSource.class);
                Map<String, DataSource> dataSourceMap = shardingDataSource.getDataSourceMap();
                DataSource ds0 = dataSourceMap.get("ds0");
                if (ds0 != null && (ds0 instanceof DruidDataSource)){
                    druidDataSources.add((DruidDataSource)ds0);
                }
                DataSource ds1 = dataSourceMap.get("ds1");
                if (ds1 != null && (ds1 instanceof DruidDataSource)){
                    druidDataSources.add((DruidDataSource)ds1);
                }
            }
        }
        if (!druidDataSources.isEmpty()) {
            DruidCollector druidCollector = new DruidCollector(druidDataSources, registry);
            druidCollector.register();
        }
    }
}
