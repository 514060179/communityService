package com.newland.property.fee.dao.impl;

import com.alibaba.fastjson.JSONObject;
import com.newland.property.utils.constant.ResponseConstant;
import com.newland.property.utils.exception.DAOException;
import com.newland.property.core.base.dao.BaseServiceDao;
import com.newland.property.fee.dao.IPayFeeConfigDiscountServiceDao;
import org.slf4j.Logger;
import com.newland.property.core.log.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * 费用项折扣服务 与数据库交互
 * Created by wuxw on 2017/4/5.
 */
@Service("payFeeConfigDiscountServiceDaoImpl")
//@Transactional
public class PayFeeConfigDiscountServiceDaoImpl extends BaseServiceDao implements IPayFeeConfigDiscountServiceDao {

    private static Logger logger = LoggerFactory.getLogger(PayFeeConfigDiscountServiceDaoImpl.class);





    /**
     * 保存费用项折扣信息 到 instance
     * @param info   bId 信息
     * @throws DAOException DAO异常
     */
    @Override
    public void savePayFeeConfigDiscountInfo(Map info) throws DAOException {
        logger.debug("保存费用项折扣信息Instance 入参 info : {}",info);

        int saveFlag = sqlSessionTemplate.insert("payFeeConfigDiscountServiceDaoImpl.savePayFeeConfigDiscountInfo",info);

        if(saveFlag < 1){
            throw new DAOException(ResponseConstant.RESULT_PARAM_ERROR,"保存费用项折扣信息Instance数据失败："+ JSONObject.toJSONString(info));
        }
    }


    /**
     * 查询费用项折扣信息（instance）
     * @param info bId 信息
     * @return List<Map>
     * @throws DAOException DAO异常
     */
    @Override
    public List<Map> getPayFeeConfigDiscountInfo(Map info) throws DAOException {
        logger.debug("查询费用项折扣信息 入参 info : {}",info);

        List<Map> businessPayFeeConfigDiscountInfos = sqlSessionTemplate.selectList("payFeeConfigDiscountServiceDaoImpl.getPayFeeConfigDiscountInfo",info);

        return businessPayFeeConfigDiscountInfos;
    }


    /**
     * 修改费用项折扣信息
     * @param info 修改信息
     * @throws DAOException DAO异常
     */
    @Override
    public void updatePayFeeConfigDiscountInfo(Map info) throws DAOException {
        logger.debug("修改费用项折扣信息Instance 入参 info : {}",info);

        int saveFlag = sqlSessionTemplate.update("payFeeConfigDiscountServiceDaoImpl.updatePayFeeConfigDiscountInfo",info);

        if(saveFlag < 1){
            throw new DAOException(ResponseConstant.RESULT_PARAM_ERROR,"修改费用项折扣信息Instance数据失败："+ JSONObject.toJSONString(info));
        }
    }

     /**
     * 查询费用项折扣数量
     * @param info 费用项折扣信息
     * @return 费用项折扣数量
     */
    @Override
    public int queryPayFeeConfigDiscountsCount(Map info) {
        logger.debug("查询费用项折扣数据 入参 info : {}",info);

        List<Map> businessPayFeeConfigDiscountInfos = sqlSessionTemplate.selectList("payFeeConfigDiscountServiceDaoImpl.queryPayFeeConfigDiscountsCount", info);
        if (businessPayFeeConfigDiscountInfos.size() < 1) {
            return 0;
        }

        return Integer.parseInt(businessPayFeeConfigDiscountInfos.get(0).get("count").toString());
    }


}
