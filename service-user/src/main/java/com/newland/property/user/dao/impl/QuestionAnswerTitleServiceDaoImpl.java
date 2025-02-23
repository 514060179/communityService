package com.newland.property.user.dao.impl;

import com.alibaba.fastjson.JSONObject;
import com.newland.property.utils.constant.ResponseConstant;
import com.newland.property.utils.exception.DAOException;
import com.newland.property.core.base.dao.BaseServiceDao;
import com.newland.property.user.dao.IQuestionAnswerTitleServiceDao;
import org.slf4j.Logger;
import com.newland.property.core.log.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * 答卷服务 与数据库交互
 * Created by wuxw on 2017/4/5.
 */
@Service("questionAnswerTitleServiceDaoImpl")
//@Transactional
public class QuestionAnswerTitleServiceDaoImpl extends BaseServiceDao implements IQuestionAnswerTitleServiceDao {

    private static Logger logger = LoggerFactory.getLogger(QuestionAnswerTitleServiceDaoImpl.class);





    /**
     * 保存答卷信息 到 instance
     * @param info   bId 信息
     * @throws DAOException DAO异常
     */
    @Override
    public void saveQuestionAnswerTitleInfo(Map info) throws DAOException {
        logger.debug("保存答卷信息Instance 入参 info : {}",info);

        int saveFlag = sqlSessionTemplate.insert("questionAnswerTitleServiceDaoImpl.saveQuestionAnswerTitleInfo",info);

        if(saveFlag < 1){
            throw new DAOException(ResponseConstant.RESULT_PARAM_ERROR,"保存答卷信息Instance数据失败："+ JSONObject.toJSONString(info));
        }
    }


    /**
     * 查询答卷信息（instance）
     * @param info bId 信息
     * @return List<Map>
     * @throws DAOException DAO异常
     */
    @Override
    public List<Map> getQuestionAnswerTitleInfo(Map info) throws DAOException {
        logger.debug("查询答卷信息 入参 info : {}",info);

        List<Map> businessQuestionAnswerTitleInfos = sqlSessionTemplate.selectList("questionAnswerTitleServiceDaoImpl.getQuestionAnswerTitleInfo",info);

        return businessQuestionAnswerTitleInfos;
    }


    /**
     * 修改答卷信息
     * @param info 修改信息
     * @throws DAOException DAO异常
     */
    @Override
    public void updateQuestionAnswerTitleInfo(Map info) throws DAOException {
        logger.debug("修改答卷信息Instance 入参 info : {}",info);

        int saveFlag = sqlSessionTemplate.update("questionAnswerTitleServiceDaoImpl.updateQuestionAnswerTitleInfo",info);

        if(saveFlag < 1){
            throw new DAOException(ResponseConstant.RESULT_PARAM_ERROR,"修改答卷信息Instance数据失败："+ JSONObject.toJSONString(info));
        }
    }

     /**
     * 查询答卷数量
     * @param info 答卷信息
     * @return 答卷数量
     */
    @Override
    public int queryQuestionAnswerTitlesCount(Map info) {
        logger.debug("查询答卷数据 入参 info : {}",info);

        List<Map> businessQuestionAnswerTitleInfos = sqlSessionTemplate.selectList("questionAnswerTitleServiceDaoImpl.queryQuestionAnswerTitlesCount", info);
        if (businessQuestionAnswerTitleInfos.size() < 1) {
            return 0;
        }

        return Integer.parseInt(businessQuestionAnswerTitleInfos.get(0).get("count").toString());
    }


}
