package com.newland.property.api.bmo.applyRoomDiscountRecord.impl;

import com.alibaba.fastjson.JSONObject;
import com.newland.property.api.bmo.ApiBaseBMO;
import com.newland.property.api.bmo.applyRoomDiscountRecord.IApplyRoomDiscountRecordBMO;
import com.newland.property.core.context.DataFlowContext;
import com.newland.property.intf.community.IApplyRoomDiscountRecordInnerServiceSMO;
import com.newland.property.po.room.ApplyRoomDiscountRecordPo;
import com.newland.property.utils.constant.BusinessTypeConstant;
import com.newland.property.utils.util.BeanConvertUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


@Service("applyRoomDiscountRecordBMOImpl")
public class ApplyRoomDiscountRecordBMOImpl extends ApiBaseBMO implements IApplyRoomDiscountRecordBMO {

    @Autowired
    private IApplyRoomDiscountRecordInnerServiceSMO applyRoomDiscountRecordInnerServiceSMOImpl;

    /**
     * 添加小区信息
     *
     * @param paramInJson     接口调用放传入入参
     * @param dataFlowContext 数据上下文
     * @return 订单服务能够接受的报文
     */
    @Override
    public void addApplyRoomDiscountRecord(JSONObject paramInJson, DataFlowContext dataFlowContext) {

        paramInJson.put("ardrId", "-1");
        ApplyRoomDiscountRecordPo applyRoomDiscountRecordPo = BeanConvertUtil.covertBean(paramInJson, ApplyRoomDiscountRecordPo.class);
        super.insert(dataFlowContext, applyRoomDiscountRecordPo, BusinessTypeConstant.BUSINESS_TYPE_SAVE_APPLY_ROOM_DISCOUNT_RECORD);
    }


    /**
     * 添加活动信息
     *
     * @param paramInJson     接口调用放传入入参
     * @param dataFlowContext 数据上下文
     * @return 订单服务能够接受的报文
     */
    @Override
    public void updateApplyRoomDiscountRecord(JSONObject paramInJson, DataFlowContext dataFlowContext) {
        ApplyRoomDiscountRecordPo applyRoomDiscountRecordPo = BeanConvertUtil.covertBean(paramInJson, ApplyRoomDiscountRecordPo.class);
        super.update(dataFlowContext, applyRoomDiscountRecordPo, BusinessTypeConstant.BUSINESS_TYPE_UPDATE_APPLY_ROOM_DISCOUNT_RECORD);
    }



    /**
     * 添加小区信息
     *
     * @param paramInJson     接口调用放传入入参
     * @param dataFlowContext 数据上下文
     * @return 订单服务能够接受的报文
     */
    @Override
    public void deleteApplyRoomDiscountRecord(JSONObject paramInJson, DataFlowContext dataFlowContext) {

        ApplyRoomDiscountRecordPo applyRoomDiscountRecordPo = BeanConvertUtil.covertBean(paramInJson, ApplyRoomDiscountRecordPo.class);
        super.update(dataFlowContext, applyRoomDiscountRecordPo, BusinessTypeConstant.BUSINESS_TYPE_DELETE_APPLY_ROOM_DISCOUNT_RECORD);
    }

}
