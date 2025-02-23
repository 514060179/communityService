package com.newland.property.common.dao.impl;

import com.newland.property.common.dao.INotificationTypeServiceDao;
import com.newland.property.core.base.dao.BaseServiceDao;
import com.newland.property.utils.exception.DAOException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * @author Administrator
 */
@Service("notificationTypeServiceDaoImpl")
public class NotificationTypeServiceDaoImpl  extends BaseServiceDao implements INotificationTypeServiceDao {

    private static Logger logger = LoggerFactory.getLogger(NotificationTypeServiceDaoImpl.class);

    @Override
    public List<Map> getNotificationTypes(Map info) throws DAOException {
        logger.debug("查询通知类型信息 入参 info : {}", info);
        return sqlSessionTemplate.selectList("notificationTypeServiceDaoImpl.getNotificationTypes", info);
    }

    @Override
    public int getNotificationTypesCount(Map info) {
        logger.debug("查询通知类型信息 入参 info : {}", info);
        List<Map> notificationTypes = sqlSessionTemplate.selectList("notificationTypeServiceDaoImpl" +
                ".getNotificationTypesCount", info);
        if (notificationTypes.isEmpty()) {
            return 0;
        }
        return Integer.parseInt(notificationTypes.get(0).get("count").toString());
    }
}
