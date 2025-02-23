package com.newland.property.user.bmo.questionAnswer.impl;

import com.newland.property.core.annotation.PropertyTransactional;
import com.newland.property.intf.common.IFileRelInnerServiceSMO;
import com.newland.property.intf.user.IQuestionAnswerInnerServiceSMO;
import com.newland.property.po.questionAnswer.QuestionAnswerPo;
import com.newland.property.user.bmo.questionAnswer.IUpdateQuestionAnswerBMO;
import com.newland.property.vo.ResultVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service("updateQuestionAnswerBMOImpl")
public class UpdateQuestionAnswerBMOImpl implements IUpdateQuestionAnswerBMO {

    @Autowired
    private IQuestionAnswerInnerServiceSMO questionAnswerInnerServiceSMOImpl;

    @Autowired
    private IFileRelInnerServiceSMO fileRelInnerServiceSMOImpl;

    /**
     * @param questionAnswerPo
     * @return 订单服务能够接受的报文
     */
    @Override
    @PropertyTransactional
    public ResponseEntity<String> update(QuestionAnswerPo questionAnswerPo) {

        int flag = questionAnswerInnerServiceSMOImpl.updateQuestionAnswer(questionAnswerPo);

        return ResultVo.createResponseEntity(ResultVo.CODE_ERROR, "保存失败");
    }
}