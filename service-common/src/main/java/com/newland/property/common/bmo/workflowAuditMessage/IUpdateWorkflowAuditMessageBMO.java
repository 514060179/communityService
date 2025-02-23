package com.newland.property.common.bmo.workflowAuditMessage;

import com.newland.property.po.oaWorkflow.WorkflowAuditMessagePo;
import org.springframework.http.ResponseEntity;

public interface IUpdateWorkflowAuditMessageBMO {


    /**
     * 修改流程审核表
     * add by wuxw
     *
     * @param workflowAuditMessagePo
     * @return
     */
    ResponseEntity<String> update(WorkflowAuditMessagePo workflowAuditMessagePo);


}
