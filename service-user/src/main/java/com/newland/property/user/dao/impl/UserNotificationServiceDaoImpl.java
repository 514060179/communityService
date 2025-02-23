package com.newland.property.user.dao.impl;

import com.alibaba.fastjson.JSONObject;
import com.newland.property.core.base.dao.BaseServiceDao;
import com.newland.property.user.dao.IUserNotificationServiceDao;
import com.newland.property.utils.constant.ResponseConstant;
import com.newland.property.utils.exception.DAOException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service("userNotificationServiceDaoImpl")
public class UserNotificationServiceDaoImpl extends BaseServiceDao implements IUserNotificationServiceDao {

    private static Logger logger = LoggerFactory.getLogger(UserNotificationServiceDaoImpl.class);

    @Override
    public void saveUserNotification(Map info) throws DAOException {
        logger.debug("保存用户通知信息Instance 入参 info : {}", info);

        int saveFlag = sqlSessionTemplate.insert("userNotificationServiceDaoImpl.saveUserNotification", info);

        if (saveFlag < 1) {
            throw new DAOException(ResponseConstant.RESULT_PARAM_ERROR, "保存用户通知信息Instance数据失败：" + JSONObject.toJSONString(info));
        }
    }

    @Override
    public void updateUserNotification(Map info) throws DAOException {
        logger.debug("更新用户通知信息Instance 入参 info : {}", info);

        int saveFlag = sqlSessionTemplate.insert("userNotificationServiceDaoImpl.updateUserNotification", info);

        if (saveFlag < 1) {
            throw new DAOException(ResponseConstant.RESULT_PARAM_ERROR, "更新用户通知信息Instance数据失败：" + JSONObject.toJSONString(info));
        }
    }

    @Override
    public void deleteUserNotification(Map info) throws DAOException {
        logger.debug("删除用户通知信息Instance 入参 info : {}", info);

        int saveFlag = sqlSessionTemplate.insert("userNotificationServiceDaoImpl.updateUserNotification", info);

        if (saveFlag < 1) {
            throw new DAOException(ResponseConstant.RESULT_PARAM_ERROR, "删除用户通知信息Instance数据失败：" + JSONObject.toJSONString(info));
        }
    }

    @Override
    public void readUserNotification(Map info) throws DAOException {
        logger.debug("读取用户通知信息Instance 入参 info : {}", info);

        sqlSessionTemplate.insert("userNotificationServiceDaoImpl.readUserNotification", info);
    }

    @Override
    public void readTypeNotification(Map info) throws DAOException {
        logger.debug("读取用户通知信息Instance 入参 info : {}", info);

        sqlSessionTemplate.insert("userNotificationServiceDaoImpl.readTypeNotification", info);
    }

    @Override
    public List<Map> queryUserNotifications(Map info) {
        logger.debug("查询用户消息中心信息 入参 info : {}",info);

        List<Map> userNotifications = sqlSessionTemplate.selectList("userNotificationServiceDaoImpl.queryUserNotifications",info);

        return userNotifications;
    }

    @Override
    public int queryUserNotificationsCount(Map info) {
        logger.debug("查询用户消息中心信息 入参 info : {}", info);

        List<Map> userNotifications = sqlSessionTemplate.selectList("userNotificationServiceDaoImpl.queryUserNotificationsCount",
                info);
        if (userNotifications.size() < 1) {
            return 0;
        }

        return Integer.parseInt(userNotifications.get(0).get("count").toString());
    }
}
