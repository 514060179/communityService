package com.newland.property.oa.smo;

import com.alibaba.fastjson.JSONObject;
import com.newland.property.core.context.BusinessServiceDataFlow;
import com.newland.property.utils.exception.SMOException;

/**
 *
 * 用户信息管理，服务
 * Created by wuxw on 2017/4/5.
 */
public interface IOaServiceSMO {


    public JSONObject service(BusinessServiceDataFlow businessServiceDataFlow) throws SMOException;

}
