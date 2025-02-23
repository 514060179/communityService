package com.newland.property.user.dao.impl;

import com.alibaba.fastjson.JSONObject;
import com.newland.property.core.base.dao.BaseServiceDao;
import com.newland.property.dto.org.OrgStaffRelDto;
import com.newland.property.user.dao.IOrgStaffRelServiceDao;
import com.newland.property.utils.constant.ResponseConstant;
import com.newland.property.utils.exception.DAOException;
import com.newland.property.utils.util.DateUtil;
import org.slf4j.Logger;
import com.newland.property.core.log.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * 组织员工关系服务 与数据库交互
 * Created by wuxw on 2017/4/5.
 */
@Service("orgStaffRelServiceDaoImpl")
//@Transactional
public class OrgStaffRelServiceDaoImpl extends BaseServiceDao implements IOrgStaffRelServiceDao {

    private static Logger logger = LoggerFactory.getLogger(OrgStaffRelServiceDaoImpl.class);

    /**
     * 组织员工关系信息封装
     * @param businessOrgStaffRelInfo 组织员工关系信息 封装
     * @throws DAOException DAO异常
     */
    @Override
    public void saveBusinessOrgStaffRelInfo(Map businessOrgStaffRelInfo) throws DAOException {
        businessOrgStaffRelInfo.put("month", DateUtil.getCurrentMonth());
        // 查询business_user 数据是否已经存在
        logger.debug("保存组织员工关系信息 入参 businessOrgStaffRelInfo : {}",businessOrgStaffRelInfo);
        int saveFlag = sqlSessionTemplate.insert("orgStaffRelServiceDaoImpl.saveBusinessOrgStaffRelInfo",businessOrgStaffRelInfo);

        if(saveFlag < 1){
            throw new DAOException(ResponseConstant.RESULT_PARAM_ERROR,"保存组织员工关系数据失败："+ JSONObject.toJSONString(businessOrgStaffRelInfo));
        }
    }


    /**
     * 查询组织员工关系信息
     * @param info bId 信息
     * @return 组织员工关系信息
     * @throws DAOException DAO异常
     */
    @Override
    public List<Map> getBusinessOrgStaffRelInfo(Map info) throws DAOException {

        logger.debug("查询组织员工关系信息 入参 info : {}",info);

        List<Map> businessOrgStaffRelInfos = sqlSessionTemplate.selectList("orgStaffRelServiceDaoImpl.getBusinessOrgStaffRelInfo",info);

        return businessOrgStaffRelInfos;
    }



    /**
     * 保存组织员工关系信息 到 instance
     * @param info   bId 信息
     * @throws DAOException DAO异常
     */
    @Override
    public void saveOrgStaffRelInfoInstance(Map info) throws DAOException {
        logger.debug("保存组织员工关系信息Instance 入参 info : {}",info);

        int saveFlag = sqlSessionTemplate.insert("orgStaffRelServiceDaoImpl.saveOrgStaffRelInfoInstance",info);

        if(saveFlag < 1){
            throw new DAOException(ResponseConstant.RESULT_PARAM_ERROR,"保存组织员工关系信息Instance数据失败："+ JSONObject.toJSONString(info));
        }
    }


    /**
     * 查询组织员工关系信息（instance）
     * @param info bId 信息
     * @return List<Map>
     * @throws DAOException DAO异常
     */
    @Override
    public List<Map> getOrgStaffRelInfo(Map info) throws DAOException {
        logger.debug("查询组织员工关系信息 入参 info : {}",info);

        List<Map> businessOrgStaffRelInfos = sqlSessionTemplate.selectList("orgStaffRelServiceDaoImpl.getOrgStaffRelInfo",info);

        return businessOrgStaffRelInfos;
    }


    /**
     * 修改组织员工关系信息
     * @param info 修改信息
     * @throws DAOException DAO异常
     */
    @Override
    public void updateOrgStaffRelInfoInstance(Map info) throws DAOException {
        logger.debug("修改组织员工关系信息Instance 入参 info : {}",info);

        int saveFlag = sqlSessionTemplate.update("orgStaffRelServiceDaoImpl.updateOrgStaffRelInfoInstance",info);

        if(saveFlag < 1){
            throw new DAOException(ResponseConstant.RESULT_PARAM_ERROR,"修改组织员工关系信息Instance数据失败："+ JSONObject.toJSONString(info));
        }
    }

     /**
     * 查询组织员工关系数量
     * @param info 组织员工关系信息
     * @return 组织员工关系数量
     */
    @Override
    public int queryOrgStaffRelsCount(Map info) {
        logger.debug("查询组织员工关系数据 入参 info : {}",info);

        List<Map> businessOrgStaffRelInfos = sqlSessionTemplate.selectList("orgStaffRelServiceDaoImpl.queryOrgStaffRelsCount", info);
        if (businessOrgStaffRelInfos.size() < 1) {
            return 0;
        }

        return Integer.parseInt(businessOrgStaffRelInfos.get(0).get("count").toString());
    }

    @Override
    public List<OrgStaffRelDto> queryOrgInfoByStaffIds(Map info) {
        List<OrgStaffRelDto> orgStaffRelDtoList = sqlSessionTemplate.selectList("orgStaffRelServiceDaoImpl.queryOrgInfoByStaffIds", info);
        return orgStaffRelDtoList;
    }

    @Override
    public List<OrgStaffRelDto> queryOrgInfoByStaffIdsNew(Map info) {
        List<OrgStaffRelDto> orgStaffRelDtoList = sqlSessionTemplate.selectList("orgStaffRelServiceDaoImpl.queryOrgInfoByStaffIdNew", info);
        return orgStaffRelDtoList;
    }


}
