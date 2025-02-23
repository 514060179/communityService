package com.newland.property.api.smo.inspection.impl;

import com.alibaba.fastjson.JSONObject;
import com.newland.property.api.smo.DefaultAbstractComponentSMO;
import com.newland.property.core.context.IPageData;
import com.newland.property.api.smo.inspection.IAddInspectionRouteSMO;
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
@Service("addInspectionRouteSMOImpl")
public class AddInspectionRouteSMOImpl extends DefaultAbstractComponentSMO implements IAddInspectionRouteSMO {

    @Autowired
    private RestTemplate restTemplate;

    @Override
    protected void validate(IPageData pd, JSONObject paramIn) {

        //super.validatePageInfo(pd);

        //Assert.hasKeyAndValue(paramIn, "xxx", "xxx");
        Assert.hasKeyAndValue(paramIn, "routeName", "必填，请填写路线名称，字数100个以内");
        Assert.hasKeyAndValue(paramIn, "seq", "必填，请选择巡点名称");
        Assert.hasKeyAndValue(paramIn, "communityId", "小区ID不能为空");


        //super.checkUserHasPrivilege(pd, restTemplate, PrivilegeCodeConstant.AGENT_HAS_LIST_INSPECTIONROUTE);

    }

    @Override
    protected ResponseEntity<String> doBusinessProcess(IPageData pd, JSONObject paramIn) {
        ResponseEntity<String> responseEntity = null;
        super.validateStoreStaffCommunityRelationship(pd, restTemplate);

        responseEntity = this.callCenterService(restTemplate, pd, paramIn.toJSONString(),
                "inspectionRoute.saveInspectionRoute",
                HttpMethod.POST);
        return responseEntity;
    }

    @Override
    public ResponseEntity<String> saveInspectionRoute(IPageData pd) {
        return super.businessProcess(pd);
    }

    public RestTemplate getRestTemplate() {
        return restTemplate;
    }

    public void setRestTemplate(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }
}
