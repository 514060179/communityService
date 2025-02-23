package com.newland.property.user.cmd.question;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.newland.property.core.annotation.NewlandPropertyCmd;
import com.newland.property.core.annotation.PropertyTransactional;
import com.newland.property.core.context.ICmdDataFlowContext;
import com.newland.property.core.event.cmd.Cmd;
import com.newland.property.core.event.cmd.CmdEvent;
import com.newland.property.core.factory.GenerateCodeFactory;
import com.newland.property.dto.user.UserDto;
import com.newland.property.dto.user.UserQuestionAnswerDto;
import com.newland.property.intf.user.IUserQuestionAnswerV1InnerServiceSMO;
import com.newland.property.intf.user.IUserQuestionAnswerValueV1InnerServiceSMO;
import com.newland.property.intf.user.IUserV1InnerServiceSMO;
import com.newland.property.po.user.UserQuestionAnswerPo;
import com.newland.property.po.user.UserQuestionAnswerValuePo;
import com.newland.property.utils.exception.CmdException;
import com.newland.property.utils.util.Assert;
import com.newland.property.utils.util.StringUtil;
import com.newland.property.vo.ResultVo;
import org.springframework.beans.factory.annotation.Autowired;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

@NewlandPropertyCmd(serviceCode = "question.saveOwnerQuestionAnswer")
public class SaveOwnerQuestionAnswerCmd extends Cmd {

    @Autowired
    private IUserQuestionAnswerV1InnerServiceSMO userQuestionAnswerV1InnerServiceSMOImpl;

    @Autowired
    private IUserV1InnerServiceSMO userV1InnerServiceSMOImpl;

    @Autowired
    private IUserQuestionAnswerValueV1InnerServiceSMO userQuestionAnswerValueV1InnerServiceSMOImpl;

    @Override
    public void validate(CmdEvent event, ICmdDataFlowContext context, JSONObject reqJson) throws CmdException, ParseException {
        Assert.hasKeyAndValue(reqJson, "communityId", "未包含小区");
        Assert.hasKeyAndValue(reqJson, "userQaId", "未包含题目");

        Assert.hasKey(reqJson, "questionAnswerTitles", "未包含答案");

        JSONArray questionAnswerTitles = reqJson.getJSONArray("questionAnswerTitles");

        if (questionAnswerTitles == null || questionAnswerTitles.size() < 1) {
            throw new IllegalArgumentException("未包含答案");
        }

        JSONObject titleObj = null;
        for (int questionAnswerTitleIndex = 0; questionAnswerTitleIndex < questionAnswerTitles.size(); questionAnswerTitleIndex++) {
            titleObj = questionAnswerTitles.getJSONObject(questionAnswerTitleIndex);
            if (titleObj.containsKey("qaTitle") && !StringUtil.isEmpty(titleObj.getString("qaTitle"))) {
                Assert.hasKeyAndValue(titleObj, "valueContent", titleObj.getString("qaTitle") + ",未填写答案");
            } else {
                Assert.hasKeyAndValue(titleObj, "valueContent", "未填写答案");
            }
        }

        String userId = context.getReqHeaders().get("user-id");
        Assert.hasLength(userId, "用户未登录");

        UserDto userDto = new UserDto();
        userDto.setUserId(userId);
        List<UserDto> userDtos = userV1InnerServiceSMOImpl.queryUsers(userDto);

        Assert.listOnlyOne(userDtos, "用户不存在");

        UserQuestionAnswerDto userQuestionAnswerDto = new UserQuestionAnswerDto();
        userQuestionAnswerDto.setUserQaId(reqJson.getString("userQaId"));
        userQuestionAnswerDto.setCommunityId(reqJson.getString("communityId"));
        userQuestionAnswerDto.setLink(userDtos.get(0).getTel());
        int count = userQuestionAnswerV1InnerServiceSMOImpl.queryUserQuestionAnswersCount(userQuestionAnswerDto);
        if (count < 1) {
            throw new CmdException("数据不存在");
        }
    }

    @Override
    @PropertyTransactional
    public void doCmd(CmdEvent event, ICmdDataFlowContext context, JSONObject reqJson) throws CmdException, ParseException {

        UserQuestionAnswerDto userQuestionAnswerDto = new UserQuestionAnswerDto();
        userQuestionAnswerDto.setUserQaId(reqJson.getString("userQaId"));
        userQuestionAnswerDto.setCommunityId(reqJson.getString("communityId"));
        List<UserQuestionAnswerDto> userQuestionAnswers = userQuestionAnswerV1InnerServiceSMOImpl.queryUserQuestionAnswers(userQuestionAnswerDto);

        Assert.listOnlyOne(userQuestionAnswers, "数据不存在");

        JSONArray questionAnswerTitles = reqJson.getJSONArray("questionAnswerTitles");
        JSONObject titleObj = null;
        UserQuestionAnswerValuePo tmpUserUserQuestionAnswerValue = null;
        List<UserQuestionAnswerValuePo> tmpUserUserQuestionAnswerValues = new ArrayList<>();

        for (int questionAnswerTitleIndex = 0; questionAnswerTitleIndex < questionAnswerTitles.size(); questionAnswerTitleIndex++) {
            titleObj = questionAnswerTitles.getJSONObject(questionAnswerTitleIndex);
            tmpUserUserQuestionAnswerValue = new UserQuestionAnswerValuePo();
            tmpUserUserQuestionAnswerValue.setCommunityId(userQuestionAnswers.get(0).getCommunityId());
            tmpUserUserQuestionAnswerValue.setQaId(userQuestionAnswers.get(0).getQaId());
            tmpUserUserQuestionAnswerValue.setTitleId(titleObj.getString("titleId"));
            tmpUserUserQuestionAnswerValue.setUserQaId(userQuestionAnswers.get(0).getUserQaId());
            tmpUserUserQuestionAnswerValue.setUserTitleId(GenerateCodeFactory.getGeneratorId(GenerateCodeFactory.CODE_PREFIX_userTitleId));
            if ("3003".equals(titleObj.getString("titleType"))) {
                tmpUserUserQuestionAnswerValue.setValueId("999");
                tmpUserUserQuestionAnswerValue.setValueContent(titleObj.getString("valueContent"));
            } else {
                tmpUserUserQuestionAnswerValue.setValueId(titleObj.getString("valueContent"));
                tmpUserUserQuestionAnswerValue.setValueContent(titleObj.getString("valueContent"));
            }
            tmpUserUserQuestionAnswerValues.add(tmpUserUserQuestionAnswerValue);
        }

        int flag = userQuestionAnswerValueV1InnerServiceSMOImpl.saveUserQuestionAnswerValues(tmpUserUserQuestionAnswerValues);
        if (flag < 0) {
            throw new IllegalArgumentException("保存失败");
        }

        UserQuestionAnswerPo userQuestionAnswerPo = new UserQuestionAnswerPo();

        userQuestionAnswerPo.setUserQaId(userQuestionAnswers.get(0).getUserQaId());
        userQuestionAnswerPo.setState("1202");
        flag = userQuestionAnswerV1InnerServiceSMOImpl.updateUserQuestionAnswer(userQuestionAnswerPo);
        if (flag < 0) {
            throw new IllegalArgumentException("保存失败");
        }
        context.setResponseEntity(ResultVo.createResponseEntity(ResultVo.CODE_OK, "保存成功"));


    }
}
