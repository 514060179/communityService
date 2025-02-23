package com.newland.property.user.cmd.question;

import com.alibaba.fastjson.JSONObject;
import com.newland.property.core.annotation.NewlandPropertyCmd;
import com.newland.property.core.context.ICmdDataFlowContext;
import com.newland.property.core.event.cmd.Cmd;
import com.newland.property.core.event.cmd.CmdEvent;
import com.newland.property.dto.questionAnswer.QuestionAnswerDto;
import com.newland.property.intf.user.IQuestionAnswerV1InnerServiceSMO;
import com.newland.property.po.questionAnswer.QuestionAnswerPo;
import com.newland.property.utils.exception.CmdException;
import com.newland.property.utils.util.Assert;
import org.springframework.beans.factory.annotation.Autowired;

import java.text.ParseException;

/**
 * 发布 投票和问卷
 */
@NewlandPropertyCmd(serviceCode = "question.publishQuestion")
public class PublishQuestionCmd extends Cmd {

    @Autowired
    private IQuestionAnswerV1InnerServiceSMO questionAnswerV1InnerServiceSMOImpl;

    @Override
    public void validate(CmdEvent event, ICmdDataFlowContext context, JSONObject reqJson) throws CmdException, ParseException {
        Assert.hasKeyAndValue(reqJson, "communityId", "未包含小区信息");
        Assert.hasKeyAndValue(reqJson, "qaId", "未包含qaId");

    }

    @Override
    public void doCmd(CmdEvent event, ICmdDataFlowContext context, JSONObject reqJson) throws CmdException, ParseException {

        QuestionAnswerPo questionAnswerPo = new QuestionAnswerPo();
        questionAnswerPo.setQaId(reqJson.getString("qaId"));
        questionAnswerPo.setCommunityId(reqJson.getString("communityId"));
        questionAnswerPo.setState(QuestionAnswerDto.STATE_COMPLETE);
        int flag = questionAnswerV1InnerServiceSMOImpl.updateQuestionAnswer(questionAnswerPo);
        if (flag < 1) {
            throw new CmdException("发布失败");
        }

        //todo 通知信息推送模块

    }
}
