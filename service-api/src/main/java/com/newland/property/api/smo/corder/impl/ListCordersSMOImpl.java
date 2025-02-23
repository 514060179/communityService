package com.newland.property.api.smo.corder.impl;

import com.alibaba.fastjson.JSONObject;
import com.newland.property.api.smo.DefaultAbstractComponentSMO;
import com.newland.property.core.context.IPageData;
import com.newland.property.api.smo.corder.IListCordersSMO;
import com.newland.property.utils.exception.SMOException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;

@Service("listCordersItemSMOImpl")
public class ListCordersSMOImpl extends DefaultAbstractComponentSMO implements IListCordersSMO {
    @Autowired
    private RestTemplate restTemplate;

    @Override
    protected void validate(IPageData pd, JSONObject paramIn) {
        super.validatePageInfo(pd);
    }

    @Override
    protected ResponseEntity<String> doBusinessProcess(IPageData pd, JSONObject paramIn) throws IOException {

        String apiUrl = "corders.listCorders" + mapToUrlParam(paramIn);


        ResponseEntity<String> responseEntity = this.callCenterService(restTemplate, pd, "",
                apiUrl,
                HttpMethod.GET);

        return responseEntity;
    }

    @Override
    public ResponseEntity<String> listCorders(IPageData pd) throws SMOException {
        return super.businessProcess(pd);
    }
}
