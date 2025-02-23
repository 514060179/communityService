package com.newland.property.api.bmo.repair.impl;

import com.alibaba.fastjson.JSONObject;
import com.newland.property.api.bmo.ApiBaseBMO;
import com.newland.property.api.bmo.repair.IRepairTypeUserBMO;
import com.newland.property.core.context.DataFlowContext;
import com.newland.property.intf.community.IRepairTypeUserInnerServiceSMO;
import com.newland.property.po.repair.RepairTypeUserPo;
import com.newland.property.utils.constant.BusinessTypeConstant;
import com.newland.property.utils.util.BeanConvertUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service("repairTypeUserBMOImpl")
public class RepairTypeUserBMOImpl extends ApiBaseBMO implements IRepairTypeUserBMO {

    @Autowired
    private IRepairTypeUserInnerServiceSMO repairTypeUserInnerServiceSMOImpl;

    /**
     * 添加小区信息
     *
     * @param paramInJson     接口调用放传入入参
     * @param dataFlowContext 数据上下文
     * @return 订单服务能够接受的报文
     */
    @Override
    public void addRepairTypeUser(JSONObject paramInJson, DataFlowContext dataFlowContext) {

        paramInJson.put("typeUserId", "-1");
        RepairTypeUserPo repairTypeUserPo = BeanConvertUtil.covertBean(paramInJson, RepairTypeUserPo.class);
        super.insert(dataFlowContext, repairTypeUserPo, BusinessTypeConstant.BUSINESS_TYPE_SAVE_REPAIR_TYPE_USER);
    }


    /**
     * 添加活动信息
     *
     * @param paramInJson     接口调用放传入入参
     * @param dataFlowContext 数据上下文
     * @return 订单服务能够接受的报文
     */
    @Override
    public void updateRepairTypeUser(JSONObject paramInJson, DataFlowContext dataFlowContext) {
        RepairTypeUserPo repairTypeUserPo = BeanConvertUtil.covertBean(paramInJson, RepairTypeUserPo.class);
        super.update(dataFlowContext, repairTypeUserPo, BusinessTypeConstant.BUSINESS_TYPE_UPDATE_REPAIR_TYPE_USER);
    }



    /**
     * 添加小区信息
     *
     * @param paramInJson     接口调用放传入入参
     * @param dataFlowContext 数据上下文
     * @return 订单服务能够接受的报文
     */
    @Override
    public void deleteRepairTypeUser(JSONObject paramInJson, DataFlowContext dataFlowContext) {

        RepairTypeUserPo repairTypeUserPo = BeanConvertUtil.covertBean(paramInJson, RepairTypeUserPo.class);
        super.update(dataFlowContext, repairTypeUserPo, BusinessTypeConstant.BUSINESS_TYPE_DELETE_REPAIR_TYPE_USER);
    }

}
