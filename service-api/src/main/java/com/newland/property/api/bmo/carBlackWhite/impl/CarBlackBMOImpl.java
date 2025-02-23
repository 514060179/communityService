package com.newland.property.api.bmo.carBlackWhite.impl;

import com.alibaba.fastjson.JSONObject;
import com.newland.property.api.bmo.ApiBaseBMO;
import com.newland.property.api.bmo.carBlackWhite.ICarBlackWhiteBMO;
import com.newland.property.core.context.DataFlowContext;
import com.newland.property.po.car.CarBlackWhitePo;
import com.newland.property.utils.constant.BusinessTypeConstant;
import com.newland.property.utils.util.BeanConvertUtil;
import org.springframework.stereotype.Service;

/**
 * @ClassName ApiBaseBMOImpl
 * @Description TODO
 * @Author wuxw
 * @Date 2020/3/9 20:58
 * @Version 1.0
 * add by wuxw 2020/3/9
 **/
@Service("carBlackBMOImpl")
public class CarBlackBMOImpl extends ApiBaseBMO implements ICarBlackWhiteBMO {


    /**
     * 添加小区信息
     *
     * @param paramInJson     接口调用放传入入参
     * @param dataFlowContext 数据上下文
     * @return 订单服务能够接受的报文
     */
    @Override
    public void deleteCarBlackWhite(JSONObject paramInJson, DataFlowContext dataFlowContext) {

        CarBlackWhitePo carBlackWhitePo = BeanConvertUtil.covertBean(paramInJson, CarBlackWhitePo.class);
        super.delete(dataFlowContext, carBlackWhitePo, BusinessTypeConstant.BUSINESS_TYPE_DELETE_CAR_BLACK_WHITE);
    }

    /**
     * 添加小区信息
     *
     * @param paramInJson     接口调用放传入入参
     * @param dataFlowContext 数据上下文
     * @return 订单服务能够接受的报文
     */
    @Override
    public void addCarBlackWhite(JSONObject paramInJson, DataFlowContext dataFlowContext) {


        paramInJson.put("bwId", "-1");

        CarBlackWhitePo carBlackWhitePo = BeanConvertUtil.covertBean(paramInJson, CarBlackWhitePo.class);

        super.insert(dataFlowContext, carBlackWhitePo, BusinessTypeConstant.BUSINESS_TYPE_SAVE_CAR_BLACK_WHITE);
    }


    /**
     * 添加黑白名单信息
     *
     * @param paramInJson     接口调用放传入入参
     * @param dataFlowContext 数据上下文
     * @return 订单服务能够接受的报文
     */
    @Override
    public void updateCarBlackWhite(JSONObject paramInJson, DataFlowContext dataFlowContext) {
        CarBlackWhitePo carBlackWhitePo = BeanConvertUtil.covertBean(paramInJson,CarBlackWhitePo.class);

        super.update(dataFlowContext,carBlackWhitePo,BusinessTypeConstant.BUSINESS_TYPE_UPDATE_CAR_BLACK_WHITE);
    }


}
