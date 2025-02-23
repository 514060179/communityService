package com.newland.property.api.bmo.communityLocationAttr.impl;

import com.alibaba.fastjson.JSONObject;
import com.newland.property.api.bmo.ApiBaseBMO;
import com.newland.property.api.bmo.communityLocationAttr.ICommunityLocationAttrBMO;
import com.newland.property.core.context.DataFlowContext;
import com.newland.property.intf.community.ICommunityLocationAttrInnerServiceSMO;
import com.newland.property.po.community.CommunityLocationAttrPo;
import com.newland.property.utils.constant.BusinessTypeConstant;
import com.newland.property.utils.util.BeanConvertUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service("communityLocationAttrBMOImpl")
public class CommunityLocationAttrBMOImpl extends ApiBaseBMO implements ICommunityLocationAttrBMO {

    @Autowired
    private ICommunityLocationAttrInnerServiceSMO communityLocationAttrInnerServiceSMOImpl;

    /**
     * 添加小区信息
     *
     * @param paramInJson     接口调用放传入入参
     * @param dataFlowContext 数据上下文
     * @return 订单服务能够接受的报文
     */
    @Override
    public void addCommunityLocationAttr(JSONObject paramInJson, DataFlowContext dataFlowContext) {

        paramInJson.put("attrId", "-1");
        CommunityLocationAttrPo communityLocationAttrPo = BeanConvertUtil.covertBean(paramInJson, CommunityLocationAttrPo.class);
        super.insert(dataFlowContext, communityLocationAttrPo, BusinessTypeConstant.BUSINESS_TYPE_SAVE_LOCATION_ATTR);
    }


    /**
     * 添加活动信息
     *
     * @param paramInJson     接口调用放传入入参
     * @param dataFlowContext 数据上下文
     * @return 订单服务能够接受的报文
     */
    @Override
    public void updateCommunityLocationAttr(JSONObject paramInJson, DataFlowContext dataFlowContext) {
        CommunityLocationAttrPo communityLocationAttrPo = BeanConvertUtil.covertBean(paramInJson, CommunityLocationAttrPo.class);
        super.update(dataFlowContext, communityLocationAttrPo, BusinessTypeConstant.BUSINESS_TYPE_UPDATE_LOCATION_ATTR);
    }


    /**
     * 添加小区信息
     *
     * @param paramInJson     接口调用放传入入参
     * @param dataFlowContext 数据上下文
     * @return 订单服务能够接受的报文
     */
    @Override
    public void deleteCommunityLocationAttr(JSONObject paramInJson, DataFlowContext dataFlowContext) {

        CommunityLocationAttrPo communityLocationAttrPo = BeanConvertUtil.covertBean(paramInJson, CommunityLocationAttrPo.class);
        super.update(dataFlowContext, communityLocationAttrPo, BusinessTypeConstant.BUSINESS_TYPE_DELETE_LOCATION_ATTR);
    }

}
