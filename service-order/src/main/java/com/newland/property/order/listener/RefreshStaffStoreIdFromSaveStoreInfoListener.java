package com.newland.property.order.listener;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.newland.property.core.annotation.NewlandPropertyListener;
import com.newland.property.core.event.app.order.Ordered;
import com.newland.property.core.event.center.event.InvokeBusinessBSuccessEvent;
import com.newland.property.core.event.center.listener.DataFlowListener;
import com.newland.property.dto.system.Business;
import com.newland.property.po.store.StoreUserPo;
import com.newland.property.utils.constant.BusinessTypeConstant;
import org.slf4j.Logger;
import com.newland.property.core.log.LoggerFactory;

import java.util.List;

/**
 * 刷新 员工的storeId
 * Created by Administrator on 2019/3/30.
 */
@NewlandPropertyListener(name = "refreshStaffStoreIdFromSaveStoreInfoListener")
public class RefreshStaffStoreIdFromSaveStoreInfoListener implements DataFlowListener<InvokeBusinessBSuccessEvent>, Ordered {

    private final static Logger logger = LoggerFactory.getLogger(RefreshStaffStoreIdFromSaveStoreInfoListener.class);

    @Override
    public int getOrder() {
        return 2;
    }

    @Override
    public void soService(InvokeBusinessBSuccessEvent event) {

        Business business = event.getBusiness();
        if (!BusinessTypeConstant.BUSINESS_TYPE_SAVE_STORE_INFO.equals(business.getBusinessTypeCd())) {
            return;
        }
        List<Business> businessList = event.getDataFlow().getBusinessList();
        JSONObject businessResponseData = event.getBusinessResponseData();

        if (!businessResponseData.containsKey("storeId")) {
            return;
        }

        String storeId = businessResponseData.getString("storeId");

        for (Business tmpBusiness : businessList) {
            if (!BusinessTypeConstant.BUSINESS_TYPE_SAVE_STORE_USER.equals(tmpBusiness.getBusinessTypeCd())) {
                continue;
            }

            JSONArray businessStoreUsers = tmpBusiness.getData().getJSONArray(StoreUserPo.class.getSimpleName());
            dealBusinessStoreUserStoreId(businessStoreUsers, storeId);
        }
    }


    /**
     * businessStoreUser 中storeId 为负数的问题
     *
     * @param businessStoreUsers
     */
    private void dealBusinessStoreUserStoreId(JSONArray businessStoreUsers, String storeId) {
        for (int bStoreUserIndex = 0; bStoreUserIndex < businessStoreUsers.size(); bStoreUserIndex++) {
            JSONObject businessStoreUser = businessStoreUsers.getJSONObject(bStoreUserIndex);

            if (!businessStoreUser.containsKey("storeId") || "-1".equals(businessStoreUser.getString("storeId"))) {
                businessStoreUser.put("storeId", storeId);
            }

        }
    }
}
