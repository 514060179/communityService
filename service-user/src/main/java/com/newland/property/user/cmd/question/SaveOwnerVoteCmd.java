package com.newland.property.user.cmd.question;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.newland.property.core.annotation.NewlandPropertyCmd;
import com.newland.property.core.annotation.PropertyTransactional;
import com.newland.property.core.context.ICmdDataFlowContext;
import com.newland.property.core.event.cmd.Cmd;
import com.newland.property.core.event.cmd.CmdEvent;
import com.newland.property.core.factory.GenerateCodeFactory;
import com.newland.property.dto.questionAnswer.QuestionAnswerDto;
import com.newland.property.dto.questionTitle.QuestionTitleDto;
import com.newland.property.intf.user.IQuestionAnswerTitleRelV1InnerServiceSMO;
import com.newland.property.intf.user.IQuestionAnswerV1InnerServiceSMO;
import com.newland.property.intf.user.IQuestionTitleV1InnerServiceSMO;
import com.newland.property.intf.user.IQuestionTitleValueV1InnerServiceSMO;
import com.newland.property.po.questionAnswer.QuestionAnswerPo;
import com.newland.property.po.questionAnswerTitleRel.QuestionAnswerTitleRelPo;
import com.newland.property.po.questionTitle.QuestionTitlePo;
import com.newland.property.po.questionTitleValue.QuestionTitleValuePo;
import com.newland.property.user.bmo.question.IQuestionAnswerBMO;
import com.newland.property.utils.exception.CmdException;
import com.newland.property.utils.util.Assert;
import com.newland.property.utils.util.StringUtil;
import org.springframework.beans.factory.annotation.Autowired;

import java.text.ParseException;

/**
 * 保存业主投票功能
 * add by wuxw 2023-07-12
 */

@NewlandPropertyCmd(serviceCode = "question.saveOwnerVote")
public class SaveOwnerVoteCmd extends Cmd {

    public static final String CODE_PREFIX_ID = "10";

    @Autowired
    private IQuestionTitleV1InnerServiceSMO questionTitleV1InnerServiceSMOImpl;

    @Autowired
    private IQuestionTitleValueV1InnerServiceSMO questionTitleValueV1InnerServiceSMOImpl;
    @Autowired
    private IQuestionAnswerV1InnerServiceSMO questionAnswerV1InnerServiceSMOImpl;

    @Autowired
    private IQuestionAnswerTitleRelV1InnerServiceSMO questionAnswerTitleRelV1InnerServiceSMOImpl;

    @Autowired
    private IQuestionAnswerBMO questionAnswerBMOImpl;


    @Override
    public void validate(CmdEvent event, ICmdDataFlowContext context, JSONObject reqJson) throws CmdException, ParseException {

        Assert.hasKeyAndValue(reqJson, "communityId", "请求报文中未包含communityId");
        Assert.hasKeyAndValue(reqJson, "qaName", "请求报文中未包含投票名称");
        Assert.hasKey(reqJson, "roomIds", "请求报文中未包含投票房屋");
        Assert.hasKeyAndValue(reqJson, "startTime", "未包含开始时间");
        Assert.hasKeyAndValue(reqJson, "endTime", "未包含结束时间");
        Assert.hasKeyAndValue(reqJson, "titleType", "未包含题目类型");
        Assert.hasKeyAndValue(reqJson, "content", "未包含说明");
        JSONArray titleValues = null;
        if (!QuestionTitleDto.TITLE_TYPE_QUESTIONS.equals(reqJson.getString("titleType"))) {
            titleValues = reqJson.getJSONArray("titleValues");
            if (titleValues.size() < 1) {
                throw new IllegalArgumentException("未包含选项");
            }
        }
    }

