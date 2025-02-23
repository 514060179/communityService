package com.newland.property.user.bmo.questionAnswer;

import com.newland.property.dto.questionAnswer.QuestionAnswerDto;
import org.springframework.http.ResponseEntity;

public interface IGetQuestionAnswerBMO {


    /**
     * 查询答卷
     * add by wuxw
     *
     * @param questionAnswerDto
     * @return
     */
    ResponseEntity<String> get(QuestionAnswerDto questionAnswerDto);


}
