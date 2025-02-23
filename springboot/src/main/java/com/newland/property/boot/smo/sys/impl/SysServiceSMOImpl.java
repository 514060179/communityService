package com.newland.property.boot.smo.sys.impl;

import com.alibaba.fastjson.JSONObject;
import com.newland.property.boot.smo.DefaultAbstractComponentSMO;
import com.newland.property.boot.smo.sys.ISysServiceSMO;
import com.newland.property.core.context.IPageData;
import com.newland.property.utils.cache.MappingCache;
import com.newland.property.utils.constant.MappingConstant;
import com.newland.property.utils.exception.SMOException;
import com.newland.property.utils.util.StringUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service("sysServiceSMOImpl")
public class SysServiceSMOImpl extends DefaultAbstractComponentSMO implements ISysServiceSMO {


    @Autowired
    private RestTemplate restTemplate;

    @Override
    public ResponseEntity<String> getSysInfo(IPageData pd) throws SMOException {
        return businessProcess(pd);
    }

    @Override
    protected void validate(IPageData pd, JSONObject paramIn) {

        /*super.validatePageInfo(pd);

        super.checkUserHasPrivilege(pd, restTemplate, PrivilegeCodeConstant.LIST_ORG);*/
    }

    @Override
    protected ResponseEntity<String> doBusinessProcess(IPageData pd, JSONObject paramIn) {
        /*ComponentValidateResult result = super.validateStoreStaffCommunityRelationship(pd, restTemplate);

        Map paramMap = BeanConvertUtil.beanCovertMap(result);
        paramIn.putAll(paramMap);

        String apiUrl = "org.listOrgs" + mapToUrlParam(paramIn);


        ResponseEntity<String> responseEntity = this.callCenterService(restTemplate, pd, "",
                apiUrl,
                HttpMethod.GET);*/

        String logo = MappingCache.getValue("SYS_LOGO");
        if (StringUtil.isEmpty(logo)) {
            logo = "HC";
        }
        String apiUrl = MappingCache.getValue(MappingConstant.URL_DOMAIN,"SYS_API_URL");

        JSONObject sysInfo = new JSONObject();
        sysInfo.put("logo", logo);
        sysInfo.put("apiUrl", apiUrl);

        return new ResponseEntity<String>(sysInfo.toJSONString(), HttpStatus.OK);
    }

    public RestTemplate getRestTemplate() {
        return restTemplate;
    }

    public void setRestTemplate(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }
}
