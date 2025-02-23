package com.newland.property.user.bmo.userQuestionAnswerValue.impl;

import com.newland.property.dto.user.UserQuestionAnswerValueDto;
import com.newland.property.intf.user.IUserQuestionAnswerValueInnerServiceSMO;
import com.newland.property.user.bmo.userQuestionAnswerValue.IGetUserQuestionAnswerValueBMO;
import com.newland.property.vo.ResultVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service("getUserQuestionAnswerValueBMOImpl")
public class GetUserQuestionAnswerValueBMOImpl implements IGetUserQuestionAnswerValueBMO {

    @Autowired
    private IUserQuestionAnswerValueInnerServiceSMO userQuestionAnswerValueInnerServiceSMOImpl;

    /**
     * @param userQuestionAnswerValueDto
     * @return 订单服务能够接受的报文
     */
    @Override
    public ResponseEntity<String> get(UserQuestionAnswerValueDto userQuestionAnswerValueDto) {


        int count = userQuestionAnswerValueInnerServiceSMOImpl.queryUserQuestionAnswerValuesCount(userQuestionAnswerValueDto);

        List<UserQuestionAnswerValueDto> userQuestionAnswerValueDtos = null;
        if (count > 0) {
            userQuestionAnswerValueDtos = userQuestionAnswerValueInnerServiceSMOImpl.queryUserQuestionAnswerValues(userQuestionAnswerValueDto);
        } else {
            userQuestionAnswerValueDtos = new ArrayList<>();
        }

        ResultVo resultVo = new ResultVo((int) Math.ceil((double) count / (double) userQuestionAnswerValueDto.getRow()), count, userQuestionAnswerValueDtos);

        ResponseEntity<String> responseEntity = new ResponseEntity<String>(resultVo.toString(), HttpStatus.OK);

        return responseEntity;
    }

}
