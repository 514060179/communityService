package com.newland.property.user.bmo.questionAnswerTitle;

import com.alibaba.fastjson.JSONArray;
import com.newland.property.po.questionAnswer.QuestionAnswerTitlePo;
import org.springframework.http.ResponseEntity;

public interface IUpdateQuestionAnswerTitleBMO {


    /**
     * 修改答卷
     * add by wuxw
     *
     * @param questionAnswerTitlePo
     * @return
     */
    ResponseEntity<String> update(QuestionAnswerTitlePo questionAnswerTitlePo, JSONArray titleValues);


}
