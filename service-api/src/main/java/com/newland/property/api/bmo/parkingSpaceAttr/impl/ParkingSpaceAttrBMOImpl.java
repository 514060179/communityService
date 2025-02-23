package com.newland.property.api.bmo.parkingSpaceAttr.impl;

import com.alibaba.fastjson.JSONObject;
import com.newland.property.api.bmo.ApiBaseBMO;
import com.newland.property.api.bmo.parkingSpaceAttr.IParkingSpaceAttrBMO;
import com.newland.property.core.context.DataFlowContext;
import com.newland.property.intf.community.IParkingSpaceAttrInnerServiceSMO;
import com.newland.property.po.parking.ParkingSpaceAttrPo;
import com.newland.property.utils.constant.BusinessTypeConstant;
import com.newland.property.utils.util.BeanConvertUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service("parkingSpaceAttrBMOImpl")
public class ParkingSpaceAttrBMOImpl extends ApiBaseBMO implements IParkingSpaceAttrBMO {

    @Autowired
    private IParkingSpaceAttrInnerServiceSMO parkingSpaceAttrInnerServiceSMOImpl;

    /**
     * 添加小区信息
     *
     * @param paramInJson     接口调用放传入入参
     * @param dataFlowContext 数据上下文
     * @return 订单服务能够接受的报文
     */
    @Override
    public void addParkingSpaceAttr(JSONObject paramInJson, DataFlowContext dataFlowContext) {

        paramInJson.put("attrId", "-1");
        ParkingSpaceAttrPo parkingSpaceAttrPo = BeanConvertUtil.covertBean(paramInJson, ParkingSpaceAttrPo.class);
        super.insert(dataFlowContext, parkingSpaceAttrPo, BusinessTypeConstant.BUSINESS_TYPE_SAVE_PARKING_SPACE_ATTR);
    }


    /**
     * 添加活动信息
     *
     * @param paramInJson     接口调用放传入入参
     * @param dataFlowContext 数据上下文
     * @return 订单服务能够接受的报文
     */
    @Override
    public void updateParkingSpaceAttr(JSONObject paramInJson, DataFlowContext dataFlowContext) {
        ParkingSpaceAttrPo parkingSpaceAttrPo = BeanConvertUtil.covertBean(paramInJson, ParkingSpaceAttrPo.class);
        super.update(dataFlowContext, parkingSpaceAttrPo, BusinessTypeConstant.BUSINESS_TYPE_UPDATE_PARKING_SPACE_ATTR);
    }


    /**
     * 添加小区信息
     *
     * @param paramInJson     接口调用放传入入参
     * @param dataFlowContext 数据上下文
     * @return 订单服务能够接受的报文
     */
    @Override
    public void deleteParkingSpaceAttr(JSONObject paramInJson, DataFlowContext dataFlowContext) {

        ParkingSpaceAttrPo parkingSpaceAttrPo = BeanConvertUtil.covertBean(paramInJson, ParkingSpaceAttrPo.class);
        super.update(dataFlowContext, parkingSpaceAttrPo, BusinessTypeConstant.BUSINESS_TYPE_DELETE_PARKING_SPACE_ATTR);
    }

}
