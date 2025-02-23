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
package com.newland.property.community.cmd.communitySpace;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.newland.property.core.annotation.NewlandPropertyCmd;
import com.newland.property.core.annotation.PropertyTransactional;
import com.newland.property.core.context.ICmdDataFlowContext;
import com.newland.property.core.event.cmd.Cmd;
import com.newland.property.core.event.cmd.CmdEvent;
import com.newland.property.core.factory.GenerateCodeFactory;
import com.newland.property.dto.community.CommunitySpacePersonDto;
import com.newland.property.dto.community.CommunitySpacePersonTimeDto;
import com.newland.property.intf.community.ICommunitySpacePersonTimeV1InnerServiceSMO;
import com.newland.property.intf.community.ICommunitySpacePersonV1InnerServiceSMO;
import com.newland.property.po.communitySpace.CommunitySpacePersonPo;
import com.newland.property.po.communitySpace.CommunitySpacePersonTimePo;
import com.newland.property.doc.annotation.*;
import com.newland.property.utils.exception.CmdException;
import com.newland.property.utils.util.Assert;
import com.newland.property.utils.util.BeanConvertUtil;
import com.newland.property.utils.util.StringUtil;
import com.newland.property.vo.ResultVo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;


@NewlandPropertyCmdDoc(title = "预约场地",
        description = "系统中的预约场地",
        httpMethod = "post",
        url = "http://{ip}:{port}/app/communitySpace.saveCommunitySpacePerson",
        resource = "communityDoc",
        author = "吴学文",
        serviceCode = "communitySpace.saveCommunitySpacePerson"
)

@NewlandPropertyParamsDoc(params = {
        @NewlandPropertyParamDoc(name = "page",type = "int",length = 11, remark = "分页页数"),
        @NewlandPropertyParamDoc(name = "row",type = "int", length = 11, remark = "分页行数"),
        @NewlandPropertyParamDoc(name = "communityId", length = 30, remark = "小区ID"),
        @NewlandPropertyParamDoc(name = "appointmentDate", length = 30, remark = "预约日期 YYYY-MM-DD"),
        @NewlandPropertyParamDoc(name = "appointmentTime", length = 30, remark = "预约时间 HH24:MI"),
        @NewlandPropertyParamDoc(name = "payWay", length = 12, remark = "支付方式"),
        @NewlandPropertyParamDoc(name = "personName", length = 64, remark = "预约人"),
        @NewlandPropertyParamDoc(name = "personTel", length = 30, remark = "预约人电话"),
        @NewlandPropertyParamDoc(name = "receivableAmount", length = 30, remark = "应收金额"),
        @NewlandPropertyParamDoc(name = "receivedAmount", length = 30, remark = "实收金额"),
        @NewlandPropertyParamDoc(name = "spaceId", length = 30, remark = "场地ID"),
        @NewlandPropertyParamDoc(name = "openTimes",type="Array", length = 30, remark = "场地ID"),
        @NewlandPropertyParamDoc(parentNodeName = "openTimes",name = "hours", length = 30, remark = "预约时间"),

})

@NewlandPropertyResponseDoc(
        params = {
                @NewlandPropertyParamDoc(name = "code", type = "int", length = 11, defaultValue = "0", remark = "返回编号，0 成功 其他失败"),
                @NewlandPropertyParamDoc(name = "msg", type = "String", length = 250, defaultValue = "成功", remark = "描述"),

        }
)

@NewlandPropertyExampleDoc(
        reqBody="{\"spaceId\":\"102022093043260007\",\"personName\":\"wuxw\",\"personTel\":\"18909711443\",\"appointmentTime\":\"01:00\",\"receivableAmount\":\"10\",\"receivedAmount\":\"10\",\"payWay\":\"2\",\"state\":\"S\",\"remark\":\"123\",\"appointmentDate\":\"2022-09-01\",\"communityId\":\"2022081539020475\"}",
        resBody="{\"code\":0,\"msg\":\"成功\"}"
)

/**
 * 类表述：保存
 * 服务编码：communitySpacePerson.saveCommunitySpacePerson
 * 请求路劲：/app/communitySpacePerson.SaveCommunitySpacePerson
 * add by 吴学文 at 2022-09-30 11:36:52 mail: 928255095@qq.com
 * open source address: https://gitee.com/wuxw7/MicroCommunity
 * 官网：http://www.homecommunity.cn
 * 温馨提示：如果您对此文件进行修改 请不要删除原有作者及注释信息，请补充您的 修改的原因以及联系邮箱如下
 * // modify by 张三 at 2021-09-12 第10行在某种场景下存在某种bug 需要修复，注释10至20行 加入 20行至30行
 */
@NewlandPropertyCmd(serviceCode = "communitySpace.saveCommunitySpacePerson")
public class SaveCommunitySpacePersonCmd extends Cmd {

    private static Logger logger = LoggerFactory.getLogger(SaveCommunitySpacePersonCmd.class);

    public static final String CODE_PREFIX_ID = "10";

    @Autowired
    private ICommunitySpacePersonV1InnerServiceSMO communitySpacePersonV1InnerServiceSMOImpl;

