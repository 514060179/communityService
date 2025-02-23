package com.newland.property.common.bmo.workflowAuditMessage.impl;

import com.newland.property.common.bmo.workflowAuditMessage.IDeleteWorkflowAuditMessageBMO;
import com.newland.property.core.annotation.PropertyTransactional;
import com.newland.property.intf.common.IWorkflowAuditMessageInnerServiceSMO;
import com.newland.property.po.oaWorkflow.WorkflowAuditMessagePo;
import com.newland.property.vo.ResultVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service("deleteWorkflowAuditMessageBMOImpl")
public class DeleteWorkflowAuditMessageBMOImpl implements IDeleteWorkflowAuditMessageBMO {

    @Autowired
    private IWorkflowAuditMessageInnerServiceSMO workflowAuditMessageInnerServiceSMOImpl;

    /**
     * @param workflowAuditMessagePo 数据
     * @return 订单服务能够接受的报文
     */
    @Override
    @PropertyTransactional
    public ResponseEntity<String> delete(WorkflowAuditMessagePo workflowAuditMessagePo) {

        int flag = workflowAuditMessageInnerServiceSMOImpl.deleteWorkflowAuditMessage(workflowAuditMessagePo);

        if (flag > 0) {
            return ResultVo.createResponseEntity(ResultVo.CODE_OK, "保存成功");
        }

        return ResultVo.createResponseEntity(ResultVo.CODE_ERROR, "保存失败");
    }

}
