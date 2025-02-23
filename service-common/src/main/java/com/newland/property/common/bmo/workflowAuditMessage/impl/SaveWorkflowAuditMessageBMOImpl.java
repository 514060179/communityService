package com.newland.property.common.bmo.workflowAuditMessage.impl;

import com.newland.property.common.bmo.workflowAuditMessage.ISaveWorkflowAuditMessageBMO;
import com.newland.property.core.annotation.PropertyTransactional;
import com.newland.property.core.factory.GenerateCodeFactory;
import com.newland.property.intf.common.IWorkflowAuditMessageInnerServiceSMO;
import com.newland.property.po.oaWorkflow.WorkflowAuditMessagePo;
import com.newland.property.vo.ResultVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service("saveWorkflowAuditMessageBMOImpl")
public class SaveWorkflowAuditMessageBMOImpl implements ISaveWorkflowAuditMessageBMO {

    @Autowired
    private IWorkflowAuditMessageInnerServiceSMO workflowAuditMessageInnerServiceSMOImpl;

    /**
     * 添加小区信息
     *
     * @param workflowAuditMessagePo
     * @return 订单服务能够接受的报文
     */
    @Override
    @PropertyTransactional
    public ResponseEntity<String> save(WorkflowAuditMessagePo workflowAuditMessagePo) {

        workflowAuditMessagePo.setAuditId(GenerateCodeFactory.getGeneratorId(GenerateCodeFactory.CODE_PREFIX_auditId));
        int flag = workflowAuditMessageInnerServiceSMOImpl.saveWorkflowAuditMessage(workflowAuditMessagePo);

        if (flag > 0) {
            return ResultVo.createResponseEntity(ResultVo.CODE_OK, "保存成功");
        }

        return ResultVo.createResponseEntity(ResultVo.CODE_ERROR, "保存失败");
    }

}
