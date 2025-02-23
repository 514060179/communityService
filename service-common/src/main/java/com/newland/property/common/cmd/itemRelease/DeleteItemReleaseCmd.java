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
package com.newland.property.common.cmd.itemRelease;

import com.alibaba.fastjson.JSONObject;
import com.newland.property.core.annotation.NewlandPropertyCmd;
import com.newland.property.core.annotation.PropertyTransactional;
import com.newland.property.core.context.ICmdDataFlowContext;
import com.newland.property.core.event.cmd.Cmd;
import com.newland.property.core.event.cmd.CmdEvent;
import com.newland.property.intf.common.IItemReleaseV1InnerServiceSMO;
import com.newland.property.po.itemRelease.ItemReleasePo;
import com.newland.property.doc.annotation.*;
import com.newland.property.utils.exception.CmdException;
import com.newland.property.utils.util.Assert;
import com.newland.property.utils.util.BeanConvertUtil;
import com.newland.property.vo.ResultVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@NewlandPropertyCmdDoc(title = "删除物品放行",
        description = "删除业主申请的物品放行功能",
        httpMethod = "post",
        url = "http://{ip}:{port}/app/itemRelease.deleteItemRelease",
        resource = "commonDoc",
        author = "吴学文",
        serviceCode = "itemRelease.deleteItemRelease"
)

@NewlandPropertyParamsDoc(params = {
        @NewlandPropertyParamDoc(name = "irId", length = 30, remark = "放行ID"),
        @NewlandPropertyParamDoc(name = "communityId", length = 30, remark = "放行小区"),
})

@NewlandPropertyResponseDoc(
        params = {
                @NewlandPropertyParamDoc(name = "code", type = "int", length = 11, defaultValue = "0", remark = "返回编号，0 成功 其他失败"),
                @NewlandPropertyParamDoc(name = "msg", type = "String", length = 250, defaultValue = "成功", remark = "描述"),
        }
)

@NewlandPropertyExampleDoc(
        reqBody="{'irId':'123','communityId':'123'}",
        resBody="{'code':0,'msg':'成功'}"
)

/**
 * 类表述：删除
 * 服务编码：itemRelease.deleteItemRelease
 * 请求路劲：/app/itemRelease.DeleteItemRelease
 * add by 吴学文 at 2023-01-11 15:40:01 mail: 928255095@qq.com
 * open source address: https://gitee.com/wuxw7/MicroCommunity
 * 官网：http://www.homecommunity.cn
 * 温馨提示：如果您对此文件进行修改 请不要删除原有作者及注释信息，请补充您的 修改的原因以及联系邮箱如下
 * // modify by 张三 at 2021-09-12 第10行在某种场景下存在某种bug 需要修复，注释10至20行 加入 20行至30行
 */
@NewlandPropertyCmd(serviceCode = "itemRelease.deleteItemRelease")
public class DeleteItemReleaseCmd extends Cmd {
    private static Logger logger = LoggerFactory.getLogger(DeleteItemReleaseCmd.class);

    @Autowired
    private IItemReleaseV1InnerServiceSMO itemReleaseV1InnerServiceSMOImpl;

    @Override
    public void validate(CmdEvent event, ICmdDataFlowContext cmdDataFlowContext, JSONObject reqJson) {
        Assert.hasKeyAndValue(reqJson, "irId", "irId不能为空");
        Assert.hasKeyAndValue(reqJson, "communityId", "communityId不能为空");

    }

    @Override
    @PropertyTransactional
    public void doCmd(CmdEvent event, ICmdDataFlowContext cmdDataFlowContext, JSONObject reqJson) throws CmdException {

        ItemReleasePo itemReleasePo = BeanConvertUtil.covertBean(reqJson, ItemReleasePo.class);
        int flag = itemReleaseV1InnerServiceSMOImpl.deleteItemRelease(itemReleasePo);

        if (flag < 1) {
            throw new CmdException("删除数据失败");
        }

        cmdDataFlowContext.setResponseEntity(ResultVo.success());
    }
}
