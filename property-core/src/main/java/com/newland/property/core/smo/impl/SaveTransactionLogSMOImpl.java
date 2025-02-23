package com.newland.property.core.smo.impl;

import com.alibaba.fastjson.JSONObject;
import com.newland.property.core.context.Environment;
import com.newland.property.core.context.IPageData;
import com.newland.property.core.context.PageData;
import com.newland.property.core.factory.CallApiServiceFactory;
import com.newland.property.core.smo.ISaveTransactionLogSMO;
import com.newland.property.dto.app.AppDto;
import com.newland.property.dto.log.AssetImportLogDto;
import com.newland.property.intf.common.ITransactionLogInnerServiceSMO;
import com.newland.property.intf.common.ITransactionOutLogV1ServiceSMO;
import com.newland.property.po.log.TransactionLogPo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

/**
 * @ClassName SaveTransactionLogSMOImpl
 * @Description TODO
 * @Author wuxw
 * @Date 2020/11/16 0:43
 * @Version 1.0
 * add by wuxw 2020/11/16
 **/
@Service
public class SaveTransactionLogSMOImpl implements ISaveTransactionLogSMO {

    @Autowired(required = false)
    private ITransactionLogInnerServiceSMO transactionLogInnerServiceSMOImpl;

    @Autowired(required = false)
    private RestTemplate restTemplate;

    @Autowired(required = false)
    private RestTemplate outRestTemplate;

    @Autowired(required = false)
    private ITransactionOutLogV1ServiceSMO transactionOutLogV1InnerServiceSMOImpl;

    @Override
    @Async
    public void saveLog(TransactionLogPo transactionLogPo) {
        transactionLogInnerServiceSMOImpl.saveTransactionLog(transactionLogPo);
    }

    @Override
    @Async
    public void saveAssetImportLog(AssetImportLogDto assetImportLogDto) {

        String apiUrl = "http://api-service/api/assetImportLog/saveAssetImportLog";
        RestTemplate tmpRestTemplate = restTemplate;
        if (Environment.isStartBootWay()) {
            apiUrl = "http://127.0.0.1:8008/api/assetImportLog/saveAssetImportLog";
            tmpRestTemplate = outRestTemplate;
        }
        IPageData newPd = PageData.newInstance().builder("-1", "批量日志", "", "",
                "", "", apiUrl, "",
                AppDto.WEB_APP_ID);

        CallApiServiceFactory.callCenterService(tmpRestTemplate, newPd, JSONObject.toJSONString(assetImportLogDto), apiUrl, HttpMethod.POST);
    }

}
