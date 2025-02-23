package com.newland.property.user.bmo.userQuestionAnswerValue.impl;

import com.newland.property.core.annotation.PropertyTransactional;
import com.newland.property.intf.user.IUserQuestionAnswerValueInnerServiceSMO;
import com.newland.property.po.user.UserQuestionAnswerValuePo;
import com.newland.property.user.bmo.userQuestionAnswerValue.IUpdateUserQuestionAnswerValueBMO;
import com.newland.property.vo.ResultVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service("updateUserQuestionAnswerValueBMOImpl")
public class UpdateUserQuestionAnswerValueBMOImpl implements IUpdateUserQuestionAnswerValueBMO {

    @Autowired
    private IUserQuestionAnswerValueInnerServiceSMO userQuestionAnswerValueInnerServiceSMOImpl;

    /**
     * @param userQuestionAnswerValuePo
     * @return 订单服务能够接受的报文
     */
    @Override
    @PropertyTransactional
    public ResponseEntity<String> update(UserQuestionAnswerValuePo userQuestionAnswerValuePo) {

        int flag = userQuestionAnswerValueInnerServiceSMOImpl.updateUserQuestionAnswerValue(userQuestionAnswerValuePo);

        if (flag > 0) {
            return ResultVo.createResponseEntity(ResultVo.CODE_OK, "保存成功");
        }

        return ResultVo.createResponseEntity(ResultVo.CODE_ERROR, "保存失败");
    }

}
