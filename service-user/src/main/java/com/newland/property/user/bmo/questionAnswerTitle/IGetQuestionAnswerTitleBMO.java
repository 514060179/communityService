package com.newland.property.user.bmo.questionAnswerTitle;
import com.newland.property.dto.questionAnswer.QuestionAnswerTitleDto;
import org.springframework.http.ResponseEntity;
public interface IGetQuestionAnswerTitleBMO {


    /**
     * 查询答卷
     * add by wuxw
     * @param  questionAnswerTitleDto
     * @return
     */
    ResponseEntity<String> get(QuestionAnswerTitleDto questionAnswerTitleDto);


}
