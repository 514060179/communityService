package com.newland.property.fee.dao.impl;

import com.alibaba.fastjson.JSONObject;
import com.newland.property.utils.constant.ResponseConstant;
import com.newland.property.utils.exception.DAOException;
import com.newland.property.core.base.dao.BaseServiceDao;
import com.newland.property.fee.dao.IFeeDiscountSpecServiceDao;
import org.slf4j.Logger;
import com.newland.property.core.log.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * 费用折扣服务 与数据库交互
 * Created by wuxw on 2017/4/5.
 */
@Service("feeDiscountSpecServiceDaoImpl")
//@Transactional
public class FeeDiscountSpecServiceDaoImpl extends BaseServiceDao implements IFeeDiscountSpecServiceDao {

    private static Logger logger = LoggerFactory.getLogger(FeeDiscountSpecServiceDaoImpl.class);





    /**
     * 保存费用折扣信息 到 instance
     * @param info   bId 信息
     * @throws DAOException DAO异常
     */
    @Override
    public void saveFeeDiscountSpecInfo(Map info) throws DAOException {
        logger.debug("保存费用折扣信息Instance 入参 info : {}",info);

        int saveFlag = sqlSessionTemplate.insert("feeDiscountSpecServiceDaoImpl.saveFeeDiscountSpecInfo",info);

        if(saveFlag < 1){
            throw new DAOException(ResponseConstant.RESULT_PARAM_ERROR,"保存费用折扣信息Instance数据失败："+ JSONObject.toJSONString(info));
        }
    }


    /**
     * 查询费用折扣信息（instance）
     * @param info bId 信息
     * @return List<Map>
     * @throws DAOException DAO异常
     */
    @Override
    public List<Map> getFeeDiscountSpecInfo(Map info) throws DAOException {
        logger.debug("查询费用折扣信息 入参 info : {}",info);

        List<Map> businessFeeDiscountSpecInfos = sqlSessionTemplate.selectList("feeDiscountSpecServiceDaoImpl.getFeeDiscountSpecInfo",info);

        return businessFeeDiscountSpecInfos;
    }


    /**
     * 修改费用折扣信息
     * @param info 修改信息
     * @throws DAOException DAO异常
     */
    @Override
    public void updateFeeDiscountSpecInfo(Map info) throws DAOException {
        logger.debug("修改费用折扣信息Instance 入参 info : {}",info);

        int saveFlag = sqlSessionTemplate.update("feeDiscountSpecServiceDaoImpl.updateFeeDiscountSpecInfo",info);

        if(saveFlag < 1){
            throw new DAOException(ResponseConstant.RESULT_PARAM_ERROR,"修改费用折扣信息Instance数据失败："+ JSONObject.toJSONString(info));
        }
    }

     /**
     * 查询费用折扣数量
     * @param info 费用折扣信息
     * @return 费用折扣数量
     */
    @Override
    public int queryFeeDiscountSpecsCount(Map info) {
        logger.debug("查询费用折扣数据 入参 info : {}",info);

        List<Map> businessFeeDiscountSpecInfos = sqlSessionTemplate.selectList("feeDiscountSpecServiceDaoImpl.queryFeeDiscountSpecsCount", info);
        if (businessFeeDiscountSpecInfos.size() < 1) {
            return 0;
        }

        return Integer.parseInt(businessFeeDiscountSpecInfos.get(0).get("count").toString());
    }


}
