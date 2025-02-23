package com.newland.property.job.dto.carinout;

import com.alibaba.fastjson.JSONObject;
import com.newland.property.job.dto.DoorResponseHead;


/**
 * @author simon feng
 * @date 2024/6/2 14:29
 * @description 车辆出入场model
 */
public class CarInoutResponse {

    private DoorResponseHead head;

    private JSONObject body;

    public DoorResponseHead getHead() {
        return head;
    }

    public void setHead(DoorResponseHead head) {
        this.head = head;
    }

    public JSONObject getBody() {
        return body;
    }

    public void setBody(JSONObject body) {
        this.body = body;
    }
}
