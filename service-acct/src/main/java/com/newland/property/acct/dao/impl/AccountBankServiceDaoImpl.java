package com.newland.property.acct.dao.impl;

import com.alibaba.fastjson.JSONObject;
import com.newland.property.utils.constant.ResponseConstant;
import com.newland.property.utils.exception.DAOException;
import com.newland.property.core.base.dao.BaseServiceDao;
import com.newland.property.acct.dao.IAccountBankServiceDao;
import org.slf4j.Logger;
import com.newland.property.core.log.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * 开户行服务 与数据库交互
 * Created by wuxw on 2017/4/5.
 */
@Service("accountBankServiceDaoImpl")
//@Transactional
public class AccountBankServiceDaoImpl extends BaseServiceDao implements IAccountBankServiceDao {

    private static Logger logger = LoggerFactory.getLogger(AccountBankServiceDaoImpl.class);





    /**
     * 保存开户行信息 到 instance
     * @param info   bId 信息
     * @throws DAOException DAO异常
     */
    @Override
    public void saveAccountBankInfo(Map info) throws DAOException {
        logger.debug("保存开户行信息Instance 入参 info : {}",info);

        int saveFlag = sqlSessionTemplate.insert("accountBankServiceDaoImpl.saveAccountBankInfo",info);

        if(saveFlag < 1){
            throw new DAOException(ResponseConstant.RESULT_PARAM_ERROR,"保存开户行信息Instance数据失败："+ JSONObject.toJSONString(info));
        }
    }


    /**
     * 查询开户行信息（instance）
     * @param info bId 信息
     * @return List<Map>
     * @throws DAOException DAO异常
     */
    @Override
    public List<Map> getAccountBankInfo(Map info) throws DAOException {
        logger.debug("查询开户行信息 入参 info : {}",info);

        List<Map> businessAccountBankInfos = sqlSessionTemplate.selectList("accountBankServiceDaoImpl.getAccountBankInfo",info);

        return businessAccountBankInfos;
    }


    /**
     * 修改开户行信息
     * @param info 修改信息
     * @throws DAOException DAO异常
     */
    @Override
    public void updateAccountBankInfo(Map info) throws DAOException {
        logger.debug("修改开户行信息Instance 入参 info : {}",info);

        int saveFlag = sqlSessionTemplate.update("accountBankServiceDaoImpl.updateAccountBankInfo",info);

        if(saveFlag < 1){
            throw new DAOException(ResponseConstant.RESULT_PARAM_ERROR,"修改开户行信息Instance数据失败："+ JSONObject.toJSONString(info));
        }
    }

     /**
     * 查询开户行数量
     * @param info 开户行信息
     * @return 开户行数量
     */
    @Override
    public int queryAccountBanksCount(Map info) {
        logger.debug("查询开户行数据 入参 info : {}",info);

        List<Map> businessAccountBankInfos = sqlSessionTemplate.selectList("accountBankServiceDaoImpl.queryAccountBanksCount", info);
        if (businessAccountBankInfos.size() < 1) {
            return 0;
        }

        return Integer.parseInt(businessAccountBankInfos.get(0).get("count").toString());
    }


}
