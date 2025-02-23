package com.newland.property.api.smo.fee.impl;

import com.alibaba.fastjson.JSONObject;
import com.newland.property.api.smo.DefaultAbstractComponentSMO;
import com.newland.property.core.context.IPageData;
import com.newland.property.dto.system.ComponentValidateResult;
import com.newland.property.api.smo.fee.IListPayFeeSMO;
import com.newland.property.utils.constant.PrivilegeCodeConstant;
import com.newland.property.utils.exception.SMOException;
import com.newland.property.utils.util.Assert;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

/**
 * 查询app服务类
 */
@Service("listPayFeeSMOImpl")
public class ListPayFeeSMOImpl extends DefaultAbstractComponentSMO implements IListPayFeeSMO {

    @Autowired
    private RestTemplate restTemplate;

    @Override
    public ResponseEntity<String> list(IPageData pd) throws SMOException {
        return businessProcess(pd);
    }

    @Override
    protected void validate(IPageData pd, JSONObject paramIn) {

        super.validatePageInfo(pd);
        Assert.hasKeyAndValue(paramIn, "communityId", "未包含小区信息");

        super.checkUserHasPrivilege(pd, restTemplate, PrivilegeCodeConstant.LIST_PAY_FEE);
    }

    @Override
    protected ResponseEntity<String> doBusinessProcess(IPageData pd, JSONObject paramIn) {
        ComponentValidateResult result = super.validateStoreStaffCommunityRelationship(pd, restTemplate);

//        Map paramMap = BeanConvertUtil.beanCovertMap(result);
//        paramIn.putAll(paramMap);
        int page = paramIn.getInteger("page");
        int row = paramIn.getInteger("row");
        paramIn.put("storeId", result.getStoreId());
        paramIn.put("page", (page - 1) * row);
        paramIn.put("row", row);

        String apiUrl = "";
        if (!paramIn.containsKey("payObjType") || "3333".equals(paramIn.getString("payObjType"))) {
            apiUrl = "api.getPropertyPayFee" + mapToUrlParam(paramIn);
        } else if ("6666".equals(paramIn.getString("payObjType"))) {
            apiUrl = "api.getParkingSpacePayFee" + mapToUrlParam(paramIn);
        } else {
            apiUrl = "api.getListPayFee" + mapToUrlParam(paramIn);
        }


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
