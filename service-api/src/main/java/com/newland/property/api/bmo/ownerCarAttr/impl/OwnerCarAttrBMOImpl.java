package com.newland.property.api.bmo.ownerCarAttr.impl;

import com.alibaba.fastjson.JSONObject;
import com.newland.property.api.bmo.ApiBaseBMO;
import com.newland.property.api.bmo.ownerCarAttr.IOwnerCarAttrBMO;
import com.newland.property.core.context.DataFlowContext;
import com.newland.property.intf.user.IOwnerCarAttrInnerServiceSMO;
import com.newland.property.po.owner.OwnerCarAttrPo;
import com.newland.property.utils.constant.BusinessTypeConstant;
import com.newland.property.utils.util.BeanConvertUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service("ownerCarAttrBMOImpl")
public class OwnerCarAttrBMOImpl extends ApiBaseBMO implements IOwnerCarAttrBMO {

    @Autowired
    private IOwnerCarAttrInnerServiceSMO ownerCarAttrInnerServiceSMOImpl;

    /**
     * 添加小区信息
     *
     * @param paramInJson     接口调用放传入入参
     * @param dataFlowContext 数据上下文
     * @return 订单服务能够接受的报文
     */
    @Override
    public void addOwnerCarAttr(JSONObject paramInJson, DataFlowContext dataFlowContext) {

        paramInJson.put("attrId", "-1");
        OwnerCarAttrPo ownerCarAttrPo = BeanConvertUtil.covertBean(paramInJson, OwnerCarAttrPo.class);
        super.insert(dataFlowContext, ownerCarAttrPo, BusinessTypeConstant.BUSINESS_TYPE_SAVE_OWNER_CAR_ATTR);
    }


    /**
     * 添加活动信息
     *
     * @param paramInJson     接口调用放传入入参
     * @param dataFlowContext 数据上下文
     * @return 订单服务能够接受的报文
     */
    @Override
    public void updateOwnerCarAttr(JSONObject paramInJson, DataFlowContext dataFlowContext) {
        OwnerCarAttrPo ownerCarAttrPo = BeanConvertUtil.covertBean(paramInJson, OwnerCarAttrPo.class);
        super.update(dataFlowContext, ownerCarAttrPo, BusinessTypeConstant.BUSINESS_TYPE_UPDATE_OWNER_CAR_ATTR);
    }


    /**
     * 添加小区信息
     *
     * @param paramInJson     接口调用放传入入参
     * @param dataFlowContext 数据上下文
     * @return 订单服务能够接受的报文
     */
    @Override
    public void deleteOwnerCarAttr(JSONObject paramInJson, DataFlowContext dataFlowContext) {

        OwnerCarAttrPo ownerCarAttrPo = BeanConvertUtil.covertBean(paramInJson, OwnerCarAttrPo.class);
        super.update(dataFlowContext, ownerCarAttrPo, BusinessTypeConstant.BUSINESS_TYPE_DELETE_OWNER_CAR_ATTR);
    }

}
