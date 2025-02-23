package com.newland.property.user.bmo.questionAnswer.impl;

import com.newland.property.core.annotation.PropertyTransactional;
import com.newland.property.intf.user.IQuestionAnswerInnerServiceSMO;
import com.newland.property.po.questionAnswer.QuestionAnswerPo;
import com.newland.property.user.bmo.questionAnswer.IDeleteQuestionAnswerBMO;
import com.newland.property.vo.ResultVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service("deleteQuestionAnswerBMOImpl")
public class DeleteQuestionAnswerBMOImpl implements IDeleteQuestionAnswerBMO {

    @Autowired
    private IQuestionAnswerInnerServiceSMO questionAnswerInnerServiceSMOImpl;

    /**
     * @param questionAnswerPo 数据
     * @return 订单服务能够接受的报文
     */
    @Override
    @PropertyTransactional
    public ResponseEntity<String> delete(QuestionAnswerPo questionAnswerPo) {

        int flag = questionAnswerInnerServiceSMOImpl.deleteQuestionAnswer(questionAnswerPo);

        if (flag > 0) {
            return ResultVo.createResponseEntity(ResultVo.CODE_OK, "保存成功");
        }

        return ResultVo.createResponseEntity(ResultVo.CODE_ERROR, "保存失败");
    }

}
