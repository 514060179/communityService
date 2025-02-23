package com.newland.property.user.bmo.questionAnswerTitle;

import com.alibaba.fastjson.JSONArray;
import com.newland.property.po.questionAnswer.QuestionAnswerTitlePo;
import org.springframework.http.ResponseEntity;
public interface ISaveQuestionAnswerTitleBMO {


    /**
     * 添加答卷
     * add by wuxw
     * @param questionAnswerTitlePo
     * @return
     */
    ResponseEntity<String> save(QuestionAnswerTitlePo questionAnswerTitlePo, JSONArray titleValues);


}
