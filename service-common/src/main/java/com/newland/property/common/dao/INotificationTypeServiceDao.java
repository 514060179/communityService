package com.newland.property.common.dao;

import com.newland.property.utils.exception.DAOException;
import java.util.List;
import java.util.Map;

/**
 * @author Moonny
 */
public interface INotificationTypeServiceDao {

    List<Map> getNotificationTypes(Map info) throws DAOException;

    int getNotificationTypesCount(Map info);
}
