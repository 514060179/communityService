package com.newland.property.user.bmo.questionAnswerTitleValue.impl;

import com.newland.property.core.annotation.PropertyTransactional;
import com.newland.property.intf.user.IQuestionAnswerTitleValueInnerServiceSMO;
import com.newland.property.po.questionAnswer.QuestionAnswerTitleValuePo;
import com.newland.property.user.bmo.questionAnswerTitleValue.IUpdateQuestionAnswerTitleValueBMO;
import com.newland.property.vo.ResultVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service("updateQuestionAnswerTitleValueBMOImpl")
public class UpdateQuestionAnswerTitleValueBMOImpl implements IUpdateQuestionAnswerTitleValueBMO {

    @Autowired
    private IQuestionAnswerTitleValueInnerServiceSMO questionAnswerTitleValueInnerServiceSMOImpl;

    /**
     * @param questionAnswerTitleValuePo
     * @return 订单服务能够接受的报文
     */
    @Override
    @PropertyTransactional
    public ResponseEntity<String> update(QuestionAnswerTitleValuePo questionAnswerTitleValuePo) {

        int flag = questionAnswerTitleValueInnerServiceSMOImpl.updateQuestionAnswerTitleValue(questionAnswerTitleValuePo);

        if (flag > 0) {
            return ResultVo.createResponseEntity(ResultVo.CODE_OK, "保存成功");
        }

        return ResultVo.createResponseEntity(ResultVo.CODE_ERROR, "保存失败");
    }

}
