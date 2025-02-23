package com.newland.property.user.dao;

import com.newland.property.utils.exception.DAOException;

import java.util.List;
import java.util.Map;

public interface IUserNotificationServiceDao {

    void saveUserNotification(Map info) throws DAOException;

    void updateUserNotification(Map info) throws DAOException;

    void deleteUserNotification(Map info) throws DAOException;

    void readUserNotification(Map info) throws DAOException;

    void readTypeNotification(Map info) throws DAOException;

    List<Map> queryUserNotifications(Map info);

    int queryUserNotificationsCount(Map info);
}
