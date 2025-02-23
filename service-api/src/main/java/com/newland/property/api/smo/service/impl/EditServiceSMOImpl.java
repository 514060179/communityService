package com.newland.property.api.smo.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.newland.property.api.smo.DefaultAbstractComponentSMO;
import com.newland.property.core.context.IPageData;
import com.newland.property.api.smo.service.IEditServiceSMO;
import com.newland.property.utils.constant.PrivilegeCodeConstant;
import com.newland.property.utils.util.Assert;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

/**
 * 添加服务服务实现类
 * add by wuxw 2019-06-30
 */
@Service("eidtServiceSMOImpl")
public class EditServiceSMOImpl extends DefaultAbstractComponentSMO implements IEditServiceSMO {

    @Autowired
    private RestTemplate restTemplate;

    @Override
    protected void validate(IPageData pd, JSONObject paramIn) {

        //super.validatePageInfo(pd);

        Assert.hasKeyAndValue(paramIn, "serviceId", "服务ID不能为空");
        Assert.hasKeyAndValue(paramIn, "name", "必填，请填写服务名称");
        Assert.hasKeyAndValue(paramIn, "serviceCode", "必填，请填写服务编码如 service.saveService");
        Assert.hasKeyAndValue(paramIn, "businessTypeCd", "可填，请填写秘钥，如果填写了需要加密传输");
        Assert.hasKeyAndValue(paramIn, "seq", "必填，请填写序列");
        Assert.hasKeyAndValue(paramIn, "isInstance", "可填，请填写实例 Y 或N");
        Assert.hasKeyAndValue(paramIn, "method", "必填，请填写调用方式");
        Assert.hasKeyAndValue(paramIn, "timeout", "必填，请填写超时时间");
        Assert.hasKeyAndValue(paramIn, "retryCount", "必填，请填写重试次数");
        Assert.hasKeyAndValue(paramIn, "provideAppId", "必填，请填写提供服务");


        super.checkUserHasPrivilege(pd, restTemplate, PrivilegeCodeConstant.AGENT_HAS_LIST_SERVICE);

    }

    @Override
    protected ResponseEntity<String> doBusinessProcess(IPageData pd, JSONObject paramIn) {
        ResponseEntity<String> responseEntity = null;
        super.validateStoreStaffCommunityRelationship(pd, restTemplate);

        responseEntity = this.callCenterService(restTemplate, pd, paramIn.toJSONString(),
                "service.updateService",
                HttpMethod.POST);
        return responseEntity;
    }

    @Override
    public ResponseEntity<String> updateService(IPageData pd) {
        return super.businessProcess(pd);
    }

    public RestTemplate getRestTemplate() {
        return restTemplate;
    }

    public void setRestTemplate(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }
}
