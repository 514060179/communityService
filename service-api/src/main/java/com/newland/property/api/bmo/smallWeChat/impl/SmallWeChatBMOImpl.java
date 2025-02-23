package com.newland.property.api.bmo.smallWeChat.impl;

import com.alibaba.fastjson.JSONObject;
import com.newland.property.api.bmo.ApiBaseBMO;
import com.newland.property.api.bmo.smallWeChat.ISmallWeChatBMO;
import com.newland.property.core.context.DataFlowContext;
import com.newland.property.intf.store.ISmallWeChatInnerServiceSMO;
import com.newland.property.dto.wechat.SmallWeChatDto;
import com.newland.property.po.store.SmallWechatPo;
import com.newland.property.utils.constant.BusinessTypeConstant;
import com.newland.property.utils.util.Assert;
import com.newland.property.utils.util.BeanConvertUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service("smallWeChatBMOImpl")
public class SmallWeChatBMOImpl extends ApiBaseBMO implements ISmallWeChatBMO {

    @Autowired
    private ISmallWeChatInnerServiceSMO smallWeChatInnerServiceSMOImpl;

    /**
     * 添加小区信息
     *
     * @param paramInJson     接口调用放传入入参
     * @param dataFlowContext 数据上下文
     * @return 订单服务能够接受的报文
     */
    @Override
    public void addSmallWeChat(JSONObject paramInJson, DataFlowContext dataFlowContext) {

        JSONObject businessSmallWeChat = new JSONObject();
        businessSmallWeChat.putAll(paramInJson);
        //businessSmallWeChat.put("weChatId", "-1");
        SmallWechatPo smallWechatPo = BeanConvertUtil.covertBean(businessSmallWeChat, SmallWechatPo.class);
        super.insert(dataFlowContext, smallWechatPo, BusinessTypeConstant.BUSINESS_TYPE_SAVE_SMALL_WE_CHAT);
    }


    /**
     * 添加活动信息
     *
     * @param paramInJson     接口调用放传入入参
     * @param dataFlowContext 数据上下文
     * @return 订单服务能够接受的报文
     */
    @Override
    public void updateSmallWeChat(JSONObject paramInJson, DataFlowContext dataFlowContext) {

        SmallWeChatDto smallWeChatDto = new SmallWeChatDto();
        smallWeChatDto.setWeChatId(paramInJson.getString("weChatId"));
        smallWeChatDto.setStoreId(paramInJson.getString("soreId"));
        List<SmallWeChatDto> smallWeChatDtos = smallWeChatInnerServiceSMOImpl.querySmallWeChats(smallWeChatDto);
        Assert.listOnlyOne(smallWeChatDtos, "未找到需要修改的小程序信息 或多条数据");

        JSONObject businessSmallWeChat = new JSONObject();
        businessSmallWeChat.putAll(BeanConvertUtil.beanCovertMap(smallWeChatDtos.get(0)));
        businessSmallWeChat.putAll(paramInJson);
        SmallWechatPo smallWechatPo = BeanConvertUtil.covertBean(businessSmallWeChat, SmallWechatPo.class);
        super.update(dataFlowContext, smallWechatPo, BusinessTypeConstant.BUSINESS_TYPE_UPDATE_SMALL_WE_CHAT);
    }


    /**
     * 添加小区信息
     *
     * @param paramInJson     接口调用放传入入参
     * @param dataFlowContext 数据上下文
     * @return 订单服务能够接受的报文
     */
    @Override
    public void deleteSmallWeChat(JSONObject paramInJson, DataFlowContext dataFlowContext) {
        SmallWechatPo smallWechatPo = BeanConvertUtil.covertBean(paramInJson, SmallWechatPo.class);
        super.delete(dataFlowContext, smallWechatPo, BusinessTypeConstant.BUSINESS_TYPE_DELETE_SMALL_WE_CHAT);
    }

}
