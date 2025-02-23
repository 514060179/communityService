package com.newland.property.api.bmo.parkingArea.impl;

import com.alibaba.fastjson.JSONObject;
import com.newland.property.api.bmo.ApiBaseBMO;
import com.newland.property.api.bmo.parkingArea.IParkingAreaBMO;
import com.newland.property.core.context.DataFlowContext;
import com.newland.property.core.factory.GenerateCodeFactory;
import com.newland.property.po.parking.ParkingAreaPo;
import com.newland.property.utils.constant.BusinessTypeConstant;
import com.newland.property.utils.util.BeanConvertUtil;
import org.springframework.stereotype.Service;

/**
 * @ClassName ParkingAreaBMOImpl
 * @Description TODO
 * @Author wuxw
 * @Date 2020/3/9 23:24
 * @Version 1.0
 * add by wuxw 2020/3/9
 **/
@Service("parkingAreaBMOImpl")
public class ParkingAreaBMOImpl extends ApiBaseBMO implements IParkingAreaBMO {

    /**
     * 添加小区信息
     *
     * @param paramInJson     接口调用放传入入参
     * @param dataFlowContext 数据上下文
     * @return 订单服务能够接受的报文
     */
    @Override
    public void deleteParkingArea(JSONObject paramInJson, DataFlowContext dataFlowContext) {

        ParkingAreaPo parkingAreaPo = BeanConvertUtil.covertBean(paramInJson, ParkingAreaPo.class);
        super.delete(dataFlowContext, parkingAreaPo, BusinessTypeConstant.BUSINESS_TYPE_DELETE_PARKING_AREA);
    }

    /**
     * 添加小区信息
     *
     * @param paramInJson     接口调用放传入入参
     * @param dataFlowContext 数据上下文
     * @return 订单服务能够接受的报文
     */
    @Override
    public void addParkingArea(JSONObject paramInJson, DataFlowContext dataFlowContext) {

        JSONObject businessParkingArea = new JSONObject();
        businessParkingArea.putAll(paramInJson);
        businessParkingArea.put("paId", GenerateCodeFactory.getGeneratorId(GenerateCodeFactory.CODE_PREFIX_paId));
        ParkingAreaPo parkingAreaPo = BeanConvertUtil.covertBean(businessParkingArea, ParkingAreaPo.class);
        super.insert(dataFlowContext, parkingAreaPo, BusinessTypeConstant.BUSINESS_TYPE_SAVE_PARKING_AREA);
        paramInJson.put("paId", businessParkingArea.getString("paId"));
    }

    /**
     * 添加停车场信息
     *
     * @param paramInJson     接口调用放传入入参
     * @param dataFlowContext 数据上下文
     * @return 订单服务能够接受的报文
     */
    @Override
    public void updateParkingArea(JSONObject paramInJson, DataFlowContext dataFlowContext) {

        JSONObject businessParkingArea = new JSONObject();
        businessParkingArea.putAll(paramInJson);
        ParkingAreaPo parkingAreaPo = BeanConvertUtil.covertBean(businessParkingArea, ParkingAreaPo.class);
        super.update(dataFlowContext, parkingAreaPo, BusinessTypeConstant.BUSINESS_TYPE_UPDATE_PARKING_AREA);
    }
}
