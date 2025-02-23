package com.newland.property.fee.smo;

import com.alibaba.fastjson.JSONObject;
import com.newland.property.utils.exception.SMOException;
import com.newland.property.core.context.BusinessServiceDataFlow;

/**
 *
 * 用户信息管理，服务
 * Created by wuxw on 2017/4/5.
 */
public interface IFeeServiceSMO {


    public JSONObject service(BusinessServiceDataFlow businessServiceDataFlow) throws SMOException;

}
