package com.newland.property.api.smo.store.impl;

import com.alibaba.fastjson.JSONObject;
import com.newland.property.api.smo.DefaultAbstractComponentSMO;
import com.newland.property.core.context.IPageData;
import com.newland.property.dto.system.ComponentValidateResult;
import com.newland.property.api.smo.store.IGetStoreSMO;
import com.newland.property.utils.exception.SMOException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

/**
 * 查询carInout服务类
 */
@Service("getStoreSMOImpl")
public class GetStoreSMOImpl extends DefaultAbstractComponentSMO implements IGetStoreSMO {

    @Autowired
    private RestTemplate restTemplate;


    @Override
    protected void validate(IPageData pd, JSONObject paramIn) {
        super.validatePageInfo(pd);
    }

    @Override
    protected ResponseEntity<String> doBusinessProcess(IPageData pd, JSONObject paramIn) {
        ComponentValidateResult result = super.validateStoreStaffCommunityRelationship(pd, restTemplate);
        if(result.getStoreId() == null || result.getStoreId() == ""){
            throw new IllegalArgumentException("参数异常,没有找到对应的storeId");
        }
        paramIn.put("storeId", result.getStoreId());

        String apiUrl ="store.listStores" + mapToUrlParam(paramIn);
        ResponseEntity<String> responseEntity = this.callCenterService(restTemplate, pd, "",
                apiUrl,
                HttpMethod.GET);

        return responseEntity;
    }

    public RestTemplate getRestTemplate() {
        return restTemplate;
    }

    public void setRestTemplate(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    @Override
    public ResponseEntity<String> getStoreInfo(IPageData pd) throws SMOException {
        return businessProcess(pd);
    }
}
