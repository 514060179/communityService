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
import com.newland.property.utils.util.Assert;
import com.newland.property.utils.util.BeanConvertUtil;
import com.newland.property.vo.ResultVo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

@NewlandPropertyCmdDoc(title = "获取消息列表",
        description = "业主端App可通过此接口获取到消息列表<br/>",
        httpMethod = "GET",
        url = "http://{ip}:{port}/app/userNotification.getNotificationList",
        resource = "commonDoc",
        author = "Moonny",
        serviceCode = "userNotification.getNotificationList",
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
                @NewlandPropertyParamDoc(name = "type", remark = "分类"),
                @NewlandPropertyParamDoc(name = "page", remark = "页码，从1开始"),
                @NewlandPropertyParamDoc(name = "row", remark = "页大小"),
        })

@NewlandPropertyResponseDoc(
        params = {
                @NewlandPropertyParamDoc(name = "code", type = "int", length = 11, defaultValue = "0", remark = "返回编号，0 成功 其他失败"),
                @NewlandPropertyParamDoc(name = "msg", length = 250, defaultValue = "成功", remark = "描述"),
                @NewlandPropertyParamDoc(name = "data", type = "Object", remark = "有效数据"),
                @NewlandPropertyParamDoc(parentNodeName = "data",name = "id", remark = "id"),
                @NewlandPropertyParamDoc(parentNodeName = "data",name = "name", remark = "类型名称"),
                @NewlandPropertyParamDoc(parentNodeName = "data",name = "title", remark = "类型icon"),
                @NewlandPropertyParamDoc(parentNodeName = "data",name = "content", remark = "未读数"),
                @NewlandPropertyParamDoc(parentNodeName = "data",name = "appPageUrl", remark = "类型名称"),
                @NewlandPropertyParamDoc(parentNodeName = "data",name = "h5PageUrl", remark = "类型icon"),
                @NewlandPropertyParamDoc(parentNodeName = "data",name = "isRead", remark = "未读数"),
                @NewlandPropertyParamDoc(parentNodeName = "data",name = "readTime", remark = "读取时间"),
                @NewlandPropertyParamDoc(parentNodeName = "data",name = "sentTime", remark = "发出时间"),
                @NewlandPropertyParamDoc(parentNodeName = "data",name = "type", remark = "未读数")
        }
)

@NewlandPropertyExampleDoc(
        reqBody = "http://{ip}:{port}/app/userNotification" +
                ".getNotificationList?communityId={communityId}&userId={userId}&type={type}&page={page}&row={row}",
        resBody="{\n" +
                "    \"code\": 0,\n" +
                "    \"data\": [\n" +
                "        {\n" +
                "            \"appPageUrl\": \"/pages/family/familyList\",\n" +
                "            \"communityId\": \"2024051856730008\",\n" +
                "            \"content\": \"測試消息內容\",\n" +
                "            \"createTime\": \"2024-08-28 14:47:26\",\n" +
                "            \"h5PageUrl\": \" \",\n" +
                "            \"id\": 1,\n" +
                "            \"isDel\": 0,\n" +
                "            \"isRead\": 1,\n" +
                "            \"name\": \"測試\",\n" +
                "            \"notificationId\": 0,\n" +
                "            \"page\": -1,\n" +
                "            \"pageType\": 1,\n" +
                "            \"readTime\": \"2024-08-28 15:14:59\",\n" +
                "            \"records\": 0,\n" +
                "            \"row\": 0,\n" +
                "            \"sentTime\": \"2024-08-28 14:47:26\",\n" +
                "            \"title\": \"測試消息\",\n" +
                "            \"total\": 0,\n" +
                "            \"type\": 0,\n" +
                "            \"userId\": \"302024051890320132\"\n" +
                "        }\n" +
                "    ],\n" +
                "    \"msg\": \"成功\",\n" +
                "    \"page\": 0,\n" +
                "    \"records\": 1,\n" +
                "    \"rows\": 0,\n" +
                "    \"total\": 1\n" +
                "}"
)

/**
 * @author Administrator
 */
@NewlandPropertyCmd(serviceCode = "userNotification.getNotificationList")
public class GetNotificationListCmd extends Cmd {

    private final static Logger logger = LoggerFactory.getLogger(GetNotificationListCmd.class);

    @Autowired
    private IUserNotificationInnerServiceSMO userNotificationInnerServiceSMOImpl;

    @Override
    public void validate(CmdEvent event, ICmdDataFlowContext context, JSONObject reqJson) throws CmdException, ParseException {
        Assert.hasKeyAndValue(reqJson, "communityId", "communityId不能为空");
        Assert.hasKeyAndValue(reqJson, "userId", "userId不能为空");
        super.validatePageInfo(reqJson);
    }

    @Override
    public void doCmd(CmdEvent event, ICmdDataFlowContext context, JSONObject reqJson) throws CmdException, ParseException {
        UserNotificationDto userNotificationDto = BeanConvertUtil.covertBean(reqJson, UserNotificationDto.class);
        if(!reqJson.containsKey("type")) {
            userNotificationDto.setType(-1);
        }

        int count = userNotificationInnerServiceSMOImpl.queryUserNotificationsCount(userNotificationDto);

        List<UserNotificationDto> userNotificationDtos = null;

        if (count > 0) {
            userNotificationDtos = userNotificationInnerServiceSMOImpl.queryUserNotifications(userNotificationDto);
        } else {
            userNotificationDtos = new ArrayList<>();
        }

        ResultVo resultVo = new ResultVo((int) Math.ceil((double) count / (double) reqJson.getInteger("row")), count, userNotificationDtos);

        ResponseEntity<String> responseEntity = new ResponseEntity<String>(resultVo.toString(), HttpStatus.OK);

        context.setResponseEntity(responseEntity);
    }
}
