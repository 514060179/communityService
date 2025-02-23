package com.newland.property.common.cmd.workflow;

import com.alibaba.fastjson.JSONObject;
import com.newland.property.core.annotation.NewlandPropertyCmd;
import com.newland.property.core.context.ICmdDataFlowContext;
import com.newland.property.core.event.cmd.Cmd;
import com.newland.property.core.event.cmd.CmdEvent;
import com.newland.property.intf.common.IWorkflowV1InnerServiceSMO;
import com.newland.property.utils.exception.CmdException;
import com.newland.property.utils.util.Assert;
import com.newland.property.vo.ResultVo;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

@NewlandPropertyCmd(serviceCode = "workflow.listWorkflowNextNode")
public class ListWorkflowNextNodeCmd extends Cmd {

    @Autowired
    private IWorkflowV1InnerServiceSMO workflowV1InnerServiceSMOImpl;

    @Override
    public void validate(CmdEvent event, ICmdDataFlowContext cmdDataFlowContext, JSONObject reqJson) {
        Assert.hasKeyAndValue(reqJson, "taskId", "未包含任务ID");
        Assert.hasKeyAndValue(reqJson, "startUserId", "未包含提交者");
    }

    @Override
    public void doCmd(CmdEvent event, ICmdDataFlowContext cmdDataFlowContext, JSONObject reqJson) throws CmdException {
        JSONObject paramIn = new JSONObject();
        paramIn.put("taskId", reqJson.getString("taskId"));
        paramIn.put("startUserId", reqJson.getString("startUserId"));
        List<JSONObject> paramOuts = workflowV1InnerServiceSMOImpl.getWorkflowNextNode(paramIn);
        cmdDataFlowContext.setResponseEntity(ResultVo.createResponseEntity(paramOuts));
    }
}
