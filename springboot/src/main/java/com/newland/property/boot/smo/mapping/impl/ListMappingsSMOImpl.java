package com.newland.property.boot.smo.mapping.impl;

import com.alibaba.fastjson.JSONObject;
import com.newland.property.boot.smo.DefaultAbstractComponentSMO;
import com.newland.property.boot.smo.mapping.IListMappingsSMO;
import com.newland.property.core.context.IPageData;
import com.newland.property.dto.system.ComponentValidateResult;
import com.newland.property.utils.constant.PrivilegeCodeConstant;
import com.newland.property.utils.exception.SMOException;
import com.newland.property.utils.util.BeanConvertUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Map;

/**
 * 查询mapping服务类
 */
@Service("listMappingsSMOImpl")
public class ListMappingsSMOImpl extends DefaultAbstractComponentSMO implements IListMappingsSMO {

    @Autowired
    private RestTemplate restTemplate;

    @Override
    public ResponseEntity<String> listMappings(IPageData pd) throws SMOException {
        return businessProcess(pd);
    }

    @Override
    protected void validate(IPageData pd, JSONObject paramIn) {

        super.validatePageInfo(pd);

        super.checkUserHasPrivilege(pd, restTemplate, PrivilegeCodeConstant.AGENT_HAS_LIST_MAPPING);
    }

    @Override
    protected ResponseEntity<String> doBusinessProcess(IPageData pd, JSONObject paramIn) {
        ComponentValidateResult result = super.validateStoreStaffCommunityRelationship(pd, restTemplate);

        Map paramMap = BeanConvertUtil.beanCovertMap(result);
        paramIn.putAll(paramMap);

        String apiUrl = "mapping.listMappings" + mapToUrlParam(paramIn);


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
