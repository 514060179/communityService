package com.newland.property.tcp.cmdRequest;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

public class PersonCmdRequest {

    public static String getAddPersonCmd(int cmdNumber, JSONObject personJson) {

        JSONObject cmdJson = new JSONObject();
        cmdJson.put("version", "0.1");
        cmdJson.put("cmd", "add person");
        cmdJson.put("cmd_number", cmdNumber);
        cmdJson.put("person_count", 1);
        JSONArray arrPersons = new JSONArray();
        arrPersons.add(personJson);
        cmdJson.put("persons", arrPersons);
        String cmdJsonString = JSONObject.toJSONString(cmdJson);
        StringBuilder cmdString = CommonHeader.GetCmdHeader(cmdJsonString.length());
        cmdString.append(cmdJsonString);

        return cmdString.toString();
    }
}
