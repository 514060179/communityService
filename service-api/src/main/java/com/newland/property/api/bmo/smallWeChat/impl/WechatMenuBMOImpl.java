package com.newland.property.api.bmo.smallWeChat.impl;

import com.alibaba.fastjson.JSONObject;
import com.newland.property.api.bmo.ApiBaseBMO;
import com.newland.property.api.bmo.smallWeChat.IWechatMenuBMO;
import com.newland.property.core.context.DataFlowContext;
import com.newland.property.intf.store.IWechatMenuInnerServiceSMO;
import com.newland.property.po.wechat.WechatMenuPo;
import com.newland.property.utils.constant.BusinessTypeConstant;
import com.newland.property.utils.util.BeanConvertUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service("wechatMenuBMOImpl")
public class WechatMenuBMOImpl extends ApiBaseBMO implements IWechatMenuBMO {

    @Autowired
    private IWechatMenuInnerServiceSMO wechatMenuInnerServiceSMOImpl;

    /**
     * 添加小区信息
     *
     * @param paramInJson     接口调用放传入入参
     * @param dataFlowContext 数据上下文
     * @return 订单服务能够接受的报文
     */
    @Override
    public void addWechatMenu(JSONObject paramInJson, DataFlowContext dataFlowContext) {

        paramInJson.put("wechatMenuId", "-1");
        WechatMenuPo wechatMenuPo = BeanConvertUtil.covertBean(paramInJson, WechatMenuPo.class);
        super.insert(dataFlowContext, wechatMenuPo, BusinessTypeConstant.BUSINESS_TYPE_SAVE_WECHAT_MENU);
    }


    /**
     * 添加活动信息
     *
     * @param paramInJson     接口调用放传入入参
     * @param dataFlowContext 数据上下文
     * @return 订单服务能够接受的报文
     */
    @Override
    public void updateWechatMenu(JSONObject paramInJson, DataFlowContext dataFlowContext) {
        WechatMenuPo wechatMenuPo = BeanConvertUtil.covertBean(paramInJson, WechatMenuPo.class);
        super.update(dataFlowContext, wechatMenuPo, BusinessTypeConstant.BUSINESS_TYPE_UPDATE_WECHAT_MENU);
    }


    /**
     * 添加小区信息
     *
     * @param paramInJson     接口调用放传入入参
     * @param dataFlowContext 数据上下文
     * @return 订单服务能够接受的报文
     */
    @Override
    public void deleteWechatMenu(JSONObject paramInJson, DataFlowContext dataFlowContext) {

        WechatMenuPo wechatMenuPo = BeanConvertUtil.covertBean(paramInJson, WechatMenuPo.class);
        super.update(dataFlowContext, wechatMenuPo, BusinessTypeConstant.BUSINESS_TYPE_DELETE_WECHAT_MENU);
    }

}
