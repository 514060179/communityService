package com.newland.property.user.bmo.questionAnswerTitleValue;
import com.newland.property.po.questionAnswer.QuestionAnswerTitleValuePo;
import org.springframework.http.ResponseEntity;

public interface IUpdateQuestionAnswerTitleValueBMO {


    /**
     * 修改答卷选项
     * add by wuxw
     * @param questionAnswerTitleValuePo
     * @return
     */
    ResponseEntity<String> update(QuestionAnswerTitleValuePo questionAnswerTitleValuePo);


}
