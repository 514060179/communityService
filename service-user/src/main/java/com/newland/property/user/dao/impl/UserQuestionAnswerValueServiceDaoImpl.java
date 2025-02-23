package com.newland.property.user.dao.impl;

import com.alibaba.fastjson.JSONObject;
import com.newland.property.utils.constant.ResponseConstant;
import com.newland.property.utils.exception.DAOException;
import com.newland.property.core.base.dao.BaseServiceDao;
import com.newland.property.user.dao.IUserQuestionAnswerValueServiceDao;
import org.slf4j.Logger;
import com.newland.property.core.log.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * 答卷答案服务 与数据库交互
 * Created by wuxw on 2017/4/5.
 */
@Service("userQuestionAnswerValueServiceDaoImpl")
//@Transactional
public class UserQuestionAnswerValueServiceDaoImpl extends BaseServiceDao implements IUserQuestionAnswerValueServiceDao {

    private static Logger logger = LoggerFactory.getLogger(UserQuestionAnswerValueServiceDaoImpl.class);





    /**
     * 保存答卷答案信息 到 instance
     * @param info   bId 信息
     * @throws DAOException DAO异常
     */
    @Override
    public void saveUserQuestionAnswerValueInfo(Map info) throws DAOException {
        logger.debug("保存答卷答案信息Instance 入参 info : {}",info);

        int saveFlag = sqlSessionTemplate.insert("userQuestionAnswerValueServiceDaoImpl.saveUserQuestionAnswerValueInfo",info);

        if(saveFlag < 1){
            throw new DAOException(ResponseConstant.RESULT_PARAM_ERROR,"保存答卷答案信息Instance数据失败："+ JSONObject.toJSONString(info));
        }
    }


    /**
     * 查询答卷答案信息（instance）
     * @param info bId 信息
     * @return List<Map>
     * @throws DAOException DAO异常
     */
    @Override
    public List<Map> getUserQuestionAnswerValueInfo(Map info) throws DAOException {
        logger.debug("查询答卷答案信息 入参 info : {}",info);

        List<Map> businessUserQuestionAnswerValueInfos = sqlSessionTemplate.selectList("userQuestionAnswerValueServiceDaoImpl.getUserQuestionAnswerValueInfo",info);

        return businessUserQuestionAnswerValueInfos;
    }


    /**
     * 修改答卷答案信息
     * @param info 修改信息
     * @throws DAOException DAO异常
     */
    @Override
    public void updateUserQuestionAnswerValueInfo(Map info) throws DAOException {
        logger.debug("修改答卷答案信息Instance 入参 info : {}",info);

        int saveFlag = sqlSessionTemplate.update("userQuestionAnswerValueServiceDaoImpl.updateUserQuestionAnswerValueInfo",info);

        if(saveFlag < 1){
            throw new DAOException(ResponseConstant.RESULT_PARAM_ERROR,"修改答卷答案信息Instance数据失败："+ JSONObject.toJSONString(info));
        }
    }

     /**
     * 查询答卷答案数量
     * @param info 答卷答案信息
     * @return 答卷答案数量
     */
    @Override
    public int queryUserQuestionAnswerValuesCount(Map info) {
        logger.debug("查询答卷答案数据 入参 info : {}",info);

        List<Map> businessUserQuestionAnswerValueInfos = sqlSessionTemplate.selectList("userQuestionAnswerValueServiceDaoImpl.queryUserQuestionAnswerValuesCount", info);
        if (businessUserQuestionAnswerValueInfos.size() < 1) {
            return 0;
        }

        return Integer.parseInt(businessUserQuestionAnswerValueInfos.get(0).get("count").toString());
    }


}
