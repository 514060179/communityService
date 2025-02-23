package com.newland.property.user.bmo.userQuestionAnswerValue;
import com.newland.property.dto.user.UserQuestionAnswerValueDto;
import org.springframework.http.ResponseEntity;
public interface IGetUserQuestionAnswerValueBMO {


    /**
     * 查询答卷答案
     * add by wuxw
     * @param  userQuestionAnswerValueDto
     * @return
     */
    ResponseEntity<String> get(UserQuestionAnswerValueDto userQuestionAnswerValueDto);


}
