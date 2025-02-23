package com.newland.property.api.bmo.purchaseApplyDetail.impl;

import com.alibaba.fastjson.JSONObject;
import com.newland.property.api.bmo.ApiBaseBMO;
import com.newland.property.api.bmo.purchaseApplyDetail.IPurchaseApplyDetailBMO;
import com.newland.property.core.context.DataFlowContext;
import com.newland.property.intf.store.IPurchaseApplyDetailInnerServiceSMO;
import com.newland.property.dto.purchase.PurchaseApplyDetailDto;
import com.newland.property.po.purchase.PurchaseApplyDetailPo;
import com.newland.property.utils.constant.BusinessTypeConstant;
import com.newland.property.utils.util.Assert;
import com.newland.property.utils.util.BeanConvertUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service("purchaseApplyDetailBMOImpl")
public class PurchaseApplyDetailBMOImpl extends ApiBaseBMO implements IPurchaseApplyDetailBMO {

    @Autowired
    private IPurchaseApplyDetailInnerServiceSMO purchaseApplyDetailInnerServiceSMOImpl;

    /**
     * 添加小区信息
     *
     * @param paramInJson     接口调用放传入入参
     * @param dataFlowContext 数据上下文
     * @return 订单服务能够接受的报文
     */
    @Override
    public void addPurchaseApplyDetail(JSONObject paramInJson, DataFlowContext dataFlowContext) {

        JSONObject businessPurchaseApplyDetail = new JSONObject();
        businessPurchaseApplyDetail.putAll(paramInJson);
        businessPurchaseApplyDetail.put("id", "-1");
        PurchaseApplyDetailPo purchaseApplyDetailPo = BeanConvertUtil.covertBean(businessPurchaseApplyDetail, PurchaseApplyDetailPo.class);
        super.insert(dataFlowContext, purchaseApplyDetailPo, BusinessTypeConstant.BUSINESS_TYPE_SAVE_PURCHASE_APPLY_DETAIL);
    }


    /**
     * 添加活动信息
     *
     * @param paramInJson     接口调用放传入入参
     * @param dataFlowContext 数据上下文
     * @return 订单服务能够接受的报文
     */
    @Override
    public void updatePurchaseApplyDetail(JSONObject paramInJson, DataFlowContext dataFlowContext) {

        PurchaseApplyDetailDto purchaseApplyDetailDto = new PurchaseApplyDetailDto();
        //purchaseApplyDetailDto.getApplyOrderId(paramInJson.getString("purchaseApplyDetailId"));
        //purchaseApplyDetailDto.setCommunityId(paramInJson.getString("communityId"));
        List<PurchaseApplyDetailDto> purchaseApplyDetailDtos = purchaseApplyDetailInnerServiceSMOImpl.queryPurchaseApplyDetails(purchaseApplyDetailDto);

        Assert.listOnlyOne(purchaseApplyDetailDtos, "未找到需要修改的活动 或多条数据");


        PurchaseApplyDetailPo purchaseApplyDetailPo = BeanConvertUtil.covertBean(paramInJson, PurchaseApplyDetailPo.class);
        super.update(dataFlowContext, purchaseApplyDetailPo, BusinessTypeConstant.BUSINESS_TYPE_UPDATE_PURCHASE_APPLY_DETAIL);
    }


    /**
     * 添加小区信息
     *
     * @param paramInJson     接口调用放传入入参
     * @param dataFlowContext 数据上下文
     * @return 订单服务能够接受的报文
     */
    @Override
    public void deletePurchaseApplyDetail(JSONObject paramInJson, DataFlowContext dataFlowContext) {

        PurchaseApplyDetailPo purchaseApplyDetailPo = BeanConvertUtil.covertBean(paramInJson, PurchaseApplyDetailPo.class);
        super.delete(dataFlowContext, purchaseApplyDetailPo, BusinessTypeConstant.BUSINESS_TYPE_DELETE_PURCHASE_APPLY_DETAIL);
    }

}
