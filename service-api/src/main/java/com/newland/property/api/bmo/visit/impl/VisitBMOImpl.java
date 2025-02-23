package com.newland.property.api.bmo.visit.impl;

import com.alibaba.fastjson.JSONObject;
import com.newland.property.api.bmo.ApiBaseBMO;
import com.newland.property.api.bmo.visit.IVisitBMO;
import com.newland.property.core.context.DataFlowContext;
import com.newland.property.po.owner.VisitPo;
import com.newland.property.utils.constant.BusinessTypeConstant;
import com.newland.property.utils.util.BeanConvertUtil;
import org.springframework.stereotype.Service;

/**
 * @ClassName VisitBMOImpl
 * @Description TODO
 * @Author wuxw
 * @Date 2020/3/10 0:06
 * @Version 1.0
 * add by wuxw 2020/3/10
 **/
@Service("visitBMOImpl")
public class VisitBMOImpl extends ApiBaseBMO implements IVisitBMO {

    /**
     * 添加小区信息
     *
     * @param paramInJson     接口调用放传入入参
     * @param dataFlowContext 数据上下文
     * @return 订单服务能够接受的报文
     */
    @Override
    public void deleteVisit(JSONObject paramInJson, DataFlowContext dataFlowContext) {

        VisitPo visitPo = BeanConvertUtil.covertBean(paramInJson, VisitPo.class);
        visitPo.setStatusCd("1");
        super.delete(dataFlowContext, visitPo, BusinessTypeConstant.BUSINESS_TYPE_DELETE_VISIT);

    }

    /**
     * 添加小区信息
     *
     * @param paramInJson     接口调用放传入入参
     * @param dataFlowContext 数据上下文
     * @return 订单服务能够接受的报文
     */
    @Override
    public void addVisit(JSONObject paramInJson, DataFlowContext dataFlowContext) {

        JSONObject businessVisit = new JSONObject();
        businessVisit.putAll(paramInJson);

        VisitPo visitPo = BeanConvertUtil.covertBean(businessVisit, VisitPo.class);
        super.insert(dataFlowContext, visitPo, BusinessTypeConstant.BUSINESS_TYPE_SAVE_VISIT);
    }

    /**
     * 添加访客登记信息
     *
     * @param paramInJson     接口调用放传入入参
     * @param dataFlowContext 数据上下文
     * @return 订单服务能够接受的报文
     */
    @Override
    public void updateVisit(JSONObject paramInJson, DataFlowContext dataFlowContext) {
        VisitPo visitPo = BeanConvertUtil.covertBean(paramInJson, VisitPo.class);
        super.update(dataFlowContext, visitPo, BusinessTypeConstant.BUSINESS_TYPE_UPDATE_VISIT);
    }
}
