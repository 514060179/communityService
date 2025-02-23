package com.newland.property.user.cmd.question;

import com.alibaba.fastjson.JSONObject;
import com.newland.property.core.annotation.NewlandPropertyCmd;
import com.newland.property.core.context.ICmdDataFlowContext;
import com.newland.property.core.event.cmd.Cmd;
import com.newland.property.core.event.cmd.CmdEvent;
import com.newland.property.dto.questionAnswer.QuestionAnswerDto;
import com.newland.property.dto.questionTitleValue.QuestionTitleValueDto;
import com.newland.property.intf.user.IQuestionAnswerV1InnerServiceSMO;
import com.newland.property.intf.user.IQuestionTitleV1InnerServiceSMO;
import com.newland.property.intf.user.IQuestionTitleValueV1InnerServiceSMO;
import com.newland.property.utils.exception.CmdException;
import com.newland.property.utils.util.Assert;
import com.newland.property.utils.util.BeanConvertUtil;
import com.newland.property.vo.ResultVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

/**
 * 查询业主投票
 */
@NewlandPropertyCmd(serviceCode = "question.listOwnerVote")
public class ListOwnerVoteCmd extends Cmd {

    @Autowired
    private IQuestionAnswerV1InnerServiceSMO questionAnswerV1InnerServiceSMOImpl;


    @Autowired
    private IQuestionTitleV1InnerServiceSMO questionTitleV1InnerServiceSMOImpl;

    @Autowired
    private IQuestionTitleValueV1InnerServiceSMO questionTitleValueV1InnerServiceSMOImpl;

    @Override
    public void validate(CmdEvent event, ICmdDataFlowContext context, JSONObject reqJson) throws CmdException, ParseException {
        Assert.hasKeyAndValue(reqJson, "communityId", "未包含小区信息");
    }

    @Override
    public void doCmd(CmdEvent event, ICmdDataFlowContext context, JSONObject reqJson) throws CmdException, ParseException {

        QuestionAnswerDto questionAnswerDto = BeanConvertUtil.covertBean(reqJson, QuestionAnswerDto.class);
        questionAnswerDto.setQaType(QuestionAnswerDto.QA_TYPE_VOTE);
        int count = questionAnswerV1InnerServiceSMOImpl.queryQuestionAnswersCount(questionAnswerDto);

        List<QuestionAnswerDto> questionAnswerDtos = null;

        if (count > 0) {
            questionAnswerDtos = questionAnswerV1InnerServiceSMOImpl.queryQuestionAnswers(questionAnswerDto);
        } else {
            questionAnswerDtos = new ArrayList<>();
        }

        //todo 刷入选项，并且计算投票人数
        computeQuestionAnswerValue(questionAnswerDtos);

        ResultVo resultVo = new ResultVo((int) Math.ceil((double) count / (double) reqJson.getInteger("row")), count, questionAnswerDtos);

        ResponseEntity<String> responseEntity = new ResponseEntity<String>(resultVo.toString(), HttpStatus.OK);

        context.setResponseEntity(responseEntity);
    }

    /**
     * 计算 投票选项 投票情况
     *
     * @param questionAnswerDtos
     */
    private void computeQuestionAnswerValue(List<QuestionAnswerDto> questionAnswerDtos) {
        if (questionAnswerDtos == null || questionAnswerDtos.size() < 1) {
            return;
        }

        List<String> qaId = new ArrayList<>();
        for (QuestionAnswerDto tmpQuestionAnswerDto : questionAnswerDtos) {
            qaId.add(tmpQuestionAnswerDto.getQaId());
        }

        QuestionTitleValueDto questionTitleValueDto = new QuestionTitleValueDto();
        questionTitleValueDto.setQaIds(qaId.toArray(new String[qaId.size()]));
        questionTitleValueDto.setCommunityId(questionAnswerDtos.get(0).getCommunityId());
        List<QuestionTitleValueDto> questionTitleValueDtos = questionTitleValueV1InnerServiceSMOImpl.queryQuestionOwnerTitleValues(questionTitleValueDto);
        if (questionTitleValueDtos == null || questionTitleValueDtos.size() < 1) {
            return;
        }
        List<QuestionTitleValueDto> values = null;
        long votedCount = 0;
        for (QuestionAnswerDto tmpQuestionAnswerDto : questionAnswerDtos) {
            values = new ArrayList<>();
            votedCount = 0;
            for (QuestionTitleValueDto tmpQuestionTitleValueDto : questionTitleValueDtos) {
                if (!tmpQuestionTitleValueDto.getQaId().equals(tmpQuestionAnswerDto.getQaId())) {
                    continue;
                }
                values.add(tmpQuestionTitleValueDto);
                tmpQuestionAnswerDto.setTitleType(tmpQuestionTitleValueDto.getTitleType());
                votedCount += tmpQuestionTitleValueDto.getPersonCount();
            }
            tmpQuestionAnswerDto.setTitleValues(values);
            tmpQuestionAnswerDto.setVotedCount(votedCount);
        }
    }
}
