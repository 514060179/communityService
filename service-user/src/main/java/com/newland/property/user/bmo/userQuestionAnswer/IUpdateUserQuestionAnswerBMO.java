package com.newland.property.user.bmo.userQuestionAnswer;
import com.newland.property.po.user.UserQuestionAnswerPo;
import org.springframework.http.ResponseEntity;

public interface IUpdateUserQuestionAnswerBMO {


    /**
     * 修改答卷
     * add by wuxw
     * @param userQuestionAnswerPo
     * @return
     */
    ResponseEntity<String> update(UserQuestionAnswerPo userQuestionAnswerPo);


}