    @Override
    @PropertyTransactional
    public void doCmd(CmdEvent event, ICmdDataFlowContext context, JSONObject reqJson) throws CmdException, ParseException {

        //todo 写入题目信息
        QuestionTitlePo questionTitlePo = new QuestionTitlePo();
        questionTitlePo.setTitleId(GenerateCodeFactory.getGeneratorId(CODE_PREFIX_ID));
        questionTitlePo.setCommunityId(reqJson.getString("communityId"));
        questionTitlePo.setQaTitle(reqJson.getString("qaName"));
        questionTitlePo.setTitleType(reqJson.getString("titleType"));
        int flag = questionTitleV1InnerServiceSMOImpl.saveQuestionTitle(questionTitlePo);

        if (flag < 1) {
            throw new CmdException("保存数据失败");
        }

        JSONArray titleValues = reqJson.getJSONArray("titleValues");
        QuestionTitleValuePo questionTitleValuePo = null;
        JSONObject valueObj = null;
        for (int titleValueIndex = 0; titleValueIndex < titleValues.size(); titleValueIndex++) {
            valueObj = titleValues.getJSONObject(titleValueIndex);
            questionTitleValuePo = new QuestionTitleValuePo();
            questionTitleValuePo.setQaValue(valueObj.getString("itemValue"));
            questionTitleValuePo.setSeq((titleValueIndex + 1) + "");
            if (valueObj.containsKey("score")) {
                questionTitleValuePo.setScore(valueObj.getString("score"));
            } else {
                questionTitleValuePo.setScore("0");
            }
            questionTitleValuePo.setTitleId(questionTitlePo.getTitleId());
            questionTitleValuePo.setValueId(GenerateCodeFactory.getGeneratorId(GenerateCodeFactory.CODE_PREFIX_valueId));
            questionTitleValuePo.setCommunityId(questionTitlePo.getCommunityId());
            questionTitleValueV1InnerServiceSMOImpl.saveQuestionTitleValue(questionTitleValuePo);
        }

        //todo 写入投票信息
        QuestionAnswerPo questionAnswerPo = new QuestionAnswerPo();
        questionAnswerPo.setContent(reqJson.getString("content"));
        questionAnswerPo.setEndTime(reqJson.getString("endTime"));
        questionAnswerPo.setStartTime(reqJson.getString("startTime"));
        questionAnswerPo.setQaId(GenerateCodeFactory.getGeneratorId(CODE_PREFIX_ID));
        questionAnswerPo.setQaName(reqJson.getString("qaName"));
        questionAnswerPo.setCommunityId(reqJson.getString("communityId"));
        questionAnswerPo.setQaType(QuestionAnswerDto.QA_TYPE_VOTE);
        questionAnswerPo.setState(QuestionAnswerDto.STATE_WAIT);
        if (!StringUtil.isEmpty(reqJson.getString("communityId"))) {
            questionAnswerPo.setObjType("3306"); //3306 是小区，3307 是商户
            questionAnswerPo.setObjId(reqJson.getString("communityId"));
        } else if (!StringUtil.isEmpty(reqJson.getString("storeId"))) {
            questionAnswerPo.setObjType("3307"); //3306 是小区，3307 是商户
            questionAnswerPo.setObjId(reqJson.getString("storeId"));
        }
        questionAnswerV1InnerServiceSMOImpl.saveQuestionAnswer(questionAnswerPo);

        QuestionAnswerTitleRelPo questionAnswerTitleRelPo = new QuestionAnswerTitleRelPo();
        questionAnswerTitleRelPo.setCommunityId(reqJson.getString("communityId"));
        questionAnswerTitleRelPo.setTitleId(questionTitleValuePo.getTitleId());
        questionAnswerTitleRelPo.setSeq("1");
        questionAnswerTitleRelPo.setScore("0");
        questionAnswerTitleRelPo.setQaId(questionAnswerPo.getQaId());
        questionAnswerTitleRelPo.setQatrId(GenerateCodeFactory.getGeneratorId(CODE_PREFIX_ID));
        questionAnswerTitleRelV1InnerServiceSMOImpl.saveQuestionAnswerTitleRel(questionAnswerTitleRelPo);

        questionAnswerBMOImpl.saveUserQuestionAnswer(questionAnswerPo, reqJson.getJSONArray("roomIds"));
    }
}
