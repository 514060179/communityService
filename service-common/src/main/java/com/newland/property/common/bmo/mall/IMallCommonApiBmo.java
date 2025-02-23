package com.newland.property.common.bmo.mall;

import com.alibaba.fastjson.JSONObject;
import com.newland.property.core.context.ICmdDataFlowContext;

public interface IMallCommonApiBmo {

    void validate(ICmdDataFlowContext context, JSONObject reqJson);

    void doCmd(ICmdDataFlowContext context, JSONObject reqJson);
}
