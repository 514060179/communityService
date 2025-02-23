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
import com.newland.property.core.factory.GenerateCodeFactory;
import com.newland.property.dto.oaWorkflow.OaWorkflowDto;
import com.newland.property.dto.oaWorkflow.WorkflowModelDto;
import com.newland.property.intf.common.IItemReleaseTypeV1InnerServiceSMO;
import com.newland.property.intf.common.IWorkflowInnerServiceSMO;
import com.newland.property.intf.oa.IOaWorkflowInnerServiceSMO;
import com.newland.property.po.itemRelease.ItemReleaseTypePo;
import com.newland.property.po.oaWorkflow.OaWorkflowPo;
import com.newland.property.doc.annotation.*;
import com.newland.property.utils.exception.CmdException;
import com.newland.property.utils.util.Assert;
import com.newland.property.utils.util.BeanConvertUtil;
import com.newland.property.vo.ResultVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;



@NewlandPropertyCmdDoc(title = "保存物品放行类型",
        description = "保存物品放行类型",
        httpMethod = "post",
        url = "http://{ip}:{port}/app/itemRelease.saveItemReleaseType",
        resource = "commonDoc",
        author = "吴学文",
        serviceCode = "itemRelease.saveItemReleaseType"
)

@NewlandPropertyParamsDoc(params = {
        @NewlandPropertyParamDoc(name = "communityId", length = 30, remark = "放行小区"),
        @NewlandPropertyParamDoc(name = "typeName", length = 30, remark = "类型名称"),
})

@NewlandPropertyResponseDoc(
        params = {
                @NewlandPropertyParamDoc(name = "code", type = "int", length = 11, defaultValue = "0", remark = "返回编号，0 成功 其他失败"),
                @NewlandPropertyParamDoc(name = "msg", type = "String", length = 250, defaultValue = "成功", remark = "描述"),
        }
)

@NewlandPropertyExampleDoc(
        reqBody="{'typeName':'123','communityId':'123'}",
        resBody="{'code':0,'msg':'成功'}"
)
/**
 * 类表述：保存
 * 服务编码：itemReleaseType.saveItemReleaseType
 * 请求路劲：/app/itemReleaseType.SaveItemReleaseType
 * add by 吴学文 at 2023-01-11 15:33:47 mail: 928255095@qq.com
 * open source address: https://gitee.com/wuxw7/MicroCommunity
 * 官网：http://www.homecommunity.cn
 * 温馨提示：如果您对此文件进行修改 请不要删除原有作者及注释信息，请补充您的 修改的原因以及联系邮箱如下
 * // modify by 张三 at 2021-09-12 第10行在某种场景下存在某种bug 需要修复，注释10至20行 加入 20行至30行
 */
@NewlandPropertyCmd(serviceCode = "itemRelease.saveItemReleaseType")
public class SaveItemReleaseTypeCmd extends Cmd {

    private static Logger logger = LoggerFactory.getLogger(SaveItemReleaseTypeCmd.class);

    public static final String CODE_PREFIX_ID = "10";

    @Autowired
    private IItemReleaseTypeV1InnerServiceSMO itemReleaseTypeV1InnerServiceSMOImpl;


    @Autowired
    private IWorkflowInnerServiceSMO workflowInnerServiceSMOImpl;

    @Autowired
    private IOaWorkflowInnerServiceSMO oaWorkflowInnerServiceSMOImpl;

    @Override
    public void validate(CmdEvent event, ICmdDataFlowContext cmdDataFlowContext, JSONObject reqJson) {
        Assert.hasKeyAndValue(reqJson, "typeName", "请求报文中未包含typeName");
        Assert.hasKeyAndValue(reqJson, "communityId", "请求报文中未包含communityId");

    }

    @Override
    @PropertyTransactional
    public void doCmd(CmdEvent event, ICmdDataFlowContext cmdDataFlowContext, JSONObject reqJson) throws CmdException {
        String storeId = cmdDataFlowContext.getReqHeaders().get("store-id");
        OaWorkflowPo oaWorkflowPo = new OaWorkflowPo();
        oaWorkflowPo.setStoreId(storeId);
        oaWorkflowPo.setFlowId(GenerateCodeFactory.getGeneratorId(GenerateCodeFactory.CODE_PREFIX_flowId));
        oaWorkflowPo.setFlowName(reqJson.getString("typeName")+"审批流程");
        oaWorkflowPo.setFlowType(OaWorkflowDto.FLOW_TYPE_ITEM_RELEASE);

        //创建model
        WorkflowModelDto workflowModelDto = new WorkflowModelDto();
        workflowModelDto.setName(oaWorkflowPo.getFlowName());
        workflowModelDto.setKey(oaWorkflowPo.getFlowId());
        workflowModelDto = workflowInnerServiceSMOImpl.createModel(workflowModelDto);

        oaWorkflowPo.setModelId(workflowModelDto.getModelId());
        oaWorkflowPo.setFlowKey(workflowModelDto.getKey());
        oaWorkflowPo.setState(OaWorkflowDto.STATE_WAIT);
        int flag = oaWorkflowInnerServiceSMOImpl.saveOaWorkflow(oaWorkflowPo);
        if (flag < 1) {
            throw new CmdException("保存数据失败");
        }


        ItemReleaseTypePo itemReleaseTypePo = BeanConvertUtil.covertBean(reqJson, ItemReleaseTypePo.class);
        itemReleaseTypePo.setTypeId(GenerateCodeFactory.getGeneratorId(CODE_PREFIX_ID));
        itemReleaseTypePo.setFlowId(oaWorkflowPo.getFlowId());
        itemReleaseTypePo.setFlowName(oaWorkflowPo.getFlowName());
         flag = itemReleaseTypeV1InnerServiceSMOImpl.saveItemReleaseType(itemReleaseTypePo);

        if (flag < 1) {
            throw new CmdException("保存数据失败");
        }

        cmdDataFlowContext.setResponseEntity(ResultVo.success());
    }
}
