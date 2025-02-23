package com.newland.property.community.dao.impl;

import com.alibaba.fastjson.JSONObject;
import com.newland.property.community.dao.IInspectionRouteServiceDao;
import com.newland.property.utils.constant.ResponseConstant;
import com.newland.property.utils.exception.DAOException;
import com.newland.property.utils.util.DateUtil;
import com.newland.property.core.base.dao.BaseServiceDao;
import org.slf4j.Logger;
import com.newland.property.core.log.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * 巡检路线服务 与数据库交互
 * Created by wuxw on 2017/4/5.
 */
@Service("inspectionRouteServiceDaoImpl")
//@Transactional
public class InspectionRouteServiceDaoImpl extends BaseServiceDao implements IInspectionRouteServiceDao {

    private static Logger logger = LoggerFactory.getLogger(InspectionRouteServiceDaoImpl.class);

    /**
     * 巡检路线信息封装
     * @param businessInspectionRouteInfo 巡检路线信息 封装
     * @throws DAOException DAO异常
     */
    @Override
    public void saveBusinessInspectionRouteInfo(Map businessInspectionRouteInfo) throws DAOException {
        businessInspectionRouteInfo.put("month", DateUtil.getCurrentMonth());
        // 查询business_user 数据是否已经存在
        logger.debug("保存巡检路线信息 入参 businessInspectionRouteInfo : {}",businessInspectionRouteInfo);
        int saveFlag = sqlSessionTemplate.insert("inspectionRouteServiceDaoImpl.saveBusinessInspectionRouteInfo",businessInspectionRouteInfo);

        if(saveFlag < 1){
            throw new DAOException(ResponseConstant.RESULT_PARAM_ERROR,"保存巡检路线数据失败："+ JSONObject.toJSONString(businessInspectionRouteInfo));
        }
    }


    /**
     * 查询巡检路线信息
     * @param info bId 信息
     * @return 巡检路线信息
     * @throws DAOException DAO异常
     */
    @Override
    public List<Map> getBusinessInspectionRouteInfo(Map info) throws DAOException {

        logger.debug("查询巡检路线信息 入参 info : {}",info);

        List<Map> businessInspectionRouteInfos = sqlSessionTemplate.selectList("inspectionRouteServiceDaoImpl.getBusinessInspectionRouteInfo",info);

        return businessInspectionRouteInfos;
    }



    /**
     * 保存巡检路线信息 到 instance
     * @param info   bId 信息
     * @throws DAOException DAO异常
     */
    @Override
    public void saveInspectionRouteInfoInstance(Map info) throws DAOException {
        logger.debug("保存巡检路线信息Instance 入参 info : {}",info);

        int saveFlag = sqlSessionTemplate.insert("inspectionRouteServiceDaoImpl.saveInspectionRouteInfoInstance",info);

        if(saveFlag < 1){
            throw new DAOException(ResponseConstant.RESULT_PARAM_ERROR,"保存巡检路线信息Instance数据失败："+ JSONObject.toJSONString(info));
        }
    }


    /**
     * 查询巡检路线信息（instance）
     * @param info bId 信息
     * @return List<Map>
     * @throws DAOException DAO异常
     */
    @Override
    public List<Map> getInspectionRouteInfo(Map info) throws DAOException {
        logger.debug("查询巡检路线信息 入参 info : {}",info);

        List<Map> businessInspectionRouteInfos = sqlSessionTemplate.selectList("inspectionRouteServiceDaoImpl.getInspectionRouteInfo",info);

        return businessInspectionRouteInfos;
    }


    /**
     * 修改巡检路线信息
     * @param info 修改信息
     * @throws DAOException DAO异常
     */
    @Override
    public void updateInspectionRouteInfoInstance(Map info) throws DAOException {
        logger.debug("修改巡检路线信息Instance 入参 info : {}",info);

        int saveFlag = sqlSessionTemplate.update("inspectionRouteServiceDaoImpl.updateInspectionRouteInfoInstance",info);

        if(saveFlag < 1){
            throw new DAOException(ResponseConstant.RESULT_PARAM_ERROR,"修改巡检路线信息Instance数据失败："+ JSONObject.toJSONString(info));
        }
    }

     /**
     * 查询巡检路线数量
     * @param info 巡检路线信息
     * @return 巡检路线数量
     */
    @Override
    public int queryInspectionRoutesCount(Map info) {
        logger.debug("查询巡检路线数据 入参 info : {}",info);

        List<Map> businessInspectionRouteInfos = sqlSessionTemplate.selectList("inspectionRouteServiceDaoImpl.queryInspectionRoutesCount", info);
        if (businessInspectionRouteInfos.size() < 1) {
            return 0;
        }

        return Integer.parseInt(businessInspectionRouteInfos.get(0).get("count").toString());
    }


}
