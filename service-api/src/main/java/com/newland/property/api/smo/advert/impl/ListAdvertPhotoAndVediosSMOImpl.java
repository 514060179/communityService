package com.newland.property.api.smo.advert.impl;

import com.alibaba.fastjson.JSONObject;
import com.newland.property.api.smo.DefaultAbstractComponentSMO;
import com.newland.property.core.context.IPageData;
import com.newland.property.api.smo.advert.IListAdvertPhotoAndVediosSMO;
import com.newland.property.utils.exception.SMOException;
import com.newland.property.utils.util.Assert;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

/**
 * 查询advert服务类
 */
@Service("listAdvertPhotoAndVediosSMOImpl")
public class ListAdvertPhotoAndVediosSMOImpl extends DefaultAbstractComponentSMO implements IListAdvertPhotoAndVediosSMO {

    @Autowired
    private RestTemplate restTemplate;

    @Override
    public ResponseEntity<String> listAdvertPhotoAndVideos(IPageData pd) throws SMOException {
        return businessProcess(pd);
    }

    @Override
    protected void validate(IPageData pd, JSONObject paramIn) {

        //super.validatePageInfo(pd);
        Assert.hasKeyAndValue(paramIn, "machineCode", "请求报文中未包含设备编码");
        Assert.hasKeyAndValue(paramIn, "communityId", "请求报文中未包含小区信息");

        //super.checkUserHasPrivilege(pd, restTemplate, PrivilegeCodeConstant.AGENT_HAS_LIST_ADVERT);
    }

    @Override
    protected ResponseEntity<String> doBusinessProcess(IPageData pd, JSONObject paramIn) {
        //ComponentValidateResult result = super.validateStoreStaffCommunityRelationship(pd, restTemplate);

        /*Map paramMap = BeanConvertUtil.beanCovertMap(result);
        paramIn.putAll(paramMap);*/

        String apiUrl = "advert.listAdvertPhotoAndVedios" + mapToUrlParam(paramIn);


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
