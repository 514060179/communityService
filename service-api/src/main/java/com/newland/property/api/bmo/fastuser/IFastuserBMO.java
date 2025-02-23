package com.newland.property.api.bmo.fastuser;

import com.alibaba.fastjson.JSONObject;
import com.newland.property.api.bmo.IApiBaseBMO;
import com.newland.property.core.context.DataFlowContext;

public interface IFastuserBMO extends IApiBaseBMO {



    /**
     * 添加活动
     * @param paramInJson
     * @param dataFlowContext
     * @return
     */
     JSONObject addFastuser(JSONObject paramInJson, DataFlowContext dataFlowContext);

}
