package com.newland.property.user.bmo.questionAnswer.impl;

import com.newland.property.core.annotation.PropertyTransactional;
import com.newland.property.core.factory.GenerateCodeFactory;
import com.newland.property.intf.common.IFileRelInnerServiceSMO;
import com.newland.property.intf.user.IQuestionAnswerInnerServiceSMO;
import com.newland.property.po.questionAnswer.QuestionAnswerPo;
import com.newland.property.user.bmo.questionAnswer.ISaveQuestionAnswerBMO;
import com.newland.property.vo.ResultVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service("saveQuestionAnswerBMOImpl")
public class SaveQuestionAnswerBMOImpl implements ISaveQuestionAnswerBMO {

    @Autowired
    private IQuestionAnswerInnerServiceSMO questionAnswerInnerServiceSMOImpl;

    @Autowired
    private IFileRelInnerServiceSMO fileRelInnerServiceSMOImpl;

    /**
     * 添加小区信息
     *
     * @param questionAnswerPo
     * @return 订单服务能够接受的报文
     */
    @Override
    @PropertyTransactional
    public ResponseEntity<String> save(QuestionAnswerPo questionAnswerPo) {
        questionAnswerPo.setQaId(GenerateCodeFactory.getGeneratorId(GenerateCodeFactory.CODE_PREFIX_qaId));
        int flag = questionAnswerInnerServiceSMOImpl.saveQuestionAnswer(questionAnswerPo);
        if (flag > 0) {

            return ResultVo.createResponseEntity(ResultVo.CODE_OK, "保存成功");
        }
        return ResultVo.createResponseEntity(ResultVo.CODE_ERROR, "保存失败");
    }
}