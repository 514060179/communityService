package com.newland.property.service.context;

import com.newland.property.utils.cache.ServiceSqlCache;
import com.newland.property.dto.system.ServiceSql;

/**
 * 数据查询工厂类
 * Created by wuxw on 2018/4/19.
 */
public class DataQueryFactory {

    public static DataQuery newInstance(){
        return new DataQuery();
    }

    public static ServiceSql getServiceSql(DataQuery dataQuery){
        return ServiceSqlCache.getServiceSql(dataQuery.getServiceCode());
    }
}
