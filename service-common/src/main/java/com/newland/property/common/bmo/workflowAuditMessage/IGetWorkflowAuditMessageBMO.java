package com.newland.property.common.bmo.workflowAuditMessage;
import com.newland.property.dto.oaWorkflow.WorkflowAuditMessageDto;
import org.springframework.http.ResponseEntity;
public interface IGetWorkflowAuditMessageBMO {


    /**
     * 查询流程审核表
     * add by wuxw
     * @param  workflowAuditMessageDto
     * @return
     */
    ResponseEntity<String> get(WorkflowAuditMessageDto workflowAuditMessageDto);


}
