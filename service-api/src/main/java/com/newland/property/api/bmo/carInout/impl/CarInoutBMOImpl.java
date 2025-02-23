package com.newland.property.api.bmo.carInout.impl;

import com.alibaba.fastjson.JSONObject;
import com.newland.property.api.bmo.ApiBaseBMO;
import com.newland.property.api.bmo.carInout.ICarInoutBMO;
import com.newland.property.core.context.DataFlowContext;
import com.newland.property.po.car.CarInoutPo;
import com.newland.property.utils.constant.BusinessTypeConstant;
import com.newland.property.utils.util.BeanConvertUtil;
import org.springframework.stereotype.Service;

/**
 * @ClassName CarInoutBMOImpl
 * @Description TODO
 * @Author wuxw
 * @Date 2020/3/9 21:17
 * @Version 1.0
 * add by wuxw 2020/3/9
 **/
@Service("carInoutBMOImpl")
public class CarInoutBMOImpl extends ApiBaseBMO implements ICarInoutBMO {
    /**
     * 添加小区信息
     *
     * @param paramInJson     接口调用放传入入参
     * @param dataFlowContext 数据上下文
     * @return 订单服务能够接受的报文
     */
    @Override
    public void addCarInout(JSONObject paramInJson, DataFlowContext dataFlowContext) {

        paramInJson.put("inoutId", "-1");
        CarInoutPo carInoutPo = BeanConvertUtil.covertBean(paramInJson, CarInoutPo.class);

        super.insert(dataFlowContext, carInoutPo, BusinessTypeConstant.BUSINESS_TYPE_SAVE_CAR_INOUT);

    }

    /**
     * 添加车辆进场信息
     *
     * @param paramInJson     接口调用放传入入参
     * @param dataFlowContext 数据上下文
     * @return 订单服务能够接受的报文
     */
    @Override
    public void updateCarInout(JSONObject paramInJson, DataFlowContext dataFlowContext) {


        CarInoutPo carInoutPo = BeanConvertUtil.covertBean(paramInJson, CarInoutPo.class);

        super.update(dataFlowContext, carInoutPo, BusinessTypeConstant.BUSINESS_TYPE_UPDATE_CAR_INOUT);
    }

}
