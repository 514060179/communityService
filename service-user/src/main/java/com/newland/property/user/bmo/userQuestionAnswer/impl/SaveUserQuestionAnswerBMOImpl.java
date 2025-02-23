package com.newland.property.user.bmo.userQuestionAnswer.impl;

import com.newland.property.core.annotation.PropertyTransactional;
import com.newland.property.core.factory.GenerateCodeFactory;
import com.newland.property.intf.user.IUserQuestionAnswerInnerServiceSMO;
import com.newland.property.po.user.UserQuestionAnswerPo;
import com.newland.property.user.bmo.userQuestionAnswer.ISaveUserQuestionAnswerBMO;
import com.newland.property.vo.ResultVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service("saveUserQuestionAnswerBMOImpl")
public class SaveUserQuestionAnswerBMOImpl implements ISaveUserQuestionAnswerBMO {

    @Autowired
    private IUserQuestionAnswerInnerServiceSMO userQuestionAnswerInnerServiceSMOImpl;

    /**
     * 添加小区信息
     *
     * @param userQuestionAnswerPo
     * @return 订单服务能够接受的报文
     */
    @Override
    @PropertyTransactional
    public ResponseEntity<String> save(UserQuestionAnswerPo userQuestionAnswerPo) {

        userQuestionAnswerPo.setUserQaId(GenerateCodeFactory.getGeneratorId(GenerateCodeFactory.CODE_PREFIX_userQaId));
        int flag = userQuestionAnswerInnerServiceSMOImpl.saveUserQuestionAnswer(userQuestionAnswerPo);

        if (flag > 0) {
            return ResultVo.createResponseEntity(ResultVo.CODE_OK, "保存成功");
        }

        return ResultVo.createResponseEntity(ResultVo.CODE_ERROR, "保存失败");
    }

}
