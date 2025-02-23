package com.newland.property.user.dao.impl;

import com.newland.property.core.base.dao.BaseServiceDao;
import com.newland.property.user.dao.IUserAliasServiceDao;
import com.newland.property.utils.exception.DAOException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service("userAliasServiceDaoImpl")
public class UserAliasServiceDaoImpl extends BaseServiceDao implements IUserAliasServiceDao {

    private static Logger logger = LoggerFactory.getLogger(UserAliasServiceDaoImpl.class);

    @Override
    public int saveUserAlias(Map info) throws DAOException {
        logger.debug("保存 saveUserAlias 入参 info : {}", info);

        int saveFlag = sqlSessionTemplate.insert("userAliasServiceDaoImpl.saveUserAlias", info);

        return saveFlag;
    }

    @Override
    public List<Map> getUserAlias(Map info) throws DAOException {
        logger.debug("查询 getUserAlias 入参 info : {}", info);

        List<Map> dataList = sqlSessionTemplate.selectList("userAliasServiceDaoImpl.getUserAlias", info);

        return dataList;
    }

    @Override
    public int updateUserAlias(Map info) throws DAOException {
        logger.debug("修改 updateUserAlias 入参 info : {}", info);

        int saveFlag = sqlSessionTemplate.update("userAliasServiceDaoImpl.updateUserAlias", info);

        return saveFlag;
    }

    @Override
    public int queryUserAliasCount(Map info) {
        logger.debug("查询 queryUserAliasCount 入参 info : {}", info);

        List<Map> infos = sqlSessionTemplate.selectList("userAliasServiceDaoImpl.queryUserAliasCount", info);
        if (infos.size() < 1) {
            return 0;
        }

        return Integer.parseInt(infos.get(0).get("count").toString());
    }
}
