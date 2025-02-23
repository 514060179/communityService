package com.newland.property.common.bmo.workflowAuditMessage.impl;

import com.newland.property.common.bmo.workflowAuditMessage.IGetWorkflowAuditMessageBMO;
import com.newland.property.dto.oaWorkflow.WorkflowAuditMessageDto;
import com.newland.property.intf.common.IWorkflowAuditMessageInnerServiceSMO;
import com.newland.property.vo.ResultVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service("getWorkflowAuditMessageBMOImpl")
public class GetWorkflowAuditMessageBMOImpl implements IGetWorkflowAuditMessageBMO {

    @Autowired
    private IWorkflowAuditMessageInnerServiceSMO workflowAuditMessageInnerServiceSMOImpl;

    /**
     * @param workflowAuditMessageDto
     * @return 订单服务能够接受的报文
     */
    @Override
    public ResponseEntity<String> get(WorkflowAuditMessageDto workflowAuditMessageDto) {


        int count = workflowAuditMessageInnerServiceSMOImpl.queryWorkflowAuditMessagesCount(workflowAuditMessageDto);

        List<WorkflowAuditMessageDto> workflowAuditMessageDtos = null;
        if (count > 0) {
            workflowAuditMessageDtos = workflowAuditMessageInnerServiceSMOImpl.queryWorkflowAuditMessages(workflowAuditMessageDto);
        } else {
            workflowAuditMessageDtos = new ArrayList<>();
        }

        ResultVo resultVo = new ResultVo((int) Math.ceil((double) count / (double) workflowAuditMessageDto.getRow()), count, workflowAuditMessageDtos);

        ResponseEntity<String> responseEntity = new ResponseEntity<String>(resultVo.toString(), HttpStatus.OK);

        return responseEntity;
    }

}
