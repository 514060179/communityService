package com.newland.property.api.smo.community.impl;

import com.alibaba.fastjson.JSONObject;
import com.newland.property.api.smo.DefaultAbstractComponentSMO;
import com.newland.property.core.context.IPageData;
import com.newland.property.api.smo.community.IAuditCommunitySMO;
import com.newland.property.utils.constant.PrivilegeCodeConstant;
import com.newland.property.utils.util.Assert;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

/**
 * 审核小区服务实现类
 * add by wuxw 2019-06-30
 */
@Service("auditCommunitySMOImpl")
public class AuditCommunitySMOImpl extends DefaultAbstractComponentSMO implements IAuditCommunitySMO {

    @Autowired
    private RestTemplate restTemplate;

    @Override
    protected void validate(IPageData pd, JSONObject paramIn) {

        //super.validatePageInfo(pd);

        Assert.hasKeyAndValue(paramIn, "communityId", "小区ID不能为空");
        Assert.hasKeyAndValue(paramIn, "state", "必填，请填写小区审核状态");
        Assert.hasKeyAndValue(paramIn, "remark", "必填，请填写小区审核原因");


        super.checkUserHasPrivilege(pd, restTemplate, PrivilegeCodeConstant.AUDIT_COMMUNITY);

    }

    @Override
    protected ResponseEntity<String> doBusinessProcess(IPageData pd, JSONObject paramIn) {
        ResponseEntity<String> responseEntity = null;
        //super.validateStoreStaffCommunityRelationship(pd, restTemplate);
        super.validateStoreStaffRelationship(pd, restTemplate);

        responseEntity = this.callCenterService(restTemplate, pd, paramIn.toJSONString(),
                "community.auditCommunity",
                HttpMethod.POST);
        return responseEntity;
    }

    @Override
    public ResponseEntity<String> auditCommunity(IPageData pd) {
        return super.businessProcess(pd);
    }

    public RestTemplate getRestTemplate() {
        return restTemplate;
    }

    public void setRestTemplate(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }
}
