package com.newland.property.user.bmo.questionAnswer;

import com.newland.property.po.questionAnswer.QuestionAnswerPo;
import org.springframework.http.ResponseEntity;

public interface ISaveQuestionAnswerBMO {


    /**
     * 添加答卷
     * add by wuxw
     *
     * @param questionAnswerPo
     * @return
     */
    ResponseEntity<String> save(QuestionAnswerPo questionAnswerPo);


}
