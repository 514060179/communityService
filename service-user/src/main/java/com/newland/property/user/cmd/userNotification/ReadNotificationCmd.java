package com.newland.property.user.cmd.userNotification;

import com.alibaba.fastjson.JSONObject;
import com.newland.property.core.annotation.NewlandPropertyCmd;
import com.newland.property.core.context.ICmdDataFlowContext;
import com.newland.property.core.event.cmd.Cmd;
import com.newland.property.core.event.cmd.CmdEvent;
import com.newland.property.doc.annotation.*;
import com.newland.property.dto.user.UserNotificationDto;
import com.newland.property.intf.user.IUserNotificationInnerServiceSMO;
import com.newland.property.utils.exception.CmdException;
import com.newland.property.utils.util.StringUtil;
import com.newland.property.vo.ResultVo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import java.text.ParseException;

@NewlandPropertyCmdDoc(title = "消息设置为已读",
        description = "业主端App可通过此接口将消息设置为已读状态<br/>",
        httpMethod = "POST",
        url = "http://{ip}:{port}/app/userNotification.readNotification",
        resource = "commonDoc",
        author = "Moonny",
        serviceCode = "userNotification.readNotification",
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
                @NewlandPropertyParamDoc(name = "communityId", remark = "小区id"),
                @NewlandPropertyParamDoc(name = "userId", remark = "用户id"),
                @NewlandPropertyParamDoc(name = "type", remark = "分类id, 设置分类下消息为已读，这时id不需要传"),
                @NewlandPropertyParamDoc(name = "id", remark = "消息id，单条消息设置为已读"),
        })

@NewlandPropertyResponseDoc(
        params = {
                @NewlandPropertyParamDoc(name = "code", type = "int", length = 11, defaultValue = "0", remark = "返回编号，0 成功 其他失败"),
                @NewlandPropertyParamDoc(name = "msg", length = 250, defaultValue = "成功", remark = "描述"),
                @NewlandPropertyParamDoc(name = "data", type = "Object", remark = "有效数据")
        }
)

@NewlandPropertyExampleDoc(
        reqBody = "http://{ip}:{port}/app/userNotification.readNotification \n\n" +
                "{\n" +
                "    \"communityId\": \"2024051856730008\",\n" +
                "    \"userId\": \"302024051890320132\",\n" +
                "    \"id\": 1,\n" +
                "    \"type\": -1\n" +
                "}",
        resBody="{\n" +
                "    \"code\": 0,\n" +
                "    \"msg\": \"成功\",\n" +
                "    \"page\": 0,\n" +
                "    \"records\": 0,\n" +
                "    \"rows\": 0,\n" +
                "    \"total\": 0\n" +
                "}"
)

/**
 * @author Moonny
 */
@NewlandPropertyCmd(serviceCode = "userNotification.readNotification")
public class ReadNotificationCmd extends Cmd {

    private final static Logger logger = LoggerFactory.getLogger(GetNotificationListCmd.class);

    @Autowired
    private IUserNotificationInnerServiceSMO userNotificationInnerServiceSMOImpl;

    @Override
    public void validate(CmdEvent event, ICmdDataFlowContext context, JSONObject reqJson) throws CmdException, ParseException {

    }

    @Override
    public void doCmd(CmdEvent event, ICmdDataFlowContext context, JSONObject reqJson) throws CmdException, ParseException {
        UserNotificationDto userNotificationDto = new UserNotificationDto();
        String type = "-1";
        if (reqJson.containsKey("type")) {
            type = StringUtil.trim(reqJson.get("type"));
        }
        userNotificationDto.setType(Integer.parseInt(type));

        String id = "0";
        if (reqJson.containsKey("id")) {
            id = StringUtil.trim(reqJson.get("id"));
            if (!StringUtil.isEmpty(id)) {
                userNotificationDto.setId(Integer.parseInt(id));
            }
        }

        userNotificationDto.setUserId(reqJson.getString("userId"));

        if (!"0".equals(id) && !StringUtil.isEmpty(id)) {
            userNotificationInnerServiceSMOImpl.readUserNotification(userNotificationDto);
        } else {
            userNotificationInnerServiceSMOImpl.readTypeNotification(userNotificationDto);
        }

        context.setResponseEntity(ResultVo.success());
    }
}
