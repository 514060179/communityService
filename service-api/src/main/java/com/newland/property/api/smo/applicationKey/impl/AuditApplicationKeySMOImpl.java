package com.newland.property.api.smo.applicationKey.impl;

import com.alibaba.fastjson.JSONObject;
import com.newland.property.api.smo.DefaultAbstractComponentSMO;
import com.newland.property.core.context.IPageData;
import com.newland.property.api.smo.applicationKey.IAuditApplicationKeySMO;
import com.newland.property.utils.constant.PrivilegeCodeConstant;
import com.newland.property.utils.util.Assert;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

/**
 * 添加钥匙申请服务实现类
 * add by wuxw 2019-06-30
 */
@Service("auditApplicationKeySMOImpl")
public class AuditApplicationKeySMOImpl extends DefaultAbstractComponentSMO implements IAuditApplicationKeySMO {

    @Autowired
    private RestTemplate restTemplate;

    @Override
    protected void validate(IPageData pd, JSONObject paramIn) {

        //super.validatePageInfo(pd);

        Assert.hasKeyAndValue(paramIn, "applicationKeyId", "钥匙申请ID不能为空");
        Assert.hasKeyAndValue(paramIn, "communityId", "必填，请填写小区ID");

        Assert.hasKeyAndValue(paramIn, "state", "必填，请填写审核状态");
        Assert.hasKeyAndValue(paramIn, "remark", "必填，请填写审核原因");



        super.checkUserHasPrivilege(pd, restTemplate, PrivilegeCodeConstant.LIST_APPLICATION_KEY);

    }

    @Override
    protected ResponseEntity<String> doBusinessProcess(IPageData pd, JSONObject paramIn) {
        ResponseEntity<String> responseEntity = null;
        super.validateStoreStaffCommunityRelationship(pd, restTemplate);

        responseEntity = this.callCenterService(restTemplate, pd, paramIn.toJSONString(),
                "applicationKey.auditApplicationKey",
                HttpMethod.POST);
        return responseEntity;
    }

    @Override
    public ResponseEntity<String> auditApplicationKey(IPageData pd) {
        return super.businessProcess(pd);
    }

    public RestTemplate getRestTemplate() {
        return restTemplate;
    }

    public void setRestTemplate(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }
}
