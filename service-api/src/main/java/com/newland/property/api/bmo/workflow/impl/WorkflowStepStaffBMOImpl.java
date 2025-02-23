package com.newland.property.api.bmo.workflow.impl;

import com.alibaba.fastjson.JSONObject;
import com.newland.property.api.bmo.ApiBaseBMO;
import com.newland.property.api.bmo.workflow.IWorkflowStepStaffBMO;
import com.newland.property.core.context.DataFlowContext;
import com.newland.property.intf.common.IWorkflowStepStaffInnerServiceSMO;
import com.newland.property.po.oaWorkflow.WorkflowStepStaffPo;
import com.newland.property.utils.constant.BusinessTypeConstant;
import com.newland.property.utils.util.BeanConvertUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service("workflowStepStaffBMOImpl")
public class WorkflowStepStaffBMOImpl extends ApiBaseBMO implements IWorkflowStepStaffBMO {

    @Autowired
    private IWorkflowStepStaffInnerServiceSMO workflowStepStaffInnerServiceSMOImpl;

    /**
     * 添加小区信息
     *
     * @param paramInJson     接口调用放传入入参
     * @param dataFlowContext 数据上下文
     * @return 订单服务能够接受的报文
     */
    @Override
    public void addWorkflowStepStaff(JSONObject paramInJson, DataFlowContext dataFlowContext) {

        paramInJson.put("wssId", "-1");
        WorkflowStepStaffPo workflowStepStaffPo = BeanConvertUtil.covertBean(paramInJson, WorkflowStepStaffPo.class);
        super.insert(dataFlowContext, workflowStepStaffPo, BusinessTypeConstant.BUSINESS_TYPE_SAVE_WORKFLOW_STEP_STAFF);
    }


    /**
     * 添加活动信息
     *
     * @param paramInJson     接口调用放传入入参
     * @param dataFlowContext 数据上下文
     * @return 订单服务能够接受的报文
     */
    @Override
    public void updateWorkflowStepStaff(JSONObject paramInJson, DataFlowContext dataFlowContext) {
        WorkflowStepStaffPo workflowStepStaffPo = BeanConvertUtil.covertBean(paramInJson, WorkflowStepStaffPo.class);
        super.update(dataFlowContext, workflowStepStaffPo, BusinessTypeConstant.BUSINESS_TYPE_UPDATE_WORKFLOW_STEP_STAFF);
    }



    /**
     * 添加小区信息
     *
     * @param paramInJson     接口调用放传入入参
     * @param dataFlowContext 数据上下文
     * @return 订单服务能够接受的报文
     */
    @Override
    public void deleteWorkflowStepStaff(JSONObject paramInJson, DataFlowContext dataFlowContext) {

        WorkflowStepStaffPo workflowStepStaffPo = BeanConvertUtil.covertBean(paramInJson, WorkflowStepStaffPo.class);
        super.delete(dataFlowContext, workflowStepStaffPo, BusinessTypeConstant.BUSINESS_TYPE_DELETE_WORKFLOW_STEP_STAFF);
    }

}
