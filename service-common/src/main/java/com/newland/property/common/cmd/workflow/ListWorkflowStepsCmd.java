package com.newland.property.common.cmd.workflow;

import com.alibaba.fastjson.JSONObject;
import com.newland.property.core.annotation.NewlandPropertyCmd;
import com.newland.property.core.context.ICmdDataFlowContext;
import com.newland.property.core.event.cmd.Cmd;
import com.newland.property.core.event.cmd.CmdEvent;
import com.newland.property.dto.oaWorkflow.WorkflowDto;
import com.newland.property.dto.oaWorkflow.WorkflowStepDto;
import com.newland.property.dto.oaWorkflow.WorkflowStepStaffDto;
import com.newland.property.intf.common.IWorkflowInnerServiceSMO;
import com.newland.property.intf.common.IWorkflowStepInnerServiceSMO;
import com.newland.property.intf.common.IWorkflowStepStaffInnerServiceSMO;
import com.newland.property.utils.exception.CmdException;
import com.newland.property.utils.util.Assert;
import com.newland.property.utils.util.BeanConvertUtil;
import com.newland.property.vo.ResultVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.List;

@NewlandPropertyCmd(serviceCode = "workflow.listWorkflowSteps")
public class ListWorkflowStepsCmd extends Cmd {

    @Autowired
    private IWorkflowStepInnerServiceSMO workflowStepInnerServiceSMOImpl;

    @Autowired
    private IWorkflowInnerServiceSMO workflowInnerServiceSMOImpl;

    @Autowired
    private IWorkflowStepStaffInnerServiceSMO workflowStepStaffInnerServiceSMOImpl;

    @Override
    public void validate(CmdEvent event, ICmdDataFlowContext context, JSONObject reqJson) throws CmdException {

        Assert.hasKeyAndValue(reqJson, "flowId", "未包含流程ID");
        Assert.hasKeyAndValue(reqJson, "communityId", "未包含小区ID");
    }

    @Override
    public void doCmd(CmdEvent event, ICmdDataFlowContext context, JSONObject reqJson) throws CmdException {
        WorkflowDto workflowDto = BeanConvertUtil.covertBean(reqJson, WorkflowDto.class);
        List<WorkflowDto> workflowDtos = workflowInnerServiceSMOImpl.queryWorkflows(workflowDto);

        Assert.listOnlyOne(workflowDtos,"查询条件错误");

        workflowDto = workflowDtos.get(0);

        WorkflowStepDto workflowStepDto = new WorkflowStepDto();
        workflowStepDto.setFlowId(workflowDto.getFlowId());

        List<WorkflowStepDto> workflowStepDtos = workflowStepInnerServiceSMOImpl.queryWorkflowSteps(workflowStepDto);

        for(WorkflowStepDto tmpWorkflowStepDto : workflowStepDtos){
            WorkflowStepStaffDto workflowStepStaffDto = new WorkflowStepStaffDto();
            workflowStepStaffDto.setCommunityId(workflowDto.getCommunityId());
            workflowStepStaffDto.setStepId(tmpWorkflowStepDto.getStepId());
            List<WorkflowStepStaffDto> workflowStepStaffDtos = workflowStepStaffInnerServiceSMOImpl.queryWorkflowStepStaffs(workflowStepStaffDto);
            tmpWorkflowStepDto.setWorkflowStepStaffs(workflowStepStaffDtos);
        }

        workflowDto.setWorkflowSteps(workflowStepDtos);

        ResultVo resultVo = new ResultVo(ResultVo.CODE_OK, ResultVo.MSG_OK, workflowDto);

        ResponseEntity<String> responseEntity = new ResponseEntity<String>(resultVo.toString(), HttpStatus.OK);

        context.setResponseEntity(responseEntity);
    }
}
