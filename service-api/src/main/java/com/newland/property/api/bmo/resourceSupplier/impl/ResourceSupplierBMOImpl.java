package com.newland.property.api.bmo.resourceSupplier.impl;

import com.alibaba.fastjson.JSONObject;
import com.newland.property.api.bmo.ApiBaseBMO;
import com.newland.property.api.bmo.resourceSupplier.IResourceSupplierBMO;
import com.newland.property.core.context.DataFlowContext;
import com.newland.property.dto.purchase.PurchaseApplyDetailDto;
import com.newland.property.intf.store.IPurchaseApplyDetailInnerServiceSMO;
import com.newland.property.intf.store.IResourceSupplierInnerServiceSMO;
import com.newland.property.po.resource.ResourceSupplierPo;
import com.newland.property.utils.constant.BusinessTypeConstant;
import com.newland.property.utils.util.Assert;
import com.newland.property.utils.util.BeanConvertUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service("resourceSupplierBMOImpl")
public class ResourceSupplierBMOImpl extends ApiBaseBMO implements IResourceSupplierBMO {

    @Autowired
    private IResourceSupplierInnerServiceSMO resourceSupplierInnerServiceSMOImpl;

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
    public void addResourceSupplier(JSONObject paramInJson, DataFlowContext dataFlowContext) {
        paramInJson.put("rsId", "-1");
        paramInJson.put("createUserId", paramInJson.getString("userId"));
        paramInJson.put("createUserName", paramInJson.getString("userName"));
        ResourceSupplierPo resourceSupplierPo = BeanConvertUtil.covertBean(paramInJson, ResourceSupplierPo.class);
        super.insert(dataFlowContext, resourceSupplierPo, BusinessTypeConstant.BUSINESS_TYPE_SAVE_RESOURCE_SUPPLIER);
    }

    /**
     * 添加活动信息
     *
     * @param paramInJson     接口调用放传入入参
     * @param dataFlowContext 数据上下文
     * @return 订单服务能够接受的报文
     */
    @Override
    public void updateResourceSupplier(JSONObject paramInJson, DataFlowContext dataFlowContext) {
        ResourceSupplierPo resourceSupplierPo = BeanConvertUtil.covertBean(paramInJson, ResourceSupplierPo.class);
        super.update(dataFlowContext, resourceSupplierPo, BusinessTypeConstant.BUSINESS_TYPE_UPDATE_RESOURCE_SUPPLIER);
    }

    /**
     * 添加小区信息
     *
     * @param paramInJson     接口调用放传入入参
     * @param dataFlowContext 数据上下文
     * @return 订单服务能够接受的报文
     */
    @Override
    public void deleteResourceSupplier(JSONObject paramInJson, DataFlowContext dataFlowContext) {
        PurchaseApplyDetailDto purchaseApplyDetailDto = new PurchaseApplyDetailDto();
        purchaseApplyDetailDto.setRsId(paramInJson.getString("rsId"));
        List<PurchaseApplyDetailDto> purchaseApplyDetailDtos = purchaseApplyDetailInnerServiceSMOImpl.queryPurchaseApplyDetails(purchaseApplyDetailDto);
        Assert.listIsNull(purchaseApplyDetailDtos, "该供应商存在物品，不能直接删除！");
        ResourceSupplierPo resourceSupplierPo = BeanConvertUtil.covertBean(paramInJson, ResourceSupplierPo.class);
        super.update(dataFlowContext, resourceSupplierPo, BusinessTypeConstant.BUSINESS_TYPE_DELETE_RESOURCE_SUPPLIER);
    }
}