    @Autowired
    private ICommunitySpacePersonTimeV1InnerServiceSMO communitySpacePersonTimeV1InnerServiceSMOImpl;

    @Override
    public void validate(CmdEvent event, ICmdDataFlowContext cmdDataFlowContext, JSONObject reqJson) {
        Assert.hasKeyAndValue(reqJson, "spaceId", "请求报文中未包含spaceId");
        Assert.hasKeyAndValue(reqJson, "personName", "请求报文中未包含personName");
        Assert.hasKeyAndValue(reqJson, "personTel", "请求报文中未包含personTel");
        Assert.hasKeyAndValue(reqJson, "appointmentTime", "请求报文中未包含appointmentTime");
        Assert.hasKeyAndValue(reqJson, "receivableAmount", "请求报文中未包含receivableAmount");
        Assert.hasKeyAndValue(reqJson, "receivedAmount", "请求报文中未包含receivedAmount");
        Assert.hasKeyAndValue(reqJson, "payWay", "请求报文中未包含payWay");
        Assert.hasKeyAndValue(reqJson, "communityId", "请求报文中未包含communityId");

        if(!reqJson.containsKey("openTimes")){
            cmdDataFlowContext.setResponseEntity(ResultVo.success());
            return ;
        }

        JSONArray openTimes = reqJson.getJSONArray("openTimes");

        if(openTimes == null || openTimes.size() <1){
            cmdDataFlowContext.setResponseEntity(ResultVo.success());
            return ;
        }
        CommunitySpacePersonTimeDto communitySpacePersonTimeDto = null;
        int flag = 0;
        for(int timeIndex = 0 ;timeIndex < openTimes.size(); timeIndex++) {
            if("N".equals(openTimes.getJSONObject(timeIndex).getString("isOpen"))){
                continue;
            }
            communitySpacePersonTimeDto = new CommunitySpacePersonTimeDto();
            communitySpacePersonTimeDto.setCommunityId(reqJson.getString("communityId"));
            communitySpacePersonTimeDto.setAppointmentTime(reqJson.getString("appointmentTime"));
            communitySpacePersonTimeDto.setHours(openTimes.getJSONObject(timeIndex).getString("hours"));
            communitySpacePersonTimeDto.setSpaceId(reqJson.getString("spaceId"));
            flag = communitySpacePersonTimeV1InnerServiceSMOImpl.queryCommunitySpacePersonTimesCount(communitySpacePersonTimeDto);
            if(flag > 0){
                throw new CmdException(reqJson.getString("appointmentTime")+","+openTimes.getJSONObject(timeIndex).getString("hours")+"已经被预约");
            }
        }

    }

    @Override
    @PropertyTransactional
    public void doCmd(CmdEvent event, ICmdDataFlowContext cmdDataFlowContext, JSONObject reqJson) throws CmdException {



        CommunitySpacePersonPo communitySpacePersonPo = BeanConvertUtil.covertBean(reqJson, CommunitySpacePersonPo.class);
        communitySpacePersonPo.setCspId(GenerateCodeFactory.getGeneratorId(CODE_PREFIX_ID));
        communitySpacePersonPo.setOrderId(GenerateCodeFactory.getGeneratorId("11"));
        if(StringUtil.isEmpty(communitySpacePersonPo.getState())){
            communitySpacePersonPo.setState(CommunitySpacePersonDto.STATE_W);
        }
        int flag = communitySpacePersonV1InnerServiceSMOImpl.saveCommunitySpacePerson(communitySpacePersonPo);

        if (flag < 1) {
            throw new CmdException("保存数据失败");
        }

        if(!reqJson.containsKey("openTimes")){
            cmdDataFlowContext.setResponseEntity(ResultVo.success());
            return ;
        }

        JSONArray openTimes = reqJson.getJSONArray("openTimes");

        if(openTimes == null || openTimes.size() <1){
            cmdDataFlowContext.setResponseEntity(ResultVo.success());
            return ;
        }
        CommunitySpacePersonTimePo communitySpacePersonTimePo = null;
        for(int timeIndex = 0 ;timeIndex < openTimes.size(); timeIndex++) {
            if("N".equals(openTimes.getJSONObject(timeIndex).getString("isOpen"))){
                continue;
            }
            communitySpacePersonTimePo = new CommunitySpacePersonTimePo();
            communitySpacePersonTimePo.setCommunityId(communitySpacePersonPo.getCommunityId());
            communitySpacePersonTimePo.setCspId(communitySpacePersonPo.getCspId());
            communitySpacePersonTimePo.setHours(openTimes.getJSONObject(timeIndex).getString("hours"));
            communitySpacePersonTimePo.setSpaceId(communitySpacePersonPo.getSpaceId());
            communitySpacePersonTimePo.setTimeId(GenerateCodeFactory.getGeneratorId(CODE_PREFIX_ID));
            communitySpacePersonTimePo.setState(CommunitySpacePersonTimeDto.STATE_WAIT_CONFIRM);
            communitySpacePersonTimeV1InnerServiceSMOImpl.saveCommunitySpacePersonTime(communitySpacePersonTimePo);
        }

        cmdDataFlowContext.setResponseEntity(ResultVo.success());
    }
}
