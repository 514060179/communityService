package com.newland.property.user.bmo.questionAnswerTitle;

import com.newland.property.po.questionAnswer.QuestionAnswerTitlePo;
import org.springframework.http.ResponseEntity;

public interface IDeleteQuestionAnswerTitleBMO {


    /**
     * 修改答卷
     * add by wuxw
     *
     * @param questionAnswerTitlePo
     * @return
     */
    ResponseEntity<String> delete(QuestionAnswerTitlePo questionAnswerTitlePo);


}
