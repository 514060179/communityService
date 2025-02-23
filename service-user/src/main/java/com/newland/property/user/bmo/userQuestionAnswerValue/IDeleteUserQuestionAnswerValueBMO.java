package com.newland.property.user.bmo.userQuestionAnswerValue;
import com.newland.property.po.user.UserQuestionAnswerValuePo;
import org.springframework.http.ResponseEntity;

public interface IDeleteUserQuestionAnswerValueBMO {


    /**
     * 修改答卷答案
     * add by wuxw
     * @param userQuestionAnswerValuePo
     * @return
     */
    ResponseEntity<String> delete(UserQuestionAnswerValuePo userQuestionAnswerValuePo);


}
