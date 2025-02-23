package com.newland.property.user.bmo.questionAnswer;

import com.newland.property.po.questionAnswer.QuestionAnswerPo;
import org.springframework.http.ResponseEntity;

public interface IUpdateQuestionAnswerBMO {


    /**
     * 修改答卷
     * add by wuxw
     *
     * @param questionAnswerPo
     * @return
     */
    ResponseEntity<String> update(QuestionAnswerPo questionAnswerPo);


}
