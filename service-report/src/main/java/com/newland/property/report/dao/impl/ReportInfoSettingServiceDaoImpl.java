package com.newland.property.report.dao.impl;

import com.alibaba.fastjson.JSONObject;
import com.newland.property.report.dao.IReportInfoSettingServiceDao;
import com.newland.property.utils.constant.ResponseConstant;
import com.newland.property.utils.exception.DAOException;
import com.newland.property.core.base.dao.BaseServiceDao;
import org.slf4j.Logger;
import com.newland.property.core.log.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * 进出上报服务 与数据库交互
 * Created by wuxw on 2017/4/5.
 */
@Service("reportInfoSettingServiceDaoImpl")
//@Transactional
public class ReportInfoSettingServiceDaoImpl extends BaseServiceDao implements IReportInfoSettingServiceDao {

    private static Logger logger = LoggerFactory.getLogger(ReportInfoSettingServiceDaoImpl.class);





    /**
     * 保存进出上报信息 到 instance
     * @param info   bId 信息
     * @throws DAOException DAO异常
     */
    @Override
    public void saveReportInfoSettingInfo(Map info) throws DAOException {
        logger.debug("保存进出上报信息Instance 入参 info : {}",info);

        int saveFlag = sqlSessionTemplate.insert("reportInfoSettingServiceDaoImpl.saveReportInfoSettingInfo",info);

        if(saveFlag < 1){
            throw new DAOException(ResponseConstant.RESULT_PARAM_ERROR,"保存进出上报信息Instance数据失败："+ JSONObject.toJSONString(info));
        }
    }


    /**
     * 查询进出上报信息（instance）
     * @param info bId 信息
     * @return List<Map>
     * @throws DAOException DAO异常
     */
    @Override
    public List<Map> getReportInfoSettingInfo(Map info) throws DAOException {
        logger.debug("查询进出上报信息 入参 info : {}",info);

        List<Map> businessReportInfoSettingInfos = sqlSessionTemplate.selectList("reportInfoSettingServiceDaoImpl.getReportInfoSettingInfo",info);

        return businessReportInfoSettingInfos;
    }


    /**
     * 修改进出上报信息
     * @param info 修改信息
     * @throws DAOException DAO异常
     */
    @Override
    public void updateReportInfoSettingInfo(Map info) throws DAOException {
        logger.debug("修改进出上报信息Instance 入参 info : {}",info);

        int saveFlag = sqlSessionTemplate.update("reportInfoSettingServiceDaoImpl.updateReportInfoSettingInfo",info);

        if(saveFlag < 1){
            throw new DAOException(ResponseConstant.RESULT_PARAM_ERROR,"修改进出上报信息Instance数据失败："+ JSONObject.toJSONString(info));
        }
    }

     /**
     * 查询进出上报数量
     * @param info 进出上报信息
     * @return 进出上报数量
     */
    @Override
    public int queryReportInfoSettingsCount(Map info) {
        logger.debug("查询进出上报数据 入参 info : {}",info);

        List<Map> businessReportInfoSettingInfos = sqlSessionTemplate.selectList("reportInfoSettingServiceDaoImpl.queryReportInfoSettingsCount", info);
        if (businessReportInfoSettingInfos.size() < 1) {
            return 0;
        }

        return Integer.parseInt(businessReportInfoSettingInfos.get(0).get("count").toString());
    }


}
