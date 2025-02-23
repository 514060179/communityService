package com.newland.property.api.bmo.parkingAreaAttr.impl;

import com.alibaba.fastjson.JSONObject;
import com.newland.property.api.bmo.ApiBaseBMO;
import com.newland.property.api.bmo.parkingAreaAttr.IParkingAreaAttrBMO;
import com.newland.property.core.context.DataFlowContext;
import com.newland.property.intf.community.IParkingAreaAttrInnerServiceSMO;
import com.newland.property.po.parking.ParkingAreaAttrPo;
import com.newland.property.utils.constant.BusinessTypeConstant;
import com.newland.property.utils.util.BeanConvertUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service("parkingAreaAttrBMOImpl")
public class ParkingAreaAttrBMOImpl extends ApiBaseBMO implements IParkingAreaAttrBMO {

    @Autowired
    private IParkingAreaAttrInnerServiceSMO parkingAreaAttrInnerServiceSMOImpl;

    /**
     * 添加小区信息
     *
     * @param paramInJson     接口调用放传入入参
     * @param dataFlowContext 数据上下文
     * @return 订单服务能够接受的报文
     */
    @Override
    public void addParkingAreaAttr(JSONObject paramInJson, DataFlowContext dataFlowContext) {

        paramInJson.put("attrId", "-1");
        ParkingAreaAttrPo parkingAreaAttrPo = BeanConvertUtil.covertBean(paramInJson, ParkingAreaAttrPo.class);
        super.insert(dataFlowContext, parkingAreaAttrPo, BusinessTypeConstant.BUSINESS_TYPE_SAVE_PARKING_AREA_ATTR);
    }


    /**
     * 添加活动信息
     *
     * @param paramInJson     接口调用放传入入参
     * @param dataFlowContext 数据上下文
     * @return 订单服务能够接受的报文
     */
    @Override
    public void updateParkingAreaAttr(JSONObject paramInJson, DataFlowContext dataFlowContext) {
        ParkingAreaAttrPo parkingAreaAttrPo = BeanConvertUtil.covertBean(paramInJson, ParkingAreaAttrPo.class);
        super.update(dataFlowContext, parkingAreaAttrPo, BusinessTypeConstant.BUSINESS_TYPE_UPDATE_PARKING_AREA_ATTR);
    }


    /**
     * 添加小区信息
     *
     * @param paramInJson     接口调用放传入入参
     * @param dataFlowContext 数据上下文
     * @return 订单服务能够接受的报文
     */
    @Override
    public void deleteParkingAreaAttr(JSONObject paramInJson, DataFlowContext dataFlowContext) {

        ParkingAreaAttrPo parkingAreaAttrPo = BeanConvertUtil.covertBean(paramInJson, ParkingAreaAttrPo.class);
        super.update(dataFlowContext, parkingAreaAttrPo, BusinessTypeConstant.BUSINESS_TYPE_DELETE_PARKING_AREA_ATTR);
    }

}
