package com.newland.property.api.listener;

import com.alibaba.fastjson.JSONObject;
import com.newland.property.dto.system.AppService;
import com.newland.property.utils.constant.CommonConstant;
import com.newland.property.utils.constant.ServiceCodeConstant;
import com.newland.property.utils.util.Assert;
import com.newland.property.core.annotation.NewlandPropertyListener;
import com.newland.property.core.context.DataFlowContext;
import com.newland.property.core.event.service.api.ServiceDataFlowEvent;
import org.slf4j.Logger;
import com.newland.property.core.log.LoggerFactory;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;

/**
 * 订单类信息处理 侦听
 * Created by wuxw on 2018/5/18.
 */
@NewlandPropertyListener("orderServiceListener")
public class OrderServiceListener extends AbstractServiceApiDataFlowListener{

    private final static Logger logger = LoggerFactory.getLogger(OrderServiceListener.class);



    @Override
    public String getServiceCode() {
        return ServiceCodeConstant.SERVICE_CODE_DO_SERVICE_ORDER;
    }

    @Override
    public HttpMethod getHttpMethod() {
        return HttpMethod.POST;
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
        Assert.jsonObjectHaveKey(paramIn,"orders","请求参数中未包含");
        JSONObject paramInObj = JSONObject.parseObject(paramIn);
        HttpHeaders header = new HttpHeaders();
        for(String key : dataFlowContext.getRequestCurrentHeaders().keySet()){
            header.add(key,dataFlowContext.getRequestCurrentHeaders().get(key));

            if(CommonConstant.HTTP_APP_ID.equals(key)) {
                paramInObj.put("appId", dataFlowContext.getRequestCurrentHeaders().get(key));
            }
            if(CommonConstant.HTTP_TRANSACTION_ID.equals(key)) {
                paramInObj.put("transactionId", dataFlowContext.getRequestCurrentHeaders().get(key));
            }
            if(CommonConstant.HTTP_SIGN.equals(key)) {
                paramInObj.put("sign", dataFlowContext.getRequestCurrentHeaders().get(key));
            }

            if(CommonConstant.HTTP_SIGN.equals(key)) {
                paramInObj.put("requestTime", dataFlowContext.getRequestCurrentHeaders().get(key));
            }
        }

        HttpEntity<String> httpEntity = new HttpEntity<String>(paramInObj.toJSONString(), header);
        //http://user-service/test/sayHello
        super.doRequest(dataFlowContext, service, httpEntity);
    }





}
