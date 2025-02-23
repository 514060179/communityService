package com.newland.property.order.listener;

import com.alibaba.fastjson.JSONObject;
import com.newland.property.utils.constant.BusinessTypeConstant;
import com.newland.property.core.annotation.NewlandPropertyListener;
import com.newland.property.dto.system.Business;
import com.newland.property.core.event.app.order.Ordered;
import com.newland.property.core.event.center.event.InvokeBusinessBSuccessEvent;
import com.newland.property.core.event.center.listener.DataFlowListener;
import org.slf4j.Logger;
import com.newland.property.core.log.LoggerFactory;

/**
 *
 * 处理商户服务返回未将storeId 返回出去问题
 * Created by Administrator on 2019/3/30.
 */
@NewlandPropertyListener(name = "responseStoreIdFromSaveStoreInfoListener")
public class ResponseStoreIdFromSaveStoreInfoListener implements DataFlowListener<InvokeBusinessBSuccessEvent>,Ordered {

    private final static Logger logger = LoggerFactory.getLogger(ResponseStoreIdFromSaveStoreInfoListener.class);

    @Override
    public int getOrder() {
        return 1;
    }

    @Override
    public void soService(InvokeBusinessBSuccessEvent event) {

        Business business = event.getBusiness();
        if(!BusinessTypeConstant.BUSINESS_TYPE_SAVE_STORE_INFO.equals(business.getBusinessTypeCd())){
            return ;
        }

        JSONObject businessResponseData = event.getBusinessResponseData();

        if(!businessResponseData.containsKey("storeId")) {
            return;
        }

        JSONObject storeInfo = new JSONObject();
        storeInfo.put("storeId",businessResponseData.getString("storeId"));
        event.getDataFlow().getResJson().getJSONArray("msg").add(storeInfo);


    }
}
