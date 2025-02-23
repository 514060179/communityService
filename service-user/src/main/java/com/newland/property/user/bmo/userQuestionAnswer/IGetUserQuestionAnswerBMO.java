package com.newland.property.user.bmo.userQuestionAnswer;
import com.newland.property.dto.user.UserQuestionAnswerDto;
import org.springframework.http.ResponseEntity;
public interface IGetUserQuestionAnswerBMO {


    /**
     * 查询答卷
     * add by wuxw
     * @param  userQuestionAnswerDto
     * @return
     */
    ResponseEntity<String> get(UserQuestionAnswerDto userQuestionAnswerDto);


}
