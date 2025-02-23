package com.newland.property.user.bmo.userQuestionAnswer.impl;

import com.newland.property.core.annotation.PropertyTransactional;
import com.newland.property.intf.user.IUserQuestionAnswerInnerServiceSMO;
import com.newland.property.po.user.UserQuestionAnswerPo;
import com.newland.property.user.bmo.userQuestionAnswer.IDeleteUserQuestionAnswerBMO;
import com.newland.property.vo.ResultVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service("deleteUserQuestionAnswerBMOImpl")
public class DeleteUserQuestionAnswerBMOImpl implements IDeleteUserQuestionAnswerBMO {

    @Autowired
    private IUserQuestionAnswerInnerServiceSMO userQuestionAnswerInnerServiceSMOImpl;

    /**
     * @param userQuestionAnswerPo 数据
     * @return 订单服务能够接受的报文
     */
    @Override
    @PropertyTransactional
    public ResponseEntity<String> delete(UserQuestionAnswerPo userQuestionAnswerPo) {

        int flag = userQuestionAnswerInnerServiceSMOImpl.deleteUserQuestionAnswer(userQuestionAnswerPo);

        if (flag > 0) {
            return ResultVo.createResponseEntity(ResultVo.CODE_OK, "保存成功");
        }

        return ResultVo.createResponseEntity(ResultVo.CODE_ERROR, "保存失败");
    }

}
