package com.newland.property.api.bmo.storehouse.impl;

import com.alibaba.fastjson.JSONObject;
import com.newland.property.api.bmo.ApiBaseBMO;
import com.newland.property.api.bmo.storehouse.IStorehouseBMO;
import com.newland.property.core.context.DataFlowContext;
import com.newland.property.intf.store.IStorehouseInnerServiceSMO;
import com.newland.property.po.store.StorehousePo;
import com.newland.property.utils.constant.BusinessTypeConstant;
import com.newland.property.utils.util.BeanConvertUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service("storehouseBMOImpl")
public class StorehouseBMOImpl extends ApiBaseBMO implements IStorehouseBMO {

    @Autowired
    private IStorehouseInnerServiceSMO storehouseInnerServiceSMOImpl;

    /**
     * 添加小区信息
     *
     * @param paramInJson     接口调用放传入入参
     * @param dataFlowContext 数据上下文
     * @return 订单服务能够接受的报文
     */
    @Override
    public void addStorehouse(JSONObject paramInJson, DataFlowContext dataFlowContext) {

        paramInJson.put("shId", "-1");
        StorehousePo storehousePo = BeanConvertUtil.covertBean(paramInJson, StorehousePo.class);

        super.insert(dataFlowContext, storehousePo, BusinessTypeConstant.BUSINESS_TYPE_SAVE_STOREHOUSE);
    }


    /**
     * 添加活动信息
     *
     * @param paramInJson     接口调用放传入入参
     * @param dataFlowContext 数据上下文
     * @return 订单服务能够接受的报文
     */
    @Override
    public void updateStorehouse(JSONObject paramInJson, DataFlowContext dataFlowContext) {
        StorehousePo storehousePo = BeanConvertUtil.covertBean(paramInJson, StorehousePo.class);
        super.update(dataFlowContext, storehousePo, BusinessTypeConstant.BUSINESS_TYPE_UPDATE_STOREHOUSE);
    }


    /**
     * 添加小区信息
     *
     * @param paramInJson     接口调用放传入入参
     * @param dataFlowContext 数据上下文
     * @return 订单服务能够接受的报文
     */
    @Override
    public void deleteStorehouse(JSONObject paramInJson, DataFlowContext dataFlowContext) {

        StorehousePo storehousePo = BeanConvertUtil.covertBean(paramInJson, StorehousePo.class);
        super.update(dataFlowContext, storehousePo, BusinessTypeConstant.BUSINESS_TYPE_DELETE_STOREHOUSE);
    }

}
