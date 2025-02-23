package com.newland.property.api.bmo.userStorehouse.impl;

import com.alibaba.fastjson.JSONObject;
import com.newland.property.api.bmo.ApiBaseBMO;
import com.newland.property.api.bmo.userStorehouse.IUserStorehouseBMO;
import com.newland.property.core.context.DataFlowContext;
import com.newland.property.intf.store.IUserStorehouseInnerServiceSMO;
import com.newland.property.po.user.UserStorehousePo;
import com.newland.property.utils.constant.BusinessTypeConstant;
import com.newland.property.utils.util.BeanConvertUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service("userStorehouseBMOImpl")
public class UserStorehouseBMOImpl extends ApiBaseBMO implements IUserStorehouseBMO {

    @Autowired
    private IUserStorehouseInnerServiceSMO userStorehouseInnerServiceSMOImpl;

    /**
     * 添加小区信息
     *
     * @param paramInJson     接口调用放传入入参
     * @param dataFlowContext 数据上下文
     * @return 订单服务能够接受的报文
     */
    @Override
    public void addUserStorehouse(JSONObject paramInJson, DataFlowContext dataFlowContext) {

        paramInJson.put("usId", "-1");
        UserStorehousePo userStorehousePo = BeanConvertUtil.covertBean(paramInJson, UserStorehousePo.class);
        super.insert(dataFlowContext, userStorehousePo, BusinessTypeConstant.BUSINESS_TYPE_SAVE_USER_STOREHOUSE);
    }


    /**
     * 添加活动信息
     *
     * @param paramInJson     接口调用放传入入参
     * @param dataFlowContext 数据上下文
     * @return 订单服务能够接受的报文
     */
    @Override
    public void updateUserStorehouse(JSONObject paramInJson, DataFlowContext dataFlowContext) {
        UserStorehousePo userStorehousePo = BeanConvertUtil.covertBean(paramInJson, UserStorehousePo.class);
        super.update(dataFlowContext, userStorehousePo, BusinessTypeConstant.BUSINESS_TYPE_UPDATE_USER_STOREHOUSE);
    }


    /**
     * 添加小区信息
     *
     * @param paramInJson     接口调用放传入入参
     * @param dataFlowContext 数据上下文
     * @return 订单服务能够接受的报文
     */
    @Override
    public void deleteUserStorehouse(JSONObject paramInJson, DataFlowContext dataFlowContext) {

        UserStorehousePo userStorehousePo = BeanConvertUtil.covertBean(paramInJson, UserStorehousePo.class);
        super.update(dataFlowContext, userStorehousePo, BusinessTypeConstant.BUSINESS_TYPE_DELETE_USER_STOREHOUSE);
    }

}
