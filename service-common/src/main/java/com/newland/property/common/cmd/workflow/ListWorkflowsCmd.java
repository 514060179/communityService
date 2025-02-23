package com.newland.property.common.cmd.workflow;

import com.alibaba.fastjson.JSONObject;
import com.newland.property.core.annotation.NewlandPropertyCmd;
import com.newland.property.core.context.ICmdDataFlowContext;
import com.newland.property.core.event.cmd.Cmd;
import com.newland.property.core.event.cmd.CmdEvent;
import com.newland.property.dto.oaWorkflow.WorkflowDto;
import com.newland.property.intf.common.IWorkflowInnerServiceSMO;
import com.newland.property.utils.exception.CmdException;
import com.newland.property.utils.util.Assert;
import com.newland.property.utils.util.BeanConvertUtil;
import com.newland.property.vo.ResultVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.List;

@NewlandPropertyCmd(serviceCode = "workflow.listWorkflows")
public class ListWorkflowsCmd extends Cmd {

    @Autowired
    private IWorkflowInnerServiceSMO workflowInnerServiceSMOImpl;
    @Override
    public void validate(CmdEvent event, ICmdDataFlowContext context, JSONObject reqJson) throws CmdException {
        super.validatePageInfo(reqJson);
        Assert.hasKeyAndValue(reqJson, "communityId", "请求报文中请包含小区ID");
        Assert.hasKeyAndValue(reqJson, "storeId", "请求报文中请包含商户ID");
    }

    @Override
    public void doCmd(CmdEvent event, ICmdDataFlowContext context, JSONObject reqJson) throws CmdException {
        WorkflowDto workflowDto = BeanConvertUtil.covertBean(reqJson, WorkflowDto.class);
        int count = workflowInnerServiceSMOImpl.queryWorkflowsCount(workflowDto);
        List<WorkflowDto> workflowDtos = null;
        workflowDtos = workflowInnerServiceSMOImpl.queryWorkflows(workflowDto);
        ResultVo resultVo = new ResultVo((int) Math.ceil((double) count / (double) reqJson.getInteger("row")), count, workflowDtos);
        ResponseEntity<String> responseEntity = new ResponseEntity<String>(resultVo.toString(), HttpStatus.OK);
        context.setResponseEntity(responseEntity);
    }
}
