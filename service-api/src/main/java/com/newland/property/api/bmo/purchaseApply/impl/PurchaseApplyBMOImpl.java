package com.newland.property.api.bmo.purchaseApply.impl;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.newland.property.api.bmo.ApiBaseBMO;
import com.newland.property.api.bmo.purchaseApply.IPurchaseApplyBMO;
import com.newland.property.core.context.DataFlowContext;
import com.newland.property.po.purchase.PurchaseApplyPo;
import com.newland.property.utils.constant.BusinessTypeConstant;
import com.newland.property.utils.util.BeanConvertUtil;
import org.springframework.stereotype.Service;

/**
 * @ClassName PurchaseApplyBMOImpl
 * @Description TODO
 * @Author wuxw
 * @Date 2020/3/9 23:34
 * @Version 1.0
 * add by wuxw 2020/3/9
 **/
@Service("purchaseApplyBMOImpl")
public class PurchaseApplyBMOImpl extends ApiBaseBMO implements IPurchaseApplyBMO {

    /**
     * 添加小区信息
     *
     * @param paramInJson     接口调用放传入入参
     * @param dataFlowContext 数据上下文
     * @return 订单服务能够接受的报文
     */
    @Override
    public void deletePurchaseApply(JSONObject paramInJson, DataFlowContext dataFlowContext) {


        PurchaseApplyPo purchaseApplyPo = BeanConvertUtil.covertBean(paramInJson, PurchaseApplyPo.class);
        super.delete(dataFlowContext, purchaseApplyPo, BusinessTypeConstant.BUSINESS_TYPE_DELETE_PURCHASE_APPLY);
    }

    /**
     * 添加采购申请信息
     *
     * @param paramInJson     接口调用放传入入参
     * @param dataFlowContext 数据上下文
     * @return 订单服务能够接受的报文
     */
    @Override
    public void updatePurchaseApply(JSONObject paramInJson, DataFlowContext dataFlowContext) {

        PurchaseApplyPo purchaseApplyPo = BeanConvertUtil.covertBean(paramInJson, PurchaseApplyPo.class);
        super.update(dataFlowContext, purchaseApplyPo, BusinessTypeConstant.BUSINESS_TYPE_UPDATE_PURCHASE_APPLY);
    }

    /**
     * 添加小区信息
     *
     * @param paramInJson     接口调用放传入入参
     * @param dataFlowContext 数据上下文
     * @return 订单服务能够接受的报文
     */
    @Override
    public void addPurchaseApply(JSONObject paramInJson, DataFlowContext dataFlowContext) {

        JSONArray resourceStores = paramInJson.getJSONArray("resourceStores");

        PurchaseApplyPo purchaseApplyPo = BeanConvertUtil.covertBean(paramInJson, PurchaseApplyPo.class);
        super.insert(dataFlowContext, purchaseApplyPo, BusinessTypeConstant.BUSINESS_TYPE_SAVE_PURCHASE_APPLY);
    }

}
