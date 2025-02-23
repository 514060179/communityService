package com.newland.property.user.dao;

import com.newland.property.utils.exception.DAOException;

import java.util.List;
import java.util.Map;

public interface IUserAliasServiceDao {

    int saveUserAlias(Map info) throws DAOException;

    List<Map> getUserAlias(Map info) throws DAOException;

    int updateUserAlias(Map info) throws DAOException;

    int queryUserAliasCount(Map info);
}
