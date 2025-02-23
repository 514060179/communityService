package com.newland.property.user.smo.impl;


import com.newland.property.core.base.smo.BaseServiceSMO;
import com.newland.property.dto.PageDto;
import com.newland.property.dto.questionAnswer.QuestionAnswerTitleValueDto;
import com.newland.property.intf.user.IQuestionAnswerTitleValueInnerServiceSMO;
import com.newland.property.po.questionAnswer.QuestionAnswerTitleValuePo;
import com.newland.property.user.dao.IQuestionAnswerTitleValueServiceDao;
import com.newland.property.utils.util.BeanConvertUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @ClassName FloorInnerServiceSMOImpl
 * @Description 答卷选项内部服务实现类
 * @Author wuxw
 * @Date 2019/4/24 9:20
 * @Version 1.0
 * add by wuxw 2019/4/24
 **/
@RestController
public class QuestionAnswerTitleValueInnerServiceSMOImpl extends BaseServiceSMO implements IQuestionAnswerTitleValueInnerServiceSMO {

    @Autowired
    private IQuestionAnswerTitleValueServiceDao questionAnswerTitleValueServiceDaoImpl;


    @Override
    public int saveQuestionAnswerTitleValue(@RequestBody QuestionAnswerTitleValuePo questionAnswerTitleValuePo) {
        int saveFlag = 1;
        questionAnswerTitleValueServiceDaoImpl.saveQuestionAnswerTitleValueInfo(BeanConvertUtil.beanCovertMap(questionAnswerTitleValuePo));
        return saveFlag;
    }

    @Override
    public int updateQuestionAnswerTitleValue(@RequestBody QuestionAnswerTitleValuePo questionAnswerTitleValuePo) {
        int saveFlag = 1;
        questionAnswerTitleValueServiceDaoImpl.updateQuestionAnswerTitleValueInfo(BeanConvertUtil.beanCovertMap(questionAnswerTitleValuePo));
        return saveFlag;
    }

    @Override
    public int deleteQuestionAnswerTitleValue(@RequestBody QuestionAnswerTitleValuePo questionAnswerTitleValuePo) {
        int saveFlag = 1;
        questionAnswerTitleValuePo.setStatusCd("1");
        questionAnswerTitleValueServiceDaoImpl.updateQuestionAnswerTitleValueInfo(BeanConvertUtil.beanCovertMap(questionAnswerTitleValuePo));
        return saveFlag;
    }

    @Override
    public List<QuestionAnswerTitleValueDto> queryQuestionAnswerTitleValues(@RequestBody QuestionAnswerTitleValueDto questionAnswerTitleValueDto) {

        //校验是否传了 分页信息

        int page = questionAnswerTitleValueDto.getPage();

        if (page != PageDto.DEFAULT_PAGE) {
            questionAnswerTitleValueDto.setPage((page - 1) * questionAnswerTitleValueDto.getRow());
        }

        List<QuestionAnswerTitleValueDto> questionAnswerTitleValues = BeanConvertUtil.covertBeanList(questionAnswerTitleValueServiceDaoImpl.getQuestionAnswerTitleValueInfo(BeanConvertUtil.beanCovertMap(questionAnswerTitleValueDto)), QuestionAnswerTitleValueDto.class);

        return questionAnswerTitleValues;
    }


    @Override
    public int queryQuestionAnswerTitleValuesCount(@RequestBody QuestionAnswerTitleValueDto questionAnswerTitleValueDto) {
        return questionAnswerTitleValueServiceDaoImpl.queryQuestionAnswerTitleValuesCount(BeanConvertUtil.beanCovertMap(questionAnswerTitleValueDto));
    }

    @Override
    public List<QuestionAnswerTitleValueDto> queryQuestionAnswerTitleValueResult(@RequestBody QuestionAnswerTitleValueDto questionAnswerTitleValueDto) {
        List<QuestionAnswerTitleValueDto> questionAnswerTitleValues
                = BeanConvertUtil.covertBeanList(
                        questionAnswerTitleValueServiceDaoImpl.queryQuestionAnswerTitleValueResult(BeanConvertUtil.beanCovertMap(questionAnswerTitleValueDto)), QuestionAnswerTitleValueDto.class);

        return questionAnswerTitleValues;
    }

    @Override
    public List<QuestionAnswerTitleValueDto> queryQuestionAnswerTitleValueResultCount(@RequestBody QuestionAnswerTitleValueDto questionAnswerTitleValueDto) {
        List<QuestionAnswerTitleValueDto> questionAnswerTitleValues
                = BeanConvertUtil.covertBeanList(
                questionAnswerTitleValueServiceDaoImpl.queryQuestionAnswerTitleValueResultCount(BeanConvertUtil.beanCovertMap(questionAnswerTitleValueDto)), QuestionAnswerTitleValueDto.class);
        return questionAnswerTitleValues;
    }

    public IQuestionAnswerTitleValueServiceDao getQuestionAnswerTitleValueServiceDaoImpl() {
        return questionAnswerTitleValueServiceDaoImpl;
    }

    public void setQuestionAnswerTitleValueServiceDaoImpl(IQuestionAnswerTitleValueServiceDao questionAnswerTitleValueServiceDaoImpl) {
        this.questionAnswerTitleValueServiceDaoImpl = questionAnswerTitleValueServiceDaoImpl;
    }
}
