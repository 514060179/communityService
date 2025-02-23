package com.newland.property.api.smo.fee.impl;

import com.alibaba.fastjson.JSONObject;
import com.newland.property.api.smo.DefaultAbstractComponentSMO;
import com.newland.property.core.context.IPageData;
import com.newland.property.dto.system.ComponentValidateResult;
import com.newland.property.api.smo.fee.IDeleteFeeSMO;
import com.newland.property.utils.exception.SMOException;
import com.newland.property.utils.util.Assert;
import com.newland.property.utils.util.BeanConvertUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Map;

/**
 * 查询feeConfig服务类
 */
@Service("deleteFeeSMOImpl")
public class DeleteFeeSMOImpl extends DefaultAbstractComponentSMO implements IDeleteFeeSMO {

    @Autowired
    private RestTemplate restTemplate;

    @Override
    public ResponseEntity<String> deleteFee(IPageData pd) throws SMOException {
        return businessProcess(pd);
    }

    @Override
    protected void validate(IPageData pd, JSONObject paramIn) {
        Assert.hasKeyAndValue(paramIn, "communityId", "未包含小区ID");
        Assert.hasKeyAndValue(paramIn, "feeId", "未包含费用ID");
    }

    @Override
    protected ResponseEntity<String> doBusinessProcess(IPageData pd, JSONObject paramIn) {
        ComponentValidateResult result = super.validateStoreStaffCommunityRelationship(pd, restTemplate);

        Map paramMap = BeanConvertUtil.beanCovertMap(result);
        paramIn.putAll(paramMap);

        String apiUrl = "fee.deleteFee";


        ResponseEntity<String> responseEntity = this.callCenterService(restTemplate, pd, JSONObject.toJSONString(paramIn),
                apiUrl,
                HttpMethod.POST);

        return responseEntity;
    }

    public RestTemplate getRestTemplate() {
        return restTemplate;
    }

    public void setRestTemplate(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }
}
