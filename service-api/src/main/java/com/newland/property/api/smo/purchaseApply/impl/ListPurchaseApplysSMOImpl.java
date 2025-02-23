package com.newland.property.api.smo.purchaseApply.impl;

import com.alibaba.fastjson.JSONObject;
import com.newland.property.api.smo.DefaultAbstractComponentSMO;
import com.newland.property.core.context.IPageData;
import com.newland.property.dto.system.ComponentValidateResult;
import com.newland.property.api.smo.purchaseApply.IListPurchaseApplysSMO;
import com.newland.property.utils.exception.SMOException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

/**
 * 查询purchaseApply服务类
 */
@Service("listPurchaseApplysSMOImpl")
public class ListPurchaseApplysSMOImpl extends DefaultAbstractComponentSMO implements IListPurchaseApplysSMO {

    @Autowired
    private RestTemplate restTemplate;

    @Override
    public ResponseEntity<String> listPurchaseApplys(IPageData pd) throws SMOException {
        return businessProcess(pd);
    }

    @Override
    protected void validate(IPageData pd, JSONObject paramIn) {

        super.validatePageInfo(pd);

        //super.checkUserHasPrivilege(pd, restTemplate, PrivilegeCodeConstant.AGENT_HAS_LIST_PURCHASEAPPLY);
    }

    @Override
    protected ResponseEntity<String> doBusinessProcess(IPageData pd, JSONObject paramIn) {
        ComponentValidateResult result = super.validateStoreStaffCommunityRelationship(pd, restTemplate);

//        Map paramMap = BeanConvertUtil.beanCovertMap(result);
//        paramIn.putAll(paramMap);
        paramIn.put("storeId",result.getStoreId());
        paramIn.put("storeTypeCd",result.getStoreTypeCd());
        paramIn.put("communityId",result.getCommunityId());
        paramIn.put("userId",result.getUserId());

        String apiUrl = "purchaseApply.listPurchaseApplys" + mapToUrlParam(paramIn);


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
}
