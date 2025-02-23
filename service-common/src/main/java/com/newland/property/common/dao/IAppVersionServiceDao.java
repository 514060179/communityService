package com.newland.property.common.dao;

import com.newland.property.utils.exception.DAOException;

import java.util.List;
import java.util.Map;

public interface IAppVersionServiceDao {
    /**
     * 查询App版本
     * @param info bId 信息
     * @return 应用信息
     * @throws DAOException DAO异常
     */
    List<Map> getAppVersion(Map info) throws DAOException;
}
