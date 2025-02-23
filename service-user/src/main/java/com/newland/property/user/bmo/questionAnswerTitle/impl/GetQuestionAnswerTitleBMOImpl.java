package com.newland.property.user.bmo.questionAnswerTitle.impl;

import com.newland.property.dto.questionAnswer.QuestionAnswerTitleDto;
import com.newland.property.dto.questionAnswer.QuestionAnswerTitleValueDto;
import com.newland.property.intf.user.IQuestionAnswerTitleInnerServiceSMO;
import com.newland.property.intf.user.IQuestionAnswerTitleValueInnerServiceSMO;
import com.newland.property.user.bmo.questionAnswerTitle.IGetQuestionAnswerTitleBMO;
import com.newland.property.vo.ResultVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service("getQuestionAnswerTitleBMOImpl")
public class GetQuestionAnswerTitleBMOImpl implements IGetQuestionAnswerTitleBMO {

    @Autowired
    private IQuestionAnswerTitleInnerServiceSMO questionAnswerTitleInnerServiceSMOImpl;

    @Autowired
    private IQuestionAnswerTitleValueInnerServiceSMO questionAnswerTitleValueInnerServiceSMOImpl;

    /**
     * @param questionAnswerTitleDto
     * @return 订单服务能够接受的报文
     */
    @Override
    public ResponseEntity<String> get(QuestionAnswerTitleDto questionAnswerTitleDto) {


        int count = questionAnswerTitleInnerServiceSMOImpl.queryQuestionAnswerTitlesCount(questionAnswerTitleDto);

        List<QuestionAnswerTitleDto> questionAnswerTitleDtos = null;
        if (count > 0) {
            questionAnswerTitleDtos = questionAnswerTitleInnerServiceSMOImpl.queryQuestionAnswerTitles(questionAnswerTitleDto);

            refreshTitileValues(questionAnswerTitleDtos);
        } else {
            questionAnswerTitleDtos = new ArrayList<>();
        }

        ResultVo resultVo = new ResultVo((int) Math.ceil((double) count / (double) questionAnswerTitleDto.getRow()), count, questionAnswerTitleDtos);

        ResponseEntity<String> responseEntity = new ResponseEntity<String>(resultVo.toString(), HttpStatus.OK);

        return responseEntity;
    }

    private void refreshTitileValues(List<QuestionAnswerTitleDto> questionAnswerTitleDtos) {

        if (questionAnswerTitleDtos == null || questionAnswerTitleDtos.size() < 1) {
            return;
        }

        List<String> titleIds = new ArrayList<>();
        for (QuestionAnswerTitleDto questionAnswerTitleDto : questionAnswerTitleDtos) {
            titleIds.add(questionAnswerTitleDto.getTitleId());
        }

        QuestionAnswerTitleValueDto questionAnswerTitleValueDto = new QuestionAnswerTitleValueDto();
        questionAnswerTitleValueDto.setTitleIds(titleIds.toArray(new String[titleIds.size()]));
        questionAnswerTitleValueDto.setObjId(questionAnswerTitleDtos.get(0).getObjId());
        List<QuestionAnswerTitleValueDto> questionAnswerTitleValueDtos
                = questionAnswerTitleValueInnerServiceSMOImpl.queryQuestionAnswerTitleValues(questionAnswerTitleValueDto);

        List<QuestionAnswerTitleValueDto> tmpQuestionAnswerTitleValueDtos = null;
        for (QuestionAnswerTitleDto questionAnswerTitleDto : questionAnswerTitleDtos) {
            tmpQuestionAnswerTitleValueDtos = new ArrayList<>();
            for (QuestionAnswerTitleValueDto questionAnswerTitleValueDto1 : questionAnswerTitleValueDtos) {
                if (questionAnswerTitleDto.getTitleId().equals(questionAnswerTitleValueDto1.getTitleId())) {
                    tmpQuestionAnswerTitleValueDtos.add(questionAnswerTitleValueDto1);
                }
            }
            questionAnswerTitleDto.setQuestionAnswerTitleValues(tmpQuestionAnswerTitleValueDtos);
        }


    }

}
