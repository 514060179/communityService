package com.newland.property.user.bmo.questionAnswerTitleValue;

import com.newland.property.po.questionAnswer.QuestionAnswerTitleValuePo;
import org.springframework.http.ResponseEntity;
public interface ISaveQuestionAnswerTitleValueBMO {


    /**
     * 添加答卷选项
     * add by wuxw
     * @param questionAnswerTitleValuePo
     * @return
     */
    ResponseEntity<String> save(QuestionAnswerTitleValuePo questionAnswerTitleValuePo);


}
