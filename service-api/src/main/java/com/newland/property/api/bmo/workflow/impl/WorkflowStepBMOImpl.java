package com.newland.property.api.bmo.workflow.impl;

import com.alibaba.fastjson.JSONObject;
import com.newland.property.api.bmo.ApiBaseBMO;
import com.newland.property.api.bmo.workflow.IWorkflowStepBMO;
import com.newland.property.core.context.DataFlowContext;
import com.newland.property.intf.common.IWorkflowStepInnerServiceSMO;
import com.newland.property.po.oaWorkflow.WorkflowStepPo;
import com.newland.property.utils.constant.BusinessTypeConstant;
import com.newland.property.utils.util.BeanConvertUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service("workflowStepBMOImpl")
public class WorkflowStepBMOImpl extends ApiBaseBMO implements IWorkflowStepBMO {

    @Autowired
    private IWorkflowStepInnerServiceSMO workflowStepInnerServiceSMOImpl;

    /**
     * 添加小区信息
     *
     * @param paramInJson     接口调用放传入入参
     * @param dataFlowContext 数据上下文
     * @return 订单服务能够接受的报文
     */
    @Override
    public void addWorkflowStep(JSONObject paramInJson, DataFlowContext dataFlowContext) {

        paramInJson.put("flowId", "-1");
        WorkflowStepPo workflowStepPo = BeanConvertUtil.covertBean(paramInJson, WorkflowStepPo.class);
        super.insert(dataFlowContext, workflowStepPo, BusinessTypeConstant.BUSINESS_TYPE_SAVE_WORKFLOW_STEP);
    }


    /**
     * 添加活动信息
     *
     * @param paramInJson     接口调用放传入入参
     * @param dataFlowContext 数据上下文
     * @return 订单服务能够接受的报文
     */
    @Override
    public void updateWorkflowStep(JSONObject paramInJson, DataFlowContext dataFlowContext) {
        WorkflowStepPo workflowStepPo = BeanConvertUtil.covertBean(paramInJson, WorkflowStepPo.class);
        super.update(dataFlowContext, workflowStepPo, BusinessTypeConstant.BUSINESS_TYPE_UPDATE_WORKFLOW_STEP);
    }



    /**
     * 添加小区信息
     *
     * @param paramInJson     接口调用放传入入参
     * @param dataFlowContext 数据上下文
     * @return 订单服务能够接受的报文
     */
    @Override
    public void deleteWorkflowStep(JSONObject paramInJson, DataFlowContext dataFlowContext) {

        WorkflowStepPo workflowStepPo = BeanConvertUtil.covertBean(paramInJson, WorkflowStepPo.class);
        super.update(dataFlowContext, workflowStepPo, BusinessTypeConstant.BUSINESS_TYPE_DELETE_WORKFLOW_STEP);
    }

}
