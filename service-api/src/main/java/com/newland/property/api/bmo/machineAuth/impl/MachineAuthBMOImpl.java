package com.newland.property.api.bmo.machineAuth.impl;

import com.alibaba.fastjson.JSONObject;
import com.newland.property.api.bmo.ApiBaseBMO;
import com.newland.property.api.bmo.machineAuth.IMachineAuthBMO;
import com.newland.property.core.context.DataFlowContext;
import com.newland.property.intf.common.IMachineAuthInnerServiceSMO;
import com.newland.property.po.machine.MachineAuthPo;
import com.newland.property.utils.constant.BusinessTypeConstant;
import com.newland.property.utils.util.BeanConvertUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service("machineAuthBMOImpl")
public class MachineAuthBMOImpl extends ApiBaseBMO implements IMachineAuthBMO {

    @Autowired
    private IMachineAuthInnerServiceSMO machineAuthInnerServiceSMOImpl;

    /**
     * 添加小区信息
     *
     * @param paramInJson     接口调用放传入入参
     * @param dataFlowContext 数据上下文
     * @return 订单服务能够接受的报文
     */
    @Override
    public void addMachineAuth(JSONObject paramInJson, DataFlowContext dataFlowContext) {

        paramInJson.put("authId", "-1");
        MachineAuthPo machineAuthPo = BeanConvertUtil.covertBean(paramInJson, MachineAuthPo.class);
        super.insert(dataFlowContext, machineAuthPo, BusinessTypeConstant.BUSINESS_TYPE_SAVE_MACHINE_AUTH);
    }


    /**
     * 添加活动信息
     *
     * @param paramInJson     接口调用放传入入参
     * @param dataFlowContext 数据上下文
     * @return 订单服务能够接受的报文
     */
    @Override
    public void updateMachineAuth(JSONObject paramInJson, DataFlowContext dataFlowContext) {
        MachineAuthPo machineAuthPo = BeanConvertUtil.covertBean(paramInJson, MachineAuthPo.class);
        super.update(dataFlowContext, machineAuthPo, BusinessTypeConstant.BUSINESS_TYPE_UPDATE_MACHINE_AUTH);
    }


    /**
     * 添加小区信息
     *
     * @param paramInJson     接口调用放传入入参
     * @param dataFlowContext 数据上下文
     * @return 订单服务能够接受的报文
     */
    @Override
    public void deleteMachineAuth(JSONObject paramInJson, DataFlowContext dataFlowContext) {

        MachineAuthPo machineAuthPo = BeanConvertUtil.covertBean(paramInJson, MachineAuthPo.class);
        super.update(dataFlowContext, machineAuthPo, BusinessTypeConstant.BUSINESS_TYPE_DELETE_MACHINE_AUTH);
    }

}
