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
package com.newland.property.oa.cmd.workflowDataFile;

import com.alibaba.fastjson.JSONObject;
import com.newland.property.core.annotation.NewlandPropertyCmd;
import com.newland.property.core.annotation.PropertyTransactional;
import com.newland.property.core.context.ICmdDataFlowContext;
import com.newland.property.core.event.cmd.Cmd;
import com.newland.property.core.event.cmd.CmdEvent;
import com.newland.property.core.factory.GenerateCodeFactory;
import com.newland.property.intf.oa.IWorkflowDataFileV1InnerServiceSMO;
import com.newland.property.po.oaWorkflow.WorkflowDataFilePo;
import com.newland.property.utils.exception.CmdException;
import com.newland.property.utils.util.Assert;
import com.newland.property.utils.util.BeanConvertUtil;
import com.newland.property.vo.ResultVo;
import org.slf4j.Logger;
import com.newland.property.core.log.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * 类表述：保存
 * 服务编码：workflowDataFile.saveWorkflowDataFile
 * 请求路劲：/app/workflowDataFile.SaveWorkflowDataFile
 * add by 吴学文 at 2021-12-02 16:25:47 mail: 928255095@qq.com
 * open source address: https://gitee.com/wuxw7/MicroCommunity
 * 官网：http://www.homecommunity.cn
 * 温馨提示：如果您对此文件进行修改 请不要删除原有作者及注释信息，请补充您的 修改的原因以及联系邮箱如下
 * // modify by 张三 at 2021-09-12 第10行在某种场景下存在某种bug 需要修复，注释10至20行 加入 20行至30行
 */
@NewlandPropertyCmd(serviceCode = "workflowDataFile.saveWorkflowDataFile")
public class SaveWorkflowDataFileCmd extends Cmd {

    private static Logger logger = LoggerFactory.getLogger(SaveWorkflowDataFileCmd.class);

    public static final String CODE_PREFIX_ID = "10";

    @Autowired
    private IWorkflowDataFileV1InnerServiceSMO workflowDataFileV1InnerServiceSMOImpl;

    @Override
    public void validate(CmdEvent event, ICmdDataFlowContext cmdDataFlowContext, JSONObject reqJson) {
        Assert.hasKeyAndValue(reqJson, "fileId", "请求报文中未包含fileId");
        Assert.hasKeyAndValue(reqJson, "id", "请求报文中未包含id");
        Assert.hasKeyAndValue(reqJson, "storeId", "请求报文中未包含storeId");
        Assert.hasKeyAndValue(reqJson, "createUserName", "请求报文中未包含createUserName");

    }

    @Override
    @PropertyTransactional
    public void doCmd(CmdEvent event, ICmdDataFlowContext cmdDataFlowContext, JSONObject reqJson) throws CmdException {

        WorkflowDataFilePo workflowDataFilePo = BeanConvertUtil.covertBean(reqJson, WorkflowDataFilePo.class);
        workflowDataFilePo.setFileId(GenerateCodeFactory.getGeneratorId(CODE_PREFIX_ID));
        int flag = workflowDataFileV1InnerServiceSMOImpl.saveWorkflowDataFile(workflowDataFilePo);

        if (flag < 1) {
            throw new CmdException("保存数据失败");
        }

        cmdDataFlowContext.setResponseEntity(ResultVo.success());
    }
}
