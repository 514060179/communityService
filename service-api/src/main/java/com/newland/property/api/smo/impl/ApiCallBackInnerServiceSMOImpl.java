package com.newland.property.api.smo.impl;

import com.alibaba.fastjson.JSONObject;
import com.newland.property.api.websocket.ParkingAreaWebsocket;
import com.newland.property.api.websocket.ParkingBoxWebsocket;
import com.newland.property.intf.api.IApiCallBackInnerServiceSMO;
import com.newland.property.utils.exception.SMOException;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ApiCallBackInnerServiceSMOImpl implements IApiCallBackInnerServiceSMO {
    @Override
    public int webSentParkingArea(@RequestBody JSONObject reqJson) {
        JSONObject param = JSONObject.parseObject(reqJson.toString());
        try {
            ParkingBoxWebsocket.sendInfo(param.toJSONString(), param.getString("extBoxId"));

        } catch (Exception e) {
            throw new SMOException(e.getMessage());
        }
        try {
            ParkingAreaWebsocket.sendInfo(param.toJSONString(), param.getString("extPaId"));
        } catch (Exception e) {
            throw new SMOException(e.getMessage());
        }
        return 1;
    }
}
