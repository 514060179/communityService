package com.newland.property.user.bmo.questionAnswerTitle.impl;

import com.newland.property.core.annotation.PropertyTransactional;
import com.newland.property.intf.user.IQuestionAnswerTitleInnerServiceSMO;
import com.newland.property.po.questionAnswer.QuestionAnswerTitlePo;
import com.newland.property.user.bmo.questionAnswerTitle.IDeleteQuestionAnswerTitleBMO;
import com.newland.property.vo.ResultVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service("deleteQuestionAnswerTitleBMOImpl")
public class DeleteQuestionAnswerTitleBMOImpl implements IDeleteQuestionAnswerTitleBMO {

    @Autowired
    private IQuestionAnswerTitleInnerServiceSMO questionAnswerTitleInnerServiceSMOImpl;

    /**
     * @param questionAnswerTitlePo 数据
     * @return 订单服务能够接受的报文
     */
    @Override
    @PropertyTransactional
    public ResponseEntity<String> delete(QuestionAnswerTitlePo questionAnswerTitlePo) {

        int flag = questionAnswerTitleInnerServiceSMOImpl.deleteQuestionAnswerTitle(questionAnswerTitlePo);

        if (flag > 0) {
            return ResultVo.createResponseEntity(ResultVo.CODE_OK, "保存成功");
        }

        return ResultVo.createResponseEntity(ResultVo.CODE_ERROR, "保存失败");
    }

}
