package com.newland.property.api.bmo.msg;

import com.alibaba.fastjson.JSONObject;
import com.newland.property.api.bmo.IApiBaseBMO;
import com.newland.property.core.context.DataFlowContext;

/**
 * @ClassName IMsgBMO
 * @Description TODO
 * @Author wuxw
 * @Date 2020/3/9 22:59
 * @Version 1.0
 * add by wuxw 2020/3/9
 **/
public interface IMsgBMO extends IApiBaseBMO {
    public void addReadMsg(JSONObject paramInJson, DataFlowContext context);
}
