package com.newland.property.api.bmo.smallWeChat.impl;

import com.alibaba.fastjson.JSONObject;
import com.newland.property.api.bmo.ApiBaseBMO;
import com.newland.property.api.bmo.smallWeChat.ISmallWechatAttrBMO;
import com.newland.property.core.context.DataFlowContext;
import com.newland.property.intf.store.ISmallWechatAttrInnerServiceSMO;
import com.newland.property.po.wechat.SmallWechatAttrPo;
import com.newland.property.utils.constant.BusinessTypeConstant;
import com.newland.property.utils.util.BeanConvertUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service("smallWechatAttrBMOImpl")
public class SmallWechatAttrBMOImpl extends ApiBaseBMO implements ISmallWechatAttrBMO {

    @Autowired
    private ISmallWechatAttrInnerServiceSMO smallWechatAttrInnerServiceSMOImpl;

    /**
     * 添加小区信息
     *
     * @param paramInJson     接口调用放传入入参
     * @param dataFlowContext 数据上下文
     * @return 订单服务能够接受的报文
     */
    @Override
    public void addSmallWechatAttr(JSONObject paramInJson, DataFlowContext dataFlowContext) {

        paramInJson.put("attrId", "-1");
        SmallWechatAttrPo smallWechatAttrPo = BeanConvertUtil.covertBean(paramInJson, SmallWechatAttrPo.class);
        super.insert(dataFlowContext, smallWechatAttrPo, BusinessTypeConstant.BUSINESS_TYPE_SAVE_SMALL_WECHAT_ATTR);
    }


    /**
     * 添加活动信息
     *
     * @param paramInJson     接口调用放传入入参
     * @param dataFlowContext 数据上下文
     * @return 订单服务能够接受的报文
     */
    @Override
    public void updateSmallWechatAttr(JSONObject paramInJson, DataFlowContext dataFlowContext) {
        SmallWechatAttrPo smallWechatAttrPo = BeanConvertUtil.covertBean(paramInJson, SmallWechatAttrPo.class);
        super.update(dataFlowContext, smallWechatAttrPo, BusinessTypeConstant.BUSINESS_TYPE_UPDATE_SMALL_WECHAT_ATTR);
    }



    /**
     * 添加小区信息
     *
     * @param paramInJson     接口调用放传入入参
     * @param dataFlowContext 数据上下文
     * @return 订单服务能够接受的报文
     */
    @Override
    public void deleteSmallWechatAttr(JSONObject paramInJson, DataFlowContext dataFlowContext) {

        SmallWechatAttrPo smallWechatAttrPo = BeanConvertUtil.covertBean(paramInJson, SmallWechatAttrPo.class);
        super.update(dataFlowContext, smallWechatAttrPo, BusinessTypeConstant.BUSINESS_TYPE_DELETE_SMALL_WECHAT_ATTR);
    }

}
