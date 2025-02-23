package com.newland.property.user.bmo.userQuestionAnswerValue.impl;

import com.newland.property.core.annotation.PropertyTransactional;
import com.newland.property.intf.user.IUserQuestionAnswerValueInnerServiceSMO;
import com.newland.property.po.user.UserQuestionAnswerValuePo;
import com.newland.property.user.bmo.userQuestionAnswerValue.IDeleteUserQuestionAnswerValueBMO;
import com.newland.property.vo.ResultVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service("deleteUserQuestionAnswerValueBMOImpl")
public class DeleteUserQuestionAnswerValueBMOImpl implements IDeleteUserQuestionAnswerValueBMO {

    @Autowired
    private IUserQuestionAnswerValueInnerServiceSMO userQuestionAnswerValueInnerServiceSMOImpl;

    /**
     * @param userQuestionAnswerValuePo 数据
     * @return 订单服务能够接受的报文
     */
    @Override
    @PropertyTransactional
    public ResponseEntity<String> delete(UserQuestionAnswerValuePo userQuestionAnswerValuePo) {

        int flag = userQuestionAnswerValueInnerServiceSMOImpl.deleteUserQuestionAnswerValue(userQuestionAnswerValuePo);

        if (flag > 0) {
            return ResultVo.createResponseEntity(ResultVo.CODE_OK, "保存成功");
        }

        return ResultVo.createResponseEntity(ResultVo.CODE_ERROR, "保存失败");
    }

}
