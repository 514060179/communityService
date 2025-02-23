package com.newland.property.api.smo.community.impl;

import com.alibaba.fastjson.JSONObject;
import com.newland.property.api.smo.DefaultAbstractComponentSMO;
import com.newland.property.core.context.IPageData;
import com.newland.property.api.smo.community.IAuditEnterCommunitySMO;
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
@Service("auditEnterCommunitySMOImpl")
public class AuditEnterCommunitySMOImpl extends DefaultAbstractComponentSMO implements IAuditEnterCommunitySMO {

    @Autowired
    private RestTemplate restTemplate;

    @Override
    protected void validate(IPageData pd, JSONObject paramIn) {

        //super.validatePageInfo(pd);

        Assert.hasKeyAndValue(paramIn, "communityMemberId", "小区成员ID不能为空");
        Assert.hasKeyAndValue(paramIn, "state", "必填，请填写小区审核状态");
        Assert.hasKeyAndValue(paramIn, "remark", "必填，请填写小区审核原因");


        super.checkUserHasPrivilege(pd, restTemplate, PrivilegeCodeConstant.AUDIT_ENTER_COMMUNITY);

    }

    @Override
    protected ResponseEntity<String> doBusinessProcess(IPageData pd, JSONObject paramIn) {
        ResponseEntity<String> responseEntity = null;
        //super.validateStoreStaffCommunityRelationship(pd, restTemplate);
        super.validateStoreStaffRelationship(pd, restTemplate);

        responseEntity = this.callCenterService(restTemplate, pd, paramIn.toJSONString(),
                "community.auditEnterCommunity",
                HttpMethod.POST);
        return responseEntity;
    }

    @Override
    public ResponseEntity<String> auditEnterCommunity(IPageData pd) {
        return super.businessProcess(pd);
    }

    public RestTemplate getRestTemplate() {
        return restTemplate;
    }

    public void setRestTemplate(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }
}
