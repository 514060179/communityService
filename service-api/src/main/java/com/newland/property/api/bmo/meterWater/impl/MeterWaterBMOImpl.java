package com.newland.property.api.bmo.meterWater.impl;

import com.alibaba.fastjson.JSONObject;
import com.newland.property.api.bmo.ApiBaseBMO;
import com.newland.property.api.bmo.meterWater.IMeterWaterBMO;
import com.newland.property.core.context.DataFlowContext;
import com.newland.property.intf.fee.IMeterWaterInnerServiceSMO;
import com.newland.property.po.meter.MeterWaterPo;
import com.newland.property.utils.constant.BusinessTypeConstant;
import com.newland.property.utils.util.BeanConvertUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service("meterWaterBMOImpl")
public class MeterWaterBMOImpl extends ApiBaseBMO implements IMeterWaterBMO {

    @Autowired
    private IMeterWaterInnerServiceSMO meterWaterInnerServiceSMOImpl;

    /**
     * 添加小区信息
     *
     * @param paramInJson     接口调用放传入入参
     * @param dataFlowContext 数据上下文
     * @return 订单服务能够接受的报文
     */
    @Override
    public void addMeterWater(JSONObject paramInJson, DataFlowContext dataFlowContext) {
        paramInJson.put("waterId", "-1");
        MeterWaterPo meterWaterPo = BeanConvertUtil.covertBean(paramInJson, MeterWaterPo.class);
        super.insert(dataFlowContext, meterWaterPo, BusinessTypeConstant.BUSINESS_TYPE_SAVE_METER_WATER);
    }


    /**
     * 添加活动信息
     *
     * @param paramInJson     接口调用放传入入参
     * @param dataFlowContext 数据上下文
     * @return 订单服务能够接受的报文
     */
    @Override
    public void updateMeterWater(JSONObject paramInJson, DataFlowContext dataFlowContext) {
        MeterWaterPo meterWaterPo = BeanConvertUtil.covertBean(paramInJson, MeterWaterPo.class);
        super.update(dataFlowContext, meterWaterPo, BusinessTypeConstant.BUSINESS_TYPE_UPDATE_METER_WATER);
    }



    /**
     * 添加小区信息
     *
     * @param paramInJson     接口调用放传入入参
     * @param dataFlowContext 数据上下文
     * @return 订单服务能够接受的报文
     */
    @Override
    public void deleteMeterWater(JSONObject paramInJson, DataFlowContext dataFlowContext) {

        MeterWaterPo meterWaterPo = BeanConvertUtil.covertBean(paramInJson, MeterWaterPo.class);
        super.update(dataFlowContext, meterWaterPo, BusinessTypeConstant.BUSINESS_TYPE_DELETE_METER_WATER);
    }

}
