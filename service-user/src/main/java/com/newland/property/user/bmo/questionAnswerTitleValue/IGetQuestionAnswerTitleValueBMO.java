package com.newland.property.user.bmo.questionAnswerTitleValue;
import com.newland.property.dto.questionAnswer.QuestionAnswerTitleValueDto;
import org.springframework.http.ResponseEntity;
public interface IGetQuestionAnswerTitleValueBMO {


    /**
     * 查询答卷选项
     * add by wuxw
     * @param  questionAnswerTitleValueDto
     * @return
     */
    ResponseEntity<String> get(QuestionAnswerTitleValueDto questionAnswerTitleValueDto);


    /**
     * 查询结果
     * @param questionAnswerTitleValueDto
     * @return
     */
    ResponseEntity<String> queryQuestionAnswerTitleValueResult(QuestionAnswerTitleValueDto questionAnswerTitleValueDto);
}
