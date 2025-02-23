package com.newland.property.user.dao.impl;

import com.newland.property.core.base.dao.BaseServiceDao;
import com.newland.property.user.dao.IInitializeOwneServiceDao;
import com.newland.property.utils.exception.DAOException;
import org.slf4j.Logger;
import com.newland.property.core.log.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.Map;

/**
 * 业主服务 与数据库交互
 * Created by wuxw on 2017/4/5.
 */
@Service("initializeOwnerServiceDaoImpl")
//@Transactional
public class InitializeOwnerServiceDaoImpl extends BaseServiceDao implements IInitializeOwneServiceDao {

    private static Logger logger = LoggerFactory.getLogger(InitializeOwnerServiceDaoImpl.class);



    @Override
    public int deleteBuildingOwner(Map info) throws DAOException {
        logger.debug("删除业主信息 入参 info : {}", info);

        int deleteFlag = sqlSessionTemplate.delete("initializeOwnerServiceDaoImpl.deleteBuildingOwner", info);

    /*    if (deleteFlag < 1) {
            throw new DAOException(ResponseConstant.RESULT_PARAM_ERROR, "building_owner初始化失败：" + JSONObject.toJSONString(info));
        }*/
        return deleteFlag;
    }

    @Override
    public int deleteUser(Map info) {
        logger.debug("删除业主信息 入参 info : {}", info);

        int deleteFlag = sqlSessionTemplate.delete("initializeOwnerServiceDaoImpl.deleteUser", info);

    /*    if (deleteFlag < 1) {
            throw new DAOException(ResponseConstant.RESULT_PARAM_ERROR, "building_owner初始化失败：" + JSONObject.toJSONString(info));
        }*/
        return deleteFlag;
    }

    @Override
    public int deleteOwnerAppUser(Map info) {
        logger.debug("删除业主信息 入参 info : {}", info);

        int deleteFlag = sqlSessionTemplate.delete("initializeOwnerServiceDaoImpl.deleteOwnerAppUser", info);

    /*    if (deleteFlag < 1) {
            throw new DAOException(ResponseConstant.RESULT_PARAM_ERROR, "building_owner初始化失败：" + JSONObject.toJSONString(info));
        }*/
        return deleteFlag;
    }
}
