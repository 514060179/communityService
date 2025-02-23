package com.newland.property.common.cmd.workflow;

import com.alibaba.fastjson.JSONObject;
import com.newland.property.core.annotation.NewlandPropertyCmd;
import com.newland.property.core.context.ICmdDataFlowContext;
import com.newland.property.core.event.cmd.Cmd;
import com.newland.property.core.event.cmd.CmdEvent;
import com.newland.property.dto.oaWorkflow.WorkflowDto;
import com.newland.property.dto.oaWorkflow.WorkflowStepStaffDto;
import com.newland.property.intf.common.IWorkflowStepStaffInnerServiceSMO;
import com.newland.property.utils.exception.CmdException;
import com.newland.property.utils.util.BeanConvertUtil;
import com.newland.property.utils.util.StringUtil;
import com.newland.property.vo.ResultVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.ArrayList;
import java.util.List;

@NewlandPropertyCmd(serviceCode = "workflow.listWorkflowStepStaffs")
public class ListWorkflowStepStaffsCmd extends Cmd {

    @Autowired
    private IWorkflowStepStaffInnerServiceSMO workflowStepStaffInnerServiceSMOImpl;

    @Override
    public void validate(CmdEvent event, ICmdDataFlowContext context, JSONObject reqJson) throws CmdException {
        super.validatePageInfo(reqJson);
    }

    @Override
    public void doCmd(CmdEvent event, ICmdDataFlowContext context, JSONObject reqJson) throws CmdException {
        WorkflowStepStaffDto workflowStepStaffDto = BeanConvertUtil.covertBean(reqJson, WorkflowStepStaffDto.class);
        if (!StringUtil.isEmpty(reqJson.getString("requestType")) && "purchaseHandle".equals(reqJson.getString("requestType"))) {//采购
            workflowStepStaffDto.setFlowType(WorkflowDto.FLOW_TYPE_PURCHASE);
        }
        if (!StringUtil.isEmpty(reqJson.getString("requestType")) && "grantHandle".equals(reqJson.getString("requestType"))) {//领用
            workflowStepStaffDto.setFlowType(WorkflowDto.FLOW_TYPE_COLLECTION);
        }
        if (!StringUtil.isEmpty(reqJson.getString("requestType")) && "allocationHandle".equals(reqJson.getString("requestType"))) {//调拨
            String[] fllowTypes = new String[]{WorkflowDto.FLOW_TYPE_ALLOCATION_STOREHOUSE, WorkflowDto.FLOW_TYPE_ALLOCATION_STOREHOUSE_GO};
            workflowStepStaffDto.setFlowTypes(fllowTypes);
        }


        int count = workflowStepStaffInnerServiceSMOImpl.queryWorkflowStepStaffsCount(workflowStepStaffDto);

        List<WorkflowStepStaffDto> workflowStepStaffDtos = null;

        if (count > 0) {
            workflowStepStaffDtos = workflowStepStaffInnerServiceSMOImpl.queryWorkflowStepStaffs(workflowStepStaffDto);
        } else {
            workflowStepStaffDtos = new ArrayList<>();
        }

        ResultVo resultVo = new ResultVo((int) Math.ceil((double) count / (double) reqJson.getInteger("row")), count, workflowStepStaffDtos);

        ResponseEntity<String> responseEntity = new ResponseEntity<String>(resultVo.toString(), HttpStatus.OK);

        context.setResponseEntity(responseEntity);
    }
}
