package com.newland.property.common.dao.impl;

import com.newland.property.common.dao.IAppVersionServiceDao;
import com.newland.property.core.base.dao.BaseServiceDao;
import com.newland.property.core.log.LoggerFactory;
import com.newland.property.utils.exception.DAOException;
import org.slf4j.Logger;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service("appVersionServiceDaoImpl")
public class AppVersionServiceDaoImpl  extends BaseServiceDao implements IAppVersionServiceDao {

    private static Logger logger = LoggerFactory.getLogger(AppVersionServiceDaoImpl.class);

    @Override
    public List<Map> getAppVersion(Map info) throws DAOException {
        logger.debug("查询应用信息 入参 info : {}",info);

        List<Map> appVersionInfos = sqlSessionTemplate.selectList("appVersionServiceDaoImpl.getAppVersion",info);

        return appVersionInfos;
    }
}
