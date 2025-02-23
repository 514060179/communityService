package com.newland.property.api.listener;

import com.newland.property.dto.system.AppService;
import com.newland.property.utils.constant.ServiceCodeConstant;
import com.newland.property.core.annotation.NewlandPropertyListener;
import com.newland.property.core.context.DataFlowContext;
import com.newland.property.core.event.service.api.ServiceDataFlowEvent;
import org.slf4j.Logger;
import com.newland.property.core.log.LoggerFactory;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;

/**
 * 保存 用户信息 侦听
 * Created by wuxw on 2018/5/18.
 */
@NewlandPropertyListener("transferServiceListener")
public class TransferServiceListener extends AbstractServiceApiDataFlowListener{

    private final static Logger logger = LoggerFactory.getLogger(TransferServiceListener.class);



    @Override
    public String getServiceCode() {
        return ServiceCodeConstant.SERVICE_CODE_DO_SERVICE_TRANSFER;
    }

    @Override
    public HttpMethod getHttpMethod() {
        return null;
    }


    @Override
    public int getOrder() {
        return 0;
    }


    @Override
    public void soService(ServiceDataFlowEvent event) {
        //获取数据上下文对象
        DataFlowContext dataFlowContext = event.getDataFlowContext();
        AppService service = event.getAppService();
        String paramIn = dataFlowContext.getReqData();
        HttpHeaders header = new HttpHeaders();
        for(String key : dataFlowContext.getRequestCurrentHeaders().keySet()){
            header.add(key,dataFlowContext.getRequestCurrentHeaders().get(key));
        }
        HttpEntity<String> httpEntity = new HttpEntity<String>(paramIn, header);
        //http://user-service/test/sayHello
        super.doRequest(dataFlowContext, service, httpEntity);
    }





}
