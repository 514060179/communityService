package com.newland.property.api.bmo.workflow.impl;

import com.alibaba.fastjson.JSONObject;
import com.newland.property.api.bmo.ApiBaseBMO;
import com.newland.property.api.bmo.workflow.IWorkflowBMO;
import com.newland.property.core.context.DataFlowContext;
import com.newland.property.intf.common.IWorkflowInnerServiceSMO;
import com.newland.property.po.oaWorkflow.WorkflowPo;
import com.newland.property.utils.constant.BusinessTypeConstant;
import com.newland.property.utils.util.BeanConvertUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service("workflowBMOImpl")
public class WorkflowBMOImpl extends ApiBaseBMO implements IWorkflowBMO {

    @Autowired
    private IWorkflowInnerServiceSMO workflowInnerServiceSMOImpl;

    /**
     * 添加小区信息
     *
     * @param paramInJson     接口调用放传入入参
     * @param dataFlowContext 数据上下文
     * @return 订单服务能够接受的报文
     */
    @Override
    public void addWorkflow(JSONObject paramInJson, DataFlowContext dataFlowContext) {

        paramInJson.put("flowId", "-1");
        WorkflowPo workflowPo = BeanConvertUtil.covertBean(paramInJson, WorkflowPo.class);
        super.insert(dataFlowContext, workflowPo, BusinessTypeConstant.BUSINESS_TYPE_SAVE_WORKFLOW);
    }


    /**
     * 添加活动信息
     *
     * @param paramInJson     接口调用放传入入参
     * @param dataFlowContext 数据上下文
     * @return 订单服务能够接受的报文
     */
    @Override
    public void updateWorkflow(JSONObject paramInJson, DataFlowContext dataFlowContext) {
        WorkflowPo workflowPo = BeanConvertUtil.covertBean(paramInJson, WorkflowPo.class);
        super.update(dataFlowContext, workflowPo, BusinessTypeConstant.BUSINESS_TYPE_UPDATE_WORKFLOW);
    }


    /**
     * 添加小区信息
     *
     * @param paramInJson     接口调用放传入入参
     * @param dataFlowContext 数据上下文
     * @return 订单服务能够接受的报文
     */
    @Override
    public void deleteWorkflow(JSONObject paramInJson, DataFlowContext dataFlowContext) {

        WorkflowPo workflowPo = BeanConvertUtil.covertBean(paramInJson, WorkflowPo.class);
        super.update(dataFlowContext, workflowPo, BusinessTypeConstant.BUSINESS_TYPE_DELETE_WORKFLOW);
    }

}
