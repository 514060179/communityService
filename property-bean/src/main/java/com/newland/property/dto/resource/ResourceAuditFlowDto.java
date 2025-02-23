package com.newland.property.dto.resource;

import com.newland.property.dto.PageDto;
import com.newland.property.dto.oaWorkflow.OaWorkflowDto;

import java.io.Serializable;
import java.util.Date;

/**
 * @ClassName FloorDto
 * @Description 物品流程数据层封装
 * @Author wuxw
 * @Date 2019/4/24 8:52
 * @Version 1.0
 * add by wuxw 2019/4/24
 **/
public class ResourceAuditFlowDto extends OaWorkflowDto implements Serializable {

    //采购
    public static final String AUDIT_TYPE_PURCHASE_APPLY="10001";
    //领用
    public static final String AUDIT_TYPE_RESOURCE_OUT="10002";
    //调拨流程
    public static final String AUDIT_TYPE_ALLOCATION="10003";

    private String rafId;
    private String remark;
    private String storeId;
    private String flowId;
    private String flowName;
    private String auditType;

    private String communityId;


    private Date createTime;

    private String statusCd = "0";


    public String getRafId() {
        return rafId;
    }

    public void setRafId(String rafId) {
        this.rafId = rafId;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    @Override
    public String getStoreId() {
        return storeId;
    }

    @Override
    public void setStoreId(String storeId) {
        this.storeId = storeId;
    }

    @Override
    public String getFlowId() {
        return flowId;
    }

    @Override
    public void setFlowId(String flowId) {
        this.flowId = flowId;
    }

    @Override
    public String getFlowName() {
        return flowName;
    }

    @Override
    public void setFlowName(String flowName) {
        this.flowName = flowName;
    }


    public String getAuditType() {
        return auditType;
    }

    public void setAuditType(String auditType) {
        this.auditType = auditType;
    }

    @Override
    public Date getCreateTime() {
        return createTime;
    }

    @Override
    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    @Override
    public String getStatusCd() {
        return statusCd;
    }

    @Override
    public void setStatusCd(String statusCd) {
        this.statusCd = statusCd;
    }

    public String getCommunityId() {
        return communityId;
    }

    public void setCommunityId(String communityId) {
        this.communityId = communityId;
    }
}
