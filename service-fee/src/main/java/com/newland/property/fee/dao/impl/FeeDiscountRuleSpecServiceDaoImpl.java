package com.newland.property.fee.dao.impl;

import com.alibaba.fastjson.JSONObject;
import com.newland.property.utils.constant.ResponseConstant;
import com.newland.property.utils.exception.DAOException;
import com.newland.property.core.base.dao.BaseServiceDao;
import com.newland.property.fee.dao.IFeeDiscountRuleSpecServiceDao;
import org.slf4j.Logger;
import com.newland.property.core.log.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * 折扣规则配置服务 与数据库交互
 * Created by wuxw on 2017/4/5.
 */
@Service("feeDiscountRuleSpecServiceDaoImpl")
//@Transactional
public class FeeDiscountRuleSpecServiceDaoImpl extends BaseServiceDao implements IFeeDiscountRuleSpecServiceDao {

    private static Logger logger = LoggerFactory.getLogger(FeeDiscountRuleSpecServiceDaoImpl.class);





    /**
     * 保存折扣规则配置信息 到 instance
     * @param info   bId 信息
     * @throws DAOException DAO异常
     */
    @Override
    public void saveFeeDiscountRuleSpecInfo(Map info) throws DAOException {
        logger.debug("保存折扣规则配置信息Instance 入参 info : {}",info);

        int saveFlag = sqlSessionTemplate.insert("feeDiscountRuleSpecServiceDaoImpl.saveFeeDiscountRuleSpecInfo",info);

        if(saveFlag < 1){
            throw new DAOException(ResponseConstant.RESULT_PARAM_ERROR,"保存折扣规则配置信息Instance数据失败："+ JSONObject.toJSONString(info));
        }
    }


    /**
     * 查询折扣规则配置信息（instance）
     * @param info bId 信息
     * @return List<Map>
     * @throws DAOException DAO异常
     */
    @Override
    public List<Map> getFeeDiscountRuleSpecInfo(Map info) throws DAOException {
        logger.debug("查询折扣规则配置信息 入参 info : {}",info);

        List<Map> businessFeeDiscountRuleSpecInfos = sqlSessionTemplate.selectList("feeDiscountRuleSpecServiceDaoImpl.getFeeDiscountRuleSpecInfo",info);

        return businessFeeDiscountRuleSpecInfos;
    }


    /**
     * 修改折扣规则配置信息
     * @param info 修改信息
     * @throws DAOException DAO异常
     */
    @Override
    public void updateFeeDiscountRuleSpecInfo(Map info) throws DAOException {
        logger.debug("修改折扣规则配置信息Instance 入参 info : {}",info);

        int saveFlag = sqlSessionTemplate.update("feeDiscountRuleSpecServiceDaoImpl.updateFeeDiscountRuleSpecInfo",info);

        if(saveFlag < 1){
            throw new DAOException(ResponseConstant.RESULT_PARAM_ERROR,"修改折扣规则配置信息Instance数据失败："+ JSONObject.toJSONString(info));
        }
    }

     /**
     * 查询折扣规则配置数量
     * @param info 折扣规则配置信息
     * @return 折扣规则配置数量
     */
    @Override
    public int queryFeeDiscountRuleSpecsCount(Map info) {
        logger.debug("查询折扣规则配置数据 入参 info : {}",info);

        List<Map> businessFeeDiscountRuleSpecInfos = sqlSessionTemplate.selectList("feeDiscountRuleSpecServiceDaoImpl.queryFeeDiscountRuleSpecsCount", info);
        if (businessFeeDiscountRuleSpecInfos.size() < 1) {
            return 0;
        }

        return Integer.parseInt(businessFeeDiscountRuleSpecInfos.get(0).get("count").toString());
    }


}
