package com.newland.property.fee.dao.impl;

import com.alibaba.fastjson.JSONObject;
import com.newland.property.utils.constant.ResponseConstant;
import com.newland.property.utils.exception.DAOException;
import com.newland.property.core.base.dao.BaseServiceDao;
import com.newland.property.fee.dao.IFeeManualCollectionDetailServiceDao;
import org.slf4j.Logger;
import com.newland.property.core.log.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * 托收明细服务 与数据库交互
 * Created by wuxw on 2017/4/5.
 */
@Service("feeManualCollectionDetailServiceDaoImpl")
//@Transactional
public class FeeManualCollectionDetailServiceDaoImpl extends BaseServiceDao implements IFeeManualCollectionDetailServiceDao {

    private static Logger logger = LoggerFactory.getLogger(FeeManualCollectionDetailServiceDaoImpl.class);





    /**
     * 保存托收明细信息 到 instance
     * @param info   bId 信息
     * @throws DAOException DAO异常
     */
    @Override
    public void saveFeeManualCollectionDetailInfo(Map info) throws DAOException {
        logger.debug("保存托收明细信息Instance 入参 info : {}",info);

        int saveFlag = sqlSessionTemplate.insert("feeManualCollectionDetailServiceDaoImpl.saveFeeManualCollectionDetailInfo",info);

        if(saveFlag < 1){
            throw new DAOException(ResponseConstant.RESULT_PARAM_ERROR,"保存托收明细信息Instance数据失败："+ JSONObject.toJSONString(info));
        }
    }


    /**
     * 查询托收明细信息（instance）
     * @param info bId 信息
     * @return List<Map>
     * @throws DAOException DAO异常
     */
    @Override
    public List<Map> getFeeManualCollectionDetailInfo(Map info) throws DAOException {
        logger.debug("查询托收明细信息 入参 info : {}",info);

        List<Map> businessFeeManualCollectionDetailInfos = sqlSessionTemplate.selectList("feeManualCollectionDetailServiceDaoImpl.getFeeManualCollectionDetailInfo",info);

        return businessFeeManualCollectionDetailInfos;
    }


    /**
     * 修改托收明细信息
     * @param info 修改信息
     * @throws DAOException DAO异常
     */
    @Override
    public void updateFeeManualCollectionDetailInfo(Map info) throws DAOException {
        logger.debug("修改托收明细信息Instance 入参 info : {}",info);

        int saveFlag = sqlSessionTemplate.update("feeManualCollectionDetailServiceDaoImpl.updateFeeManualCollectionDetailInfo",info);

        if(saveFlag < 1){
            throw new DAOException(ResponseConstant.RESULT_PARAM_ERROR,"修改托收明细信息Instance数据失败："+ JSONObject.toJSONString(info));
        }
    }

     /**
     * 查询托收明细数量
     * @param info 托收明细信息
     * @return 托收明细数量
     */
    @Override
    public int queryFeeManualCollectionDetailsCount(Map info) {
        logger.debug("查询托收明细数据 入参 info : {}",info);

        List<Map> businessFeeManualCollectionDetailInfos = sqlSessionTemplate.selectList("feeManualCollectionDetailServiceDaoImpl.queryFeeManualCollectionDetailsCount", info);
        if (businessFeeManualCollectionDetailInfos.size() < 1) {
            return 0;
        }

        return Integer.parseInt(businessFeeManualCollectionDetailInfos.get(0).get("count").toString());
    }



    /**
     * 查询托收明细数量
     * @param info 托收明细信息
     * @return 托收明细数量
     */
    @Override
    public double queryFeeManualCollectionDetailTotalFee(Map info) {
        logger.debug("查询托收明细数据 入参 info : {}",info);

        List<Map> businessFeeManualCollectionDetailInfos = sqlSessionTemplate.selectList("feeManualCollectionDetailServiceDaoImpl.queryFeeManualCollectionDetailTotalFee", info);
        if (businessFeeManualCollectionDetailInfos.size() < 1) {
            return 0;
        }

        return Double.parseDouble(businessFeeManualCollectionDetailInfos.get(0).get("totalFee").toString());
    }

}
