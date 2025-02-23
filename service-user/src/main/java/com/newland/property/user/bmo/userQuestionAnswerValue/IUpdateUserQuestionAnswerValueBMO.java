package com.newland.property.user.bmo.userQuestionAnswerValue;

import com.newland.property.po.user.UserQuestionAnswerValuePo;
import org.springframework.http.ResponseEntity;

public interface IUpdateUserQuestionAnswerValueBMO {


    /**
     * 修改答卷答案
     * add by wuxw
     *
     * @param userQuestionAnswerValuePo
     * @return
     */
    ResponseEntity<String> update(UserQuestionAnswerValuePo userQuestionAnswerValuePo);


}
