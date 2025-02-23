package com.newland.property.api.bmo.repair.impl;

import com.alibaba.fastjson.JSONObject;
import com.newland.property.api.bmo.ApiBaseBMO;
import com.newland.property.api.bmo.repair.IRepairSettingBMO;
import com.newland.property.core.context.DataFlowContext;
import com.newland.property.core.factory.GenerateCodeFactory;
import com.newland.property.intf.community.IRepairSettingInnerServiceSMO;
import com.newland.property.po.repair.RepairSettingPo;
import com.newland.property.utils.constant.BusinessTypeConstant;
import com.newland.property.utils.util.BeanConvertUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service("repairSettingBMOImpl")
public class RepairSettingBMOImpl extends ApiBaseBMO implements IRepairSettingBMO {

    @Autowired
    private IRepairSettingInnerServiceSMO repairSettingInnerServiceSMOImpl;

    /**
     * 添加小区信息
     *
     * @param paramInJson     接口调用放传入入参
     * @param dataFlowContext 数据上下文
     * @return 订单服务能够接受的报文
     */
    @Override
    public void addRepairSetting(JSONObject paramInJson, DataFlowContext dataFlowContext) {

        paramInJson.put("settingId", GenerateCodeFactory.getGeneratorId(GenerateCodeFactory.CODE_PREFIX_settingId));

        RepairSettingPo repairSettingPo = BeanConvertUtil.covertBean(paramInJson, RepairSettingPo.class);
        repairSettingPo.setRepairType(repairSettingPo.getSettingId());
        super.insert(dataFlowContext, repairSettingPo, BusinessTypeConstant.BUSINESS_TYPE_SAVE_REPAIR_SETTING);
    }


    /**
     * 添加活动信息
     *
     * @param paramInJson     接口调用放传入入参
     * @param dataFlowContext 数据上下文
     * @return 订单服务能够接受的报文
     */
    @Override
    public void updateRepairSetting(JSONObject paramInJson, DataFlowContext dataFlowContext) {
        RepairSettingPo repairSettingPo = BeanConvertUtil.covertBean(paramInJson, RepairSettingPo.class);
        super.update(dataFlowContext, repairSettingPo, BusinessTypeConstant.BUSINESS_TYPE_UPDATE_REPAIR_SETTING);
    }


    /**
     * 添加小区信息
     *
     * @param paramInJson     接口调用放传入入参
     * @param dataFlowContext 数据上下文
     * @return 订单服务能够接受的报文
     */
    @Override
    public void deleteRepairSetting(JSONObject paramInJson, DataFlowContext dataFlowContext) {

        RepairSettingPo repairSettingPo = BeanConvertUtil.covertBean(paramInJson, RepairSettingPo.class);
        super.update(dataFlowContext, repairSettingPo, BusinessTypeConstant.BUSINESS_TYPE_DELETE_REPAIR_SETTING);
    }

}
