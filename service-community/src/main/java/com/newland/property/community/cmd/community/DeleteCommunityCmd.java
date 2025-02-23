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
package com.newland.property.community.cmd.community;

import com.alibaba.fastjson.JSONObject;
import com.newland.property.core.annotation.NewlandPropertyCmd;
import com.newland.property.core.annotation.PropertyTransactional;
import com.newland.property.core.context.Environment;
import com.newland.property.core.context.ICmdDataFlowContext;
import com.newland.property.core.event.cmd.Cmd;
import com.newland.property.core.event.cmd.CmdEvent;
import com.newland.property.dto.community.CommunityDto;
import com.newland.property.intf.community.ICommunityInnerServiceSMO;
import com.newland.property.intf.community.ICommunityV1InnerServiceSMO;
import com.newland.property.po.community.CommunityPo;
import com.newland.property.doc.annotation.*;
import com.newland.property.utils.exception.CmdException;
import com.newland.property.utils.util.Assert;
import com.newland.property.utils.util.BeanConvertUtil;
import com.newland.property.vo.ResultVo;
import org.slf4j.Logger;
import com.newland.property.core.log.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

@NewlandPropertyCmdDoc(title = "删除小区",
        description = "主要提供给外系统删除小区",
        httpMethod = "post",
        url = "http://{ip}:{port}/app/community.deleteCommunity",
        resource = "communityDoc",
        author = "吴学文",
        serviceCode = "community.deleteCommunity",
        seq = 3
)

@NewlandPropertyParamsDoc(params = {
        @NewlandPropertyParamDoc(name = "communityId", length = 30, remark = "小区编码"),
})

@NewlandPropertyResponseDoc(
        params = {
                @NewlandPropertyParamDoc(name = "code", type = "int", length = 11, defaultValue = "0", remark = "返回编号，0 成功 其他失败"),
                @NewlandPropertyParamDoc(name = "msg", type = "String", length = 250, defaultValue = "成功", remark = "描述"),
        }
)

@NewlandPropertyExampleDoc(
        reqBody="{\"communityId\":\"2022092293190329\"}",
        resBody="{'code':0,'msg':'成功'}"
)
/**
 * 类表述：删除
 * 服务编码：community.deleteCommunity
 * 请求路劲：/app/community.DeleteCommunity
 * add by 吴学文 at 2021-09-18 12:54:57 mail: 928255095@qq.com
 * open source address: https://gitee.com/wuxw7/MicroCommunity
 * 官网：http://www.homecommunity.cn
 * 温馨提示：如果您对此文件进行修改 请不要删除原有作者及注释信息，请补充您的 修改的原因以及联系邮箱如下
 * // modify by 张三 at 2021-09-12 第10行在某种场景下存在某种bug 需要修复，注释10至20行 加入 20行至30行
 */
@NewlandPropertyCmd(serviceCode = "community.deleteCommunity")
public class DeleteCommunityCmd extends Cmd {
    private static Logger logger = LoggerFactory.getLogger(DeleteCommunityCmd.class);

    @Autowired
    private ICommunityV1InnerServiceSMO communityV1InnerServiceSMOImpl;

    @Autowired
    private ICommunityInnerServiceSMO communityInnerServiceSMOImpl;

    @Override
    public void validate(CmdEvent event, ICmdDataFlowContext cmdDataFlowContext, JSONObject reqJson) {
        Environment.isDevEnv();

        Assert.hasKeyAndValue(reqJson, "communityId", "小区ID不能为空");

    }

    @Override
    @PropertyTransactional
    public void doCmd(CmdEvent event, ICmdDataFlowContext cmdDataFlowContext, JSONObject reqJson) throws CmdException {
        CommunityDto communityDto = new CommunityDto();
        communityDto.setCommunityId((String) reqJson.get("communityId"));
        List<CommunityDto> communityDtos = communityInnerServiceSMOImpl.queryCommunitys(communityDto);
        if (communityDtos.size() == 0 || communityDtos == null) {
            throw new IllegalArgumentException("没有查询到communityId为：" + communityDto.getCommunityId() + "小区信息");
        }
//        if ("1100".equals(communityDtos.get(0).getState())) {
//            throw new IllegalArgumentException("删除失败,该小区已审核通过");
//        }
        CommunityPo communityPo = BeanConvertUtil.covertBean(reqJson, CommunityPo.class);
        int flag = communityV1InnerServiceSMOImpl.deleteCommunity(communityPo);

        if (flag < 1) {
            throw new CmdException("删除数据失败");
        }

        cmdDataFlowContext.setResponseEntity(ResultVo.success());
    }
}
