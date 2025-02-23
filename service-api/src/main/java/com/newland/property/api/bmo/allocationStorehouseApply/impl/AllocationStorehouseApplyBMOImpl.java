package com.newland.property.api.bmo.allocationStorehouseApply.impl;

import com.alibaba.fastjson.JSONObject;
import com.newland.property.api.bmo.ApiBaseBMO;
import com.newland.property.api.bmo.allocationStorehouseApply.IAllocationStorehouseApplyBMO;
import com.newland.property.core.context.DataFlowContext;
import com.newland.property.intf.store.IAllocationStorehouseApplyInnerServiceSMO;
import com.newland.property.po.purchase.AllocationStorehouseApplyPo;
import com.newland.property.utils.constant.BusinessTypeConstant;
import com.newland.property.utils.util.BeanConvertUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service("allocationStorehouseApplyBMOImpl")
public class AllocationStorehouseApplyBMOImpl extends ApiBaseBMO implements IAllocationStorehouseApplyBMO {

    @Autowired
    private IAllocationStorehouseApplyInnerServiceSMO allocationStorehouseApplyInnerServiceSMOImpl;

    /**
     * 添加小区信息
     *
     * @param paramInJson     接口调用放传入入参
     * @param dataFlowContext 数据上下文
     * @return 订单服务能够接受的报文
     */
    @Override
    public void addAllocationStorehouseApply(JSONObject paramInJson, DataFlowContext dataFlowContext) {

        paramInJson.put("applyId", "-1");
        AllocationStorehouseApplyPo allocationStorehouseApplyPo = BeanConvertUtil.covertBean(paramInJson, AllocationStorehouseApplyPo.class);
        super.insert(dataFlowContext, allocationStorehouseApplyPo, BusinessTypeConstant.BUSINESS_TYPE_SAVE_ALLOCATION_STOREHOUSE_APPLY);
    }


    /**
     * 添加活动信息
     *
     * @param paramInJson     接口调用放传入入参
     * @param dataFlowContext 数据上下文
     * @return 订单服务能够接受的报文
     */
    @Override
    public void updateAllocationStorehouseApply(JSONObject paramInJson, DataFlowContext dataFlowContext) {
        AllocationStorehouseApplyPo allocationStorehouseApplyPo = BeanConvertUtil.covertBean(paramInJson, AllocationStorehouseApplyPo.class);
        super.update(dataFlowContext, allocationStorehouseApplyPo, BusinessTypeConstant.BUSINESS_TYPE_UPDATE_ALLOCATION_STOREHOUSE_APPLY);
    }



    /**
     * 添加小区信息
     *
     * @param paramInJson     接口调用放传入入参
     * @param dataFlowContext 数据上下文
     * @return 订单服务能够接受的报文
     */
    @Override
    public void deleteAllocationStorehouseApply(JSONObject paramInJson, DataFlowContext dataFlowContext) {

        AllocationStorehouseApplyPo allocationStorehouseApplyPo = BeanConvertUtil.covertBean(paramInJson, AllocationStorehouseApplyPo.class);
        super.update(dataFlowContext, allocationStorehouseApplyPo, BusinessTypeConstant.BUSINESS_TYPE_DELETE_ALLOCATION_STOREHOUSE_APPLY);
    }

}
