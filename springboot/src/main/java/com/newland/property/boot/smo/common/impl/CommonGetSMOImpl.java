package com.newland.property.boot.smo.common.impl;

import com.alibaba.fastjson.JSONObject;
import com.newland.property.boot.smo.DefaultAbstractComponentSMO;
import com.newland.property.boot.smo.common.ICommonGetSMO;
import com.newland.property.core.context.IPageData;
import com.newland.property.dto.system.ComponentValidateResult;
import com.newland.property.utils.constant.ServiceConstant;
import com.newland.property.utils.util.BeanConvertUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Map;

/**
 * @ClassName CommonSMOImpl
 * @Description 通用服务处理
 * @Author wuxw
 * @Date 2020/3/8 23:37
 * @Version 1.0
 * add by wuxw 2020/3/8
 **/
@Service("commonGetSMOImpl")
public class CommonGetSMOImpl extends DefaultAbstractComponentSMO implements ICommonGetSMO {

    @Autowired
    private RestTemplate restTemplate;

    @Override
    protected void validate(IPageData pd, JSONObject paramIn) {

    }

    @Override
    protected ResponseEntity<String> doBusinessProcess(IPageData pd, JSONObject paramIn) throws Exception {
        ComponentValidateResult result = super.validateStoreStaffCommunityRelationship(pd, restTemplate);

        Map paramMap = BeanConvertUtil.beanCovertMap(result);
        paramIn.putAll(paramMap);

        String apiUrl = ServiceConstant.SERVICE_API_URL + pd.getApiUrl() + mapToUrlParam(paramIn);

        ResponseEntity<String> responseEntity = this.callCenterService(restTemplate, pd, "",
                apiUrl,
                HttpMethod.GET);

        return responseEntity;
    }

    @Override
    public ResponseEntity<String> doService(IPageData pd) {
        return businessProcess(pd);
    }
}
