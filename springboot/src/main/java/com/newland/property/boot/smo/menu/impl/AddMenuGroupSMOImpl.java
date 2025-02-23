package com.newland.property.boot.smo.menu.impl;

import com.alibaba.fastjson.JSONObject;
import com.newland.property.boot.smo.DefaultAbstractComponentSMO;
import com.newland.property.boot.smo.menu.IAddMenuGroupSMO;
import com.newland.property.core.context.IPageData;
import com.newland.property.utils.constant.PrivilegeCodeConstant;
import com.newland.property.utils.util.Assert;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

/**
 * 添加小区服务实现类
 * add by wuxw 2019-06-30
 */
@Service("addMenuGroupSMOImpl")
public class AddMenuGroupSMOImpl extends DefaultAbstractComponentSMO implements IAddMenuGroupSMO {

    @Autowired
    private RestTemplate restTemplate;

    @Override
    protected void validate(IPageData pd, JSONObject paramIn) {

        //super.validatePageInfo(pd);

        //Assert.hasKeyAndValue(paramIn, "xxx", "xxx");
        Assert.hasKeyAndValue(paramIn, "name", "必填，请填写组名称");
        Assert.hasKeyAndValue(paramIn, "icon", "必填，请填写icon");
        Assert.hasKeyAndValue(paramIn, "seq", "必填，请填写序列");


        super.checkUserHasPrivilege(pd, restTemplate, PrivilegeCodeConstant.MENU);

    }

    @Override
    protected ResponseEntity<String> doBusinessProcess(IPageData pd, JSONObject paramIn) {
        ResponseEntity<String> responseEntity = null;
        super.validateStoreStaffCommunityRelationship(pd, restTemplate);

        responseEntity = this.callCenterService(restTemplate, pd, paramIn.toJSONString(),
                "menuGroup.saveMenuGroup",
                HttpMethod.POST);
        return responseEntity;
    }

    @Override
    public ResponseEntity<String> saveMenuGroup(IPageData pd) {
        return super.businessProcess(pd);
    }

    public RestTemplate getRestTemplate() {
        return restTemplate;
    }

    public void setRestTemplate(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }
}
