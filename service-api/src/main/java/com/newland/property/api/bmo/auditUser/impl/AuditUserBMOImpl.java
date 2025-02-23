package com.newland.property.api.bmo.auditUser.impl;

import com.alibaba.fastjson.JSONObject;
import com.newland.property.api.bmo.ApiBaseBMO;
import com.newland.property.api.bmo.auditUser.IAuditUserBMO;
import com.newland.property.core.context.DataFlowContext;
import com.newland.property.po.audit.AuditUserPo;
import com.newland.property.utils.constant.BusinessTypeConstant;
import com.newland.property.utils.util.BeanConvertUtil;
import org.springframework.stereotype.Service;

/**
 * @ClassName AuditUserBMOImpl
 * @Description TODO
 * @Author wuxw
 * @Date 2020/3/9 20:49
 * @Version 1.0
 * add by wuxw 2020/3/9
 **/
@Service("auditUserBMOImpl")
public class AuditUserBMOImpl extends ApiBaseBMO implements IAuditUserBMO {


    /**
     * 添加小区信息
     *
     * @param paramInJson     接口调用放传入入参
     * @param dataFlowContext 数据上下文
     * @return 订单服务能够接受的报文
     */
    @Override
    public void deleteAuditUser(JSONObject paramInJson, DataFlowContext dataFlowContext) {
        AuditUserPo auditUserPo = BeanConvertUtil.covertBean(paramInJson, AuditUserPo.class);
        super.delete(dataFlowContext, auditUserPo, BusinessTypeConstant.BUSINESS_TYPE_DELETE_AUDIT_USER);
    }


    /**
     * 添加小区信息
     *
     * @param paramInJson     接口调用放传入入参
     * @param dataFlowContext 数据上下文
     * @return 订单服务能够接受的报文
     */
    @Override
    public void addAuditUser(JSONObject paramInJson, DataFlowContext dataFlowContext) {

        paramInJson.put("auditUserId", "-1");

        AuditUserPo auditUserPo = BeanConvertUtil.covertBean(paramInJson, AuditUserPo.class);

        super.insert(dataFlowContext, auditUserPo, BusinessTypeConstant.BUSINESS_TYPE_SAVE_AUDIT_USER);
    }
}
