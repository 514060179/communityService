package com.newland.property.fee.dao.impl;

import com.newland.property.core.base.dao.BaseServiceDao;
import com.newland.property.fee.dao.IInitializePayFeeServiceDao;
import com.newland.property.utils.exception.DAOException;
import org.slf4j.Logger;
import com.newland.property.core.log.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.Map;

/**
 * 缴费审核服务 与数据库交互
 * Created by wuxw on 2017/4/5.
 */
@Service("initializePayFeeServiceDaoImpl")
//@Transactional
public class InitializePayFeeServiceDaoImpl extends BaseServiceDao implements IInitializePayFeeServiceDao {

    private static Logger logger = LoggerFactory.getLogger(InitializePayFeeServiceDaoImpl.class);



    @Override
    public int deletePayFee(Map info) throws DAOException {
        logger.debug("删除费用信息 入参 info : {}", info);

        int deleteFlag = sqlSessionTemplate.delete("initializePayFeeServiceDaoImpl.deletePayFee", info);

        /*if (deleteFlag < 1) {
            throw new DAOException(ResponseConstant.RESULT_PARAM_ERROR, "building_owner初始化失败：" + JSONObject.toJSONString(info));
        }*/
        return deleteFlag;
    }

    @Override
    public int deletePayFeeDetail(Map info) {
        logger.debug("删除费用信息 入参 info : {}", info);

        int deleteFlag = sqlSessionTemplate.delete("initializePayFeeServiceDaoImpl.deletePayFeeDetail", info);

        /*if (deleteFlag < 1) {
            throw new DAOException(ResponseConstant.RESULT_PARAM_ERROR, "building_owner初始化失败：" + JSONObject.toJSONString(info));
        }*/
        return deleteFlag;
    }

    @Override
    public int deleteReportOweFee(Map info) {
        logger.debug("删除费用信息 入参 info : {}", info);

        int deleteFlag = sqlSessionTemplate.delete("initializePayFeeServiceDaoImpl.deleteReportOweFee", info);

        /*if (deleteFlag < 1) {
            throw new DAOException(ResponseConstant.RESULT_PARAM_ERROR, "building_owner初始化失败：" + JSONObject.toJSONString(info));
        }*/
        return deleteFlag;
    }

    @Override
    public int deletePayFeeDetailMonth(Map info) {
        logger.debug("删除费用信息 入参 info : {}", info);

        int deleteFlag = sqlSessionTemplate.delete("initializePayFeeServiceDaoImpl.deletePayFeeDetailMonth", info);

        /*if (deleteFlag < 1) {
            throw new DAOException(ResponseConstant.RESULT_PARAM_ERROR, "building_owner初始化失败：" + JSONObject.toJSONString(info));
        }*/
        return deleteFlag;
    }

}
