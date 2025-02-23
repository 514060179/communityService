package com.newland.property.api.smo.ownerRepair.impl;

import com.alibaba.fastjson.JSONObject;
import com.newland.property.api.smo.DefaultAbstractComponentSMO;
import com.newland.property.core.context.IPageData;
import com.newland.property.dto.system.ComponentValidateResult;
import com.newland.property.api.smo.ownerRepair.IListOwnerRepairsSMO;
import com.newland.property.utils.constant.PrivilegeCodeConstant;
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
 * 查询ownerRepair服务类
 */
@Service("listOwnerRepairsSMOImpl")
public class ListOwnerRepairsSMOImpl extends DefaultAbstractComponentSMO implements IListOwnerRepairsSMO {

    @Autowired
    private RestTemplate restTemplate;

    @Override
    public ResponseEntity<String> listOwnerRepairs(IPageData pd) throws SMOException {
        return businessProcess(pd);
    }

    @Override
    protected void validate(IPageData pd, JSONObject paramIn) {

        super.validatePageInfo(pd);
        Assert.hasKeyAndValue(paramIn, "communityId", "请求报文中未包含小区ID");


        super.checkUserHasPrivilege(pd, restTemplate, PrivilegeCodeConstant.LIST_OWNERREPAIR);
    }

    @Override
    protected ResponseEntity<String> doBusinessProcess(IPageData pd, JSONObject paramIn) {
        ComponentValidateResult result = super.validateStoreStaffCommunityRelationship(pd, restTemplate);

        Map paramMap = BeanConvertUtil.beanCovertMap(result);
        paramIn.putAll(paramMap);

        if(paramIn.containsKey("pageFlag") && "myRepairDispatch".equals(paramIn.getString("pageFlag"))){
            paramIn.put("staffId", result.getUserId());
        }


        String apiUrl = "ownerRepair.listOwnerRepairs" + mapToUrlParam(paramIn);


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
