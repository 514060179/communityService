package com.newland.property.report.dao.impl;

import com.alibaba.fastjson.JSONObject;
import com.newland.property.report.dao.IReportOwnerPayFeeServiceDao;
import com.newland.property.utils.constant.ResponseConstant;
import com.newland.property.utils.exception.DAOException;
import com.newland.property.core.base.dao.BaseServiceDao;
import org.slf4j.Logger;
import com.newland.property.core.log.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * 业主缴费明细服务 与数据库交互
 * Created by wuxw on 2017/4/5.
 */
@Service("reportOwnerPayFeeServiceDaoImpl")
//@Transactional
public class ReportOwnerPayFeeServiceDaoImpl extends BaseServiceDao implements IReportOwnerPayFeeServiceDao {

    private static Logger logger = LoggerFactory.getLogger(ReportOwnerPayFeeServiceDaoImpl.class);





    /**
     * 保存业主缴费明细信息 到 instance
     * @param info   bId 信息
     * @throws DAOException DAO异常
     */
    @Override
    public void saveReportOwnerPayFeeInfo(Map info) throws DAOException {
        logger.debug("保存业主缴费明细信息Instance 入参 info : {}",info);

        int saveFlag = sqlSessionTemplate.insert("reportOwnerPayFeeServiceDaoImpl.saveReportOwnerPayFeeInfo",info);

        if(saveFlag < 1){
            throw new DAOException(ResponseConstant.RESULT_PARAM_ERROR,"保存业主缴费明细信息Instance数据失败："+ JSONObject.toJSONString(info));
        }
    }


    /**
     * 查询业主缴费明细信息（instance）
     * @param info bId 信息
     * @return List<Map>
     * @throws DAOException DAO异常
     */
    @Override
    public List<Map> getReportOwnerPayFeeInfo(Map info) throws DAOException {
        logger.debug("查询业主缴费明细信息 入参 info : {}",info);

        List<Map> businessReportOwnerPayFeeInfos = sqlSessionTemplate.selectList("reportOwnerPayFeeServiceDaoImpl.getReportOwnerPayFeeInfo",info);

        return businessReportOwnerPayFeeInfos;
    }


    /**
     * 修改业主缴费明细信息
     * @param info 修改信息
     * @throws DAOException DAO异常
     */
    @Override
    public void updateReportOwnerPayFeeInfo(Map info) throws DAOException {
        logger.debug("修改业主缴费明细信息Instance 入参 info : {}",info);

        int saveFlag = sqlSessionTemplate.update("reportOwnerPayFeeServiceDaoImpl.updateReportOwnerPayFeeInfo",info);

        if(saveFlag < 1){
            throw new DAOException(ResponseConstant.RESULT_PARAM_ERROR,"修改业主缴费明细信息Instance数据失败："+ JSONObject.toJSONString(info));
        }
    }

     /**
     * 查询业主缴费明细数量
     * @param info 业主缴费明细信息
     * @return 业主缴费明细数量
     */
    @Override
    public int queryReportOwnerPayFeesCount(Map info) {
        logger.debug("查询业主缴费明细数据 入参 info : {}",info);

        List<Map> businessReportOwnerPayFeeInfos = sqlSessionTemplate.selectList("reportOwnerPayFeeServiceDaoImpl.queryReportOwnerPayFeesCount", info);
        if (businessReportOwnerPayFeeInfos.size() < 1) {
            return 0;
        }

        return Integer.parseInt(businessReportOwnerPayFeeInfos.get(0).get("count").toString());
    }

    @Override
    public List<Map> queryReportOwnerMonthPayFees(Map info) {
        logger.debug("查询业主缴费明细信息 入参 info : {}",info);

        List<Map> businessReportOwnerPayFeeInfos = sqlSessionTemplate.selectList("reportOwnerPayFeeServiceDaoImpl.queryReportOwnerMonthPayFees",info);

        return businessReportOwnerPayFeeInfos;
    }


}
