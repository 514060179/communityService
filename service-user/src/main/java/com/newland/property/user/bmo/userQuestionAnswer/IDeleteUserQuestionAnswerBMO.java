package com.newland.property.user.bmo.userQuestionAnswer;
import com.newland.property.po.user.UserQuestionAnswerPo;
import org.springframework.http.ResponseEntity;

public interface IDeleteUserQuestionAnswerBMO {


    /**
     * 修改答卷
     * add by wuxw
     * @param userQuestionAnswerPo
     * @return
     */
    ResponseEntity<String> delete(UserQuestionAnswerPo userQuestionAnswerPo);


}
