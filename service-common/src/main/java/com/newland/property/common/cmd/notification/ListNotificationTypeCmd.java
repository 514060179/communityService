package com.newland.property.common.cmd.notification;

import com.alibaba.fastjson.JSONObject;
import com.newland.property.core.annotation.NewlandPropertyCmd;
import com.newland.property.core.context.ICmdDataFlowContext;
import com.newland.property.core.event.cmd.Cmd;
import com.newland.property.core.event.cmd.CmdEvent;
import com.newland.property.doc.annotation.*;
import com.newland.property.dto.notification.NotificationTypeDto;
import com.newland.property.intf.jpush.INotificationInnerServiceSMO;
import com.newland.property.utils.exception.CmdException;
import com.newland.property.vo.ResultVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import java.text.ParseException;
import java.util.List;

@NewlandPropertyCmdDoc(title = "获取消息类型",
        description = "业主端App可通过此接口获取到消息类型<br/>",
        httpMethod = "GET",
        url = "http://{ip}:{port}/app/notification.listNotificationType",
        resource = "commonDoc",
        author = "Moonny",
        serviceCode = "notification.listNotificationType",
        seq = 30
)

@NewlandPropertyParamsDoc(
        headers = {
                @NewlandPropertyHeaderDoc(name="APP-ID",defaultValue = "通过dev账户分配应用",description = "应用APP-ID"),
                @NewlandPropertyHeaderDoc(name="TRANSACTION-ID",defaultValue = "uuid",description = "交易流水号"),
                @NewlandPropertyHeaderDoc(name="REQ-TIME",defaultValue = "20220917120915",description = "请求时间 YYYYMMDDhhmmss"),
                @NewlandPropertyHeaderDoc(name="USER-ID",defaultValue = "-1",description = "调用用户ID 一般写-1"),
        },
        params = {
                @NewlandPropertyParamDoc(name = "communityId", remark = "小区id")
        })

@NewlandPropertyResponseDoc(
        params = {
                @NewlandPropertyParamDoc(name = "code", type = "int", length = 11, defaultValue = "0", remark = "返回编号，0 成功 其他失败"),
                @NewlandPropertyParamDoc(name = "msg", length = 250, defaultValue = "成功", remark = "描述"),
                @NewlandPropertyParamDoc(name = "data", type = "Object", remark = "有效数据"),
                @NewlandPropertyParamDoc(parentNodeName = "data",name = "id", remark = "id"),
                @NewlandPropertyParamDoc(parentNodeName = "data",name = "name", remark = "类型名称"),
                @NewlandPropertyParamDoc(parentNodeName = "data",name = "icon", remark = "类型icon"),
                @NewlandPropertyParamDoc(parentNodeName = "data",name = "unreadCount", remark = "未读数")
        }
)

@NewlandPropertyExampleDoc(
        reqBody = "http://{ip}:{port}/app/app.getAppVersion?communityId={communityId}",
        resBody="{\n" +
                "    \"code\": 0,\n" +
                "    \"data\": [\n" +
                "        {\n" +
                "            \"icon\": \"\",\n" +
                "            \"id\": 1,\n" +
                "            \"isDel\": 0,\n" +
                "            \"isHome\": 0,\n" +
                "            \"name\": \"全部\",\n" +
                "            \"page\": -1,\n" +
                "            \"records\": 0,\n" +
                "            \"row\": 0,\n" +
                "            \"sortIndex\": 0,\n" +
                "            \"total\": 0,\n" +
                "            \"type\": -1\n" +
                "        },\n" +
                "        {\n" +
                "            \"icon\": \"\",\n" +
                "            \"id\": 2,\n" +
                "            \"isDel\": 0,\n" +
                "            \"isHome\": 0,\n" +
                "            \"name\": \"業主通知\",\n" +
                "            \"page\": -1,\n" +
                "            \"records\": 0,\n" +
                "            \"row\": 0,\n" +
                "            \"sortIndex\": 0,\n" +
                "            \"total\": 0,\n" +
                "            \"type\": 0\n" +
                "        },\n" +
                "        {\n" +
                "            \"icon\": \"\",\n" +
                "            \"id\": 3,\n" +
                "            \"isDel\": 0,\n" +
                "            \"isHome\": 0,\n" +
                "            \"name\": \"投訴反饋\",\n" +
                "            \"page\": -1,\n" +
                "            \"records\": 0,\n" +
                "            \"row\": 0,\n" +
                "            \"sortIndex\": 0,\n" +
                "            \"total\": 0,\n" +
                "            \"type\": 1\n" +
                "        }\n" +
                "    ],\n" +
                "    \"msg\": \"成功\",\n" +
                "    \"page\": 0,\n" +
                "    \"records\": 1,\n" +
                "    \"rows\": 0,\n" +
                "    \"total\": 3\n" +
                "}"
)

/**
 * @author Moonny
 */
@NewlandPropertyCmd(serviceCode = "notification.listNotificationType")
public class ListNotificationTypeCmd extends Cmd {

    @Autowired
    private INotificationInnerServiceSMO notificationInnerServiceSMOImpl;

    @Override
    public void validate(CmdEvent event, ICmdDataFlowContext context, JSONObject reqJson) throws CmdException, ParseException {

    }

    @Override
    public void doCmd(CmdEvent event, ICmdDataFlowContext context, JSONObject reqJson) throws CmdException, ParseException {
        List<NotificationTypeDto> notificationTypes = notificationInnerServiceSMOImpl.getNotificationTypes();

        int count = notificationTypes.size();

        if(!reqJson.containsKey("row")){
            reqJson.put("row", 100);
        }

        ResultVo resultVo = new ResultVo((int) Math.ceil((double) count / (double) reqJson.getInteger("row")), count, notificationTypes);

        ResponseEntity<String> responseEntity = new ResponseEntity<String>(resultVo.toString(), HttpStatus.OK);

        context.setResponseEntity(responseEntity);
    }
}
