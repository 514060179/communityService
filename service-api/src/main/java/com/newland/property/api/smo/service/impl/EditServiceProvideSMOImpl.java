package com.newland.property.api.smo.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.newland.property.api.smo.DefaultAbstractComponentSMO;
import com.newland.property.core.context.IPageData;
import com.newland.property.api.smo.service.IEditServiceProvideSMO;
import com.newland.property.utils.constant.PrivilegeCodeConstant;
import com.newland.property.utils.util.Assert;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

/**
 * 添加服务提供服务实现类
 * add by wuxw 2019-06-30
 */
@Service("eidtServiceProvideSMOImpl")
public class EditServiceProvideSMOImpl extends DefaultAbstractComponentSMO implements IEditServiceProvideSMO {

    @Autowired
    private RestTemplate restTemplate;

    @Override
    protected void validate(IPageData pd, JSONObject paramIn) {

        //super.validatePageInfo(pd);

        Assert.hasKeyAndValue(paramIn, "id", "提供ID不能为空");
        Assert.hasKeyAndValue(paramIn, "name", "必填，请填写服务名称");
        Assert.hasKeyAndValue(paramIn, "serviceCode", "必填，请填写服务编码");
        Assert.hasKeyAndValue(paramIn, "params", "必填，请填写参数");
        Assert.hasKeyAndValue(paramIn, "queryModel", "必填，请选择是否显示菜单");


        super.checkUserHasPrivilege(pd, restTemplate, PrivilegeCodeConstant.SERVICE_PROVIDE);

    }

    @Override
    protected ResponseEntity<String> doBusinessProcess(IPageData pd, JSONObject paramIn) {
        ResponseEntity<String> responseEntity = null;
        super.validateStoreStaffCommunityRelationship(pd, restTemplate);

        responseEntity = this.callCenterService(restTemplate, pd, paramIn.toJSONString(),
                "serviceProvide.updateServiceProvide",
                HttpMethod.POST);
        return responseEntity;
    }

    @Override
    public ResponseEntity<String> updateServiceProvide(IPageData pd) {
        return super.businessProcess(pd);
    }

    public RestTemplate getRestTemplate() {
        return restTemplate;
    }

    public void setRestTemplate(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }
}
