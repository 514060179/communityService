/*
 * Copyright 2017-2020 吴学文 and newland property team.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.newland.property.user.cmd.question;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.newland.property.core.annotation.NewlandPropertyCmd;
import com.newland.property.core.annotation.PropertyTransactional;
import com.newland.property.core.context.ICmdDataFlowContext;
import com.newland.property.core.event.cmd.Cmd;
import com.newland.property.core.event.cmd.CmdEvent;
import com.newland.property.core.factory.GenerateCodeFactory;
import com.newland.property.dto.questionTitle.QuestionTitleDto;
import com.newland.property.intf.user.IQuestionTitleV1InnerServiceSMO;
import com.newland.property.intf.user.IQuestionTitleValueV1InnerServiceSMO;
import com.newland.property.po.questionTitle.QuestionTitlePo;
import com.newland.property.po.questionTitleValue.QuestionTitleValuePo;
import com.newland.property.utils.exception.CmdException;
import com.newland.property.utils.util.Assert;
import com.newland.property.utils.util.BeanConvertUtil;
import com.newland.property.vo.ResultVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 类表述：保存
 * 服务编码：questionTitle.saveQuestionTitle
 * 请求路劲：/app/questionTitle.SaveQuestionTitle
 * add by 吴学文 at 2023-07-10 15:10:47 mail: 928255095@qq.com
 * open source address: https://gitee.com/wuxw7/MicroCommunity
 * 官网：http://www.homecommunity.cn
 * 温馨提示：如果您对此文件进行修改 请不要删除原有作者及注释信息，请补充您的 修改的原因以及联系邮箱如下
 * // modify by 张三 at 2021-09-12 第10行在某种场景下存在某种bug 需要修复，注释10至20行 加入 20行至30行
 */
@NewlandPropertyCmd(serviceCode = "question.saveQuestionTitle")
public class SaveQuestionTitleCmd extends Cmd {

    private static Logger logger = LoggerFactory.getLogger(SaveQuestionTitleCmd.class);

    public static final String CODE_PREFIX_ID = "10";

    @Autowired
    private IQuestionTitleV1InnerServiceSMO questionTitleV1InnerServiceSMOImpl;

    @Autowired
    private IQuestionTitleValueV1InnerServiceSMO questionTitleValueV1InnerServiceSMOImpl;

    @Override
    public void validate(CmdEvent event, ICmdDataFlowContext cmdDataFlowContext, JSONObject reqJson) {
        Assert.hasKeyAndValue(reqJson, "qaTitle", "请求报文中未包含qaTitle");
        Assert.hasKeyAndValue(reqJson, "titleType", "请求报文中未包含titleType");
        Assert.hasKeyAndValue(reqJson, "communityId", "请求报文中未包含communityId");

        //todo 如果不是简答题 需要 包含选项
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
    public void doCmd(CmdEvent event, ICmdDataFlowContext cmdDataFlowContext, JSONObject reqJson) throws CmdException {

        QuestionTitlePo questionTitlePo = BeanConvertUtil.covertBean(reqJson, QuestionTitlePo.class);
        questionTitlePo.setTitleId(GenerateCodeFactory.getGeneratorId(CODE_PREFIX_ID));
        int flag = questionTitleV1InnerServiceSMOImpl.saveQuestionTitle(questionTitlePo);

        if (flag < 1) {
            throw new CmdException("保存数据失败");
        }

        //todo 简答题不用 写入，直接返回
        if (QuestionTitleDto.TITLE_TYPE_QUESTIONS.equals(questionTitlePo.getTitleType())) {
            cmdDataFlowContext.setResponseEntity(ResultVo.success());
            return;
        }
        JSONArray titleValues = reqJson.getJSONArray("titleValues");
        QuestionTitleValuePo questionTitleValuePo = null;
        JSONObject valueObj = null;
        for (int titleValueIndex = 0; titleValueIndex < titleValues.size(); titleValueIndex++) {
            valueObj = titleValues.getJSONObject(titleValueIndex);
            questionTitleValuePo = new QuestionTitleValuePo();
            questionTitleValuePo.setQaValue(valueObj.getString("itemValue"));
            questionTitleValuePo.setSeq((titleValueIndex+1)+"");
            if(valueObj.containsKey("score")) {
                questionTitleValuePo.setScore(valueObj.getString("score"));
            }else{
                questionTitleValuePo.setScore("0");
            }
            questionTitleValuePo.setTitleId(questionTitlePo.getTitleId());
            questionTitleValuePo.setValueId(GenerateCodeFactory.getGeneratorId(GenerateCodeFactory.CODE_PREFIX_valueId));
            questionTitleValuePo.setCommunityId(questionTitlePo.getCommunityId());
            questionTitleValueV1InnerServiceSMOImpl.saveQuestionTitleValue(questionTitleValuePo);
        }

        cmdDataFlowContext.setResponseEntity(ResultVo.success());
    }
}
