package com.newland.property.api.bmo.tempCarFeeConfigAttr.impl;

import com.alibaba.fastjson.JSONObject;
import com.newland.property.api.bmo.ApiBaseBMO;
import com.newland.property.api.bmo.tempCarFeeConfigAttr.ITempCarFeeConfigAttrBMO;
import com.newland.property.core.context.DataFlowContext;
import com.newland.property.intf.fee.ITempCarFeeConfigAttrInnerServiceSMO;
import com.newland.property.po.tempCarFee.TempCarFeeConfigAttrPo;
import com.newland.property.utils.constant.BusinessTypeConstant;
import com.newland.property.utils.util.BeanConvertUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service("tempCarFeeConfigAttrBMOImpl")
public class TempCarFeeConfigAttrBMOImpl extends ApiBaseBMO implements ITempCarFeeConfigAttrBMO {

    @Autowired
    private ITempCarFeeConfigAttrInnerServiceSMO tempCarFeeConfigAttrInnerServiceSMOImpl;

    /**
     * 添加小区信息
     *
     * @param paramInJson     接口调用放传入入参
     * @param dataFlowContext 数据上下文
     * @return 订单服务能够接受的报文
     */
    @Override
    public void addTempCarFeeConfigAttr(JSONObject paramInJson, DataFlowContext dataFlowContext) {

        paramInJson.put("attrId", "-1");
        TempCarFeeConfigAttrPo tempCarFeeConfigAttrPo = BeanConvertUtil.covertBean(paramInJson, TempCarFeeConfigAttrPo.class);
        super.insert(dataFlowContext, tempCarFeeConfigAttrPo, BusinessTypeConstant.BUSINESS_TYPE_SAVE_TEMP_CAR_FEE_CONFIG_ATTR_INFO);
    }


    /**
     * 添加活动信息
     *
     * @param paramInJson     接口调用放传入入参
     * @param dataFlowContext 数据上下文
     * @return 订单服务能够接受的报文
     */
    @Override
    public void updateTempCarFeeConfigAttr(JSONObject paramInJson, DataFlowContext dataFlowContext) {
        TempCarFeeConfigAttrPo tempCarFeeConfigAttrPo = BeanConvertUtil.covertBean(paramInJson, TempCarFeeConfigAttrPo.class);
        super.update(dataFlowContext, tempCarFeeConfigAttrPo, BusinessTypeConstant.BUSINESS_TYPE_UPDATE_TEMP_CAR_FEE_CONFIG_ATTR_INFO);
    }


    /**
     * 添加小区信息
     *
     * @param paramInJson     接口调用放传入入参
     * @param dataFlowContext 数据上下文
     * @return 订单服务能够接受的报文
     */
    @Override
    public void deleteTempCarFeeConfigAttr(JSONObject paramInJson, DataFlowContext dataFlowContext) {

        TempCarFeeConfigAttrPo tempCarFeeConfigAttrPo = BeanConvertUtil.covertBean(paramInJson, TempCarFeeConfigAttrPo.class);
        super.update(dataFlowContext, tempCarFeeConfigAttrPo, BusinessTypeConstant.BUSINESS_TYPE_DELETE_TEMP_CAR_FEE_CONFIG_ATTR_INFO);
    }

}
