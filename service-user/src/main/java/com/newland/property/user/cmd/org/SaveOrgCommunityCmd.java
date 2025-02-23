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
package com.newland.property.user.cmd.org;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.newland.property.core.annotation.NewlandPropertyCmd;
import com.newland.property.core.annotation.PropertyTransactional;
import com.newland.property.core.context.ICmdDataFlowContext;
import com.newland.property.core.event.cmd.Cmd;
import com.newland.property.core.event.cmd.CmdEvent;
import com.newland.property.core.factory.GenerateCodeFactory;
import com.newland.property.intf.user.IOrgCommunityV1InnerServiceSMO;
import com.newland.property.po.community.OrgCommunityPo;
import com.newland.property.utils.exception.CmdException;
import com.newland.property.utils.util.Assert;
import com.newland.property.utils.util.BeanConvertUtil;
import com.newland.property.vo.ResultVo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * 类表述：保存
 * 服务编码：orgCommunity.saveOrgCommunity
 * 请求路劲：/app/orgCommunity.SaveOrgCommunity
 * add by 吴学文 at 2022-08-08 18:05:07 mail: 928255095@qq.com
 * open source address: https://gitee.com/wuxw7/MicroCommunity
 * 官网：http://www.homecommunity.cn
 * 温馨提示：如果您对此文件进行修改 请不要删除原有作者及注释信息，请补充您的 修改的原因以及联系邮箱如下
 * // modify by 张三 at 2021-09-12 第10行在某种场景下存在某种bug 需要修复，注释10至20行 加入 20行至30行
 */
@NewlandPropertyCmd(serviceCode = "org.saveOrgCommunity")
public class SaveOrgCommunityCmd extends Cmd {

    private static Logger logger = LoggerFactory.getLogger(SaveOrgCommunityCmd.class);

    public static final String CODE_PREFIX_ID = "10";

    @Autowired
    private IOrgCommunityV1InnerServiceSMO orgCommunityV1InnerServiceSMOImpl;

    @Override
    public void validate(CmdEvent event, ICmdDataFlowContext cmdDataFlowContext, JSONObject reqJson) {
        Assert.hasKeyAndValue(reqJson, "orgId", "必填，请填写组织ID");
        Assert.hasKeyAndValue(reqJson, "orgName", "必填，请填写组织名称");
        if (!reqJson.containsKey("communitys") || reqJson.getJSONArray("communitys").size() < 1) {
            throw new IllegalArgumentException("未包含小区信息");
        }

    }

    @Override
    @PropertyTransactional
    public void doCmd(CmdEvent event, ICmdDataFlowContext cmdDataFlowContext, JSONObject reqJson) throws CmdException {

        JSONArray communitys = reqJson.getJSONArray("communitys");
        for (int communityIndex = 0; communityIndex < communitys.size(); communityIndex++) {
            addOrgCommunity(reqJson, communitys.getJSONObject(communityIndex), communityIndex);
        }

        cmdDataFlowContext.setResponseEntity(ResultVo.success());
    }

    /**
     * 添加小区信息
     *
     * @param paramInJson 接口调用放传入入参
     * @return 订单服务能够接受的报文
     */
    public void addOrgCommunity(JSONObject paramInJson, JSONObject communityObj, int seq) {

        JSONObject businessOrg = new JSONObject();
        businessOrg.putAll(paramInJson);
        businessOrg.put("communityId", communityObj.getString("communityId"));
        businessOrg.put("communityName", communityObj.getString("communityName"));

        OrgCommunityPo orgCommunityPo = BeanConvertUtil.covertBean(businessOrg, OrgCommunityPo.class);
        orgCommunityPo.setOrgCommunityId(GenerateCodeFactory.getGeneratorId(CODE_PREFIX_ID));

        int flag = orgCommunityV1InnerServiceSMOImpl.saveOrgCommunity(orgCommunityPo);

        if (flag < 1) {
            throw new CmdException("保存数据失败");
        }
    }
}
