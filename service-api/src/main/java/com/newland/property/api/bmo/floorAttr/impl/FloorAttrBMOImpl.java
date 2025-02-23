package com.newland.property.api.bmo.floorAttr.impl;

import com.alibaba.fastjson.JSONObject;
import com.newland.property.api.bmo.ApiBaseBMO;
import com.newland.property.api.bmo.floorAttr.IFloorAttrBMO;
import com.newland.property.core.context.DataFlowContext;
import com.newland.property.intf.community.IFloorAttrInnerServiceSMO;
import com.newland.property.po.floor.FloorAttrPo;
import com.newland.property.utils.constant.BusinessTypeConstant;
import com.newland.property.utils.util.BeanConvertUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service("floorAttrBMOImpl")
public class FloorAttrBMOImpl extends ApiBaseBMO implements IFloorAttrBMO {

    @Autowired
    private IFloorAttrInnerServiceSMO floorAttrInnerServiceSMOImpl;

    /**
     * 添加小区信息
     *
     * @param paramInJson     接口调用放传入入参
     * @param dataFlowContext 数据上下文
     * @return 订单服务能够接受的报文
     */
    @Override
    public void addFloorAttr(JSONObject paramInJson, DataFlowContext dataFlowContext) {

        paramInJson.put("attrId", "-1");
        FloorAttrPo floorAttrPo = BeanConvertUtil.covertBean(paramInJson, FloorAttrPo.class);
        super.insert(dataFlowContext, floorAttrPo, BusinessTypeConstant.BUSINESS_TYPE_SAVE_FLOOR_ATTR_INFO);
    }


    /**
     * 添加活动信息
     *
     * @param paramInJson     接口调用放传入入参
     * @param dataFlowContext 数据上下文
     * @return 订单服务能够接受的报文
     */
    @Override
    public void updateFloorAttr(JSONObject paramInJson, DataFlowContext dataFlowContext) {
        FloorAttrPo floorAttrPo = BeanConvertUtil.covertBean(paramInJson, FloorAttrPo.class);
        super.update(dataFlowContext, floorAttrPo, BusinessTypeConstant.BUSINESS_TYPE_UPDATE_FLOOR_ATTR_INFO);
    }


    /**
     * 添加小区信息
     *
     * @param paramInJson     接口调用放传入入参
     * @param dataFlowContext 数据上下文
     * @return 订单服务能够接受的报文
     */
    @Override
    public void deleteFloorAttr(JSONObject paramInJson, DataFlowContext dataFlowContext) {

        FloorAttrPo floorAttrPo = BeanConvertUtil.covertBean(paramInJson, FloorAttrPo.class);
        super.update(dataFlowContext, floorAttrPo, BusinessTypeConstant.BUSINESS_TYPE_DELETE_FLOOR_ATTR_INFO);
    }

}
