package com.newland.property.api.bmo.payFeeDetailDiscount.impl;

import com.alibaba.fastjson.JSONObject;
import com.newland.property.api.bmo.ApiBaseBMO;
import com.newland.property.api.bmo.payFeeDetailDiscount.IPayFeeDetailDiscountBMO;
import com.newland.property.core.context.DataFlowContext;
import com.newland.property.intf.fee.IPayFeeDetailDiscountInnerServiceSMO;
import com.newland.property.po.payFee.PayFeeDetailDiscountPo;
import com.newland.property.utils.constant.BusinessTypeConstant;
import com.newland.property.utils.constant.CommonConstant;
import com.newland.property.utils.util.BeanConvertUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service("payFeeDetailDiscountBMOImpl")
public class PayFeeDetailDiscountBMOImpl extends ApiBaseBMO implements IPayFeeDetailDiscountBMO {

    @Autowired
    private IPayFeeDetailDiscountInnerServiceSMO payFeeDetailDiscountInnerServiceSMOImpl;

    /**
     * 添加小区信息
     *
     * @param paramInJson     接口调用放传入入参
     * @param dataFlowContext 数据上下文
     * @return 订单服务能够接受的报文
     */
    @Override
    public JSONObject addPayFeeDetailDiscount(JSONObject paramInJson, JSONObject discountJson, DataFlowContext dataFlowContext) {
        JSONObject business = JSONObject.parseObject("{\"datas\":{}}");
        business.put(CommonConstant.HTTP_BUSINESS_TYPE_CD, BusinessTypeConstant.BUSINESS_TYPE_SAVE_DETAIL_DISCOUNT_INFO);
        business.put(CommonConstant.HTTP_SEQ, DEFAULT_SEQ + 1);
        business.put(CommonConstant.HTTP_INVOKE_MODEL, CommonConstant.HTTP_INVOKE_MODEL_S);
        JSONObject businessFee = new JSONObject();
        businessFee.put("detailDiscountId", "-1");
        businessFee.put("discountPrice", discountJson.getString("discountPrice"));
        businessFee.put("discountId", discountJson.getString("discountId"));
        businessFee.put("detailId", paramInJson.getString("detailId"));
        businessFee.put("communityId", paramInJson.getString("communityId"));
        businessFee.put("feeId", paramInJson.getString("feeId"));
        //businessFee.putAll(feeMap);
        business.getJSONObject(CommonConstant.HTTP_BUSINESS_DATAS).put(PayFeeDetailDiscountPo.class.getSimpleName(), businessFee);
        return business;

    }

    /**
     * 添加小区信息
     *
     * @param paramInJson     接口调用放传入入参
     * @param dataFlowContext 数据上下文
     * @return 订单服务能够接受的报文
     */
    @Override
    public void addPayFeeDetailDiscountTwo(JSONObject paramInJson, JSONObject discountJson, DataFlowContext dataFlowContext) {
        JSONObject businessFee = new JSONObject();
        businessFee.put("detailDiscountId", "-1");
        businessFee.put("discountPrice", discountJson.getString("discountPrice"));
        businessFee.put("discountId", discountJson.getString("discountId"));
        businessFee.put("detailId", paramInJson.containsKey("newDetailId") ? paramInJson.getString("newDetailId") : paramInJson.getString("detailId"));
        businessFee.put("communityId", paramInJson.getString("communityId"));
        businessFee.put("feeId", paramInJson.getString("feeId"));
        PayFeeDetailDiscountPo payFeeDetailDiscountPo = BeanConvertUtil.covertBean(businessFee, PayFeeDetailDiscountPo.class);
        super.insert(dataFlowContext, payFeeDetailDiscountPo, BusinessTypeConstant.BUSINESS_TYPE_SAVE_DETAIL_DISCOUNT_INFO);
    }


    /**
     * 添加活动信息
     *
     * @param paramInJson     接口调用放传入入参
     * @param dataFlowContext 数据上下文
     * @return 订单服务能够接受的报文
     */
    @Override
    public void updatePayFeeDetailDiscount(JSONObject paramInJson, DataFlowContext dataFlowContext) {
        PayFeeDetailDiscountPo payFeeDetailDiscountPo = BeanConvertUtil.covertBean(paramInJson, PayFeeDetailDiscountPo.class);
        super.update(dataFlowContext, payFeeDetailDiscountPo, BusinessTypeConstant.BUSINESS_TYPE_UPDATE_DETAIL_DISCOUNT_INFO);
    }


    /**
     * 添加小区信息
     *
     * @param paramInJson     接口调用放传入入参
     * @param dataFlowContext 数据上下文
     * @return 订单服务能够接受的报文
     */
    @Override
    public void deletePayFeeDetailDiscount(JSONObject paramInJson, DataFlowContext dataFlowContext) {

        PayFeeDetailDiscountPo payFeeDetailDiscountPo = BeanConvertUtil.covertBean(paramInJson, PayFeeDetailDiscountPo.class);
        super.update(dataFlowContext, payFeeDetailDiscountPo, BusinessTypeConstant.BUSINESS_TYPE_DELETE_DETAIL_DISCOUNT_INFO);
    }

}
