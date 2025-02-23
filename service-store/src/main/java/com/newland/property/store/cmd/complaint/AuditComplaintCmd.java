package com.newland.property.store.cmd.complaint;

import com.alibaba.fastjson.JSONObject;
import com.newland.property.core.annotation.NewlandPropertyCmd;
import com.newland.property.core.context.CmdContextUtils;
import com.newland.property.core.context.ICmdDataFlowContext;
import com.newland.property.core.event.cmd.Cmd;
import com.newland.property.core.event.cmd.CmdEvent;
import com.newland.property.core.factory.GenerateCodeFactory;
import com.newland.property.dto.complaint.ComplaintDto;
import com.newland.property.dto.complaintEvent.ComplaintEventDto;
import com.newland.property.dto.msg.JPushMessageDto;
import com.newland.property.dto.user.UserDto;
import com.newland.property.intf.store.IComplaintEventV1InnerServiceSMO;
import com.newland.property.intf.store.IComplaintV1InnerServiceSMO;
import com.newland.property.intf.user.IUserNotificationInnerServiceSMO;
import com.newland.property.intf.user.IUserV1InnerServiceSMO;
import com.newland.property.core.jpush.JPushMessageQueue;
import com.newland.property.po.complaint.ComplaintPo;
import com.newland.property.po.complaintEvent.ComplaintEventPo;
import com.newland.property.po.user.UserNotificationPo;
import com.newland.property.utils.exception.CmdException;
import com.newland.property.utils.util.Assert;
import com.newland.property.vo.ResultVo;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

@NewlandPropertyCmd(serviceCode = "complaint.auditComplaint")
public class AuditComplaintCmd extends Cmd {

    @Autowired
    private IUserV1InnerServiceSMO userV1InnerServiceSMOImpl;


    @Autowired
    private IComplaintEventV1InnerServiceSMO complaintEventV1InnerServiceSMOImpl;

    @Autowired
    private IComplaintV1InnerServiceSMO complaintV1InnerServiceSMOImpl;

    @Autowired
    private IUserNotificationInnerServiceSMO userNotificationInnerServiceSMOImpl;

    @Override
    public void validate(CmdEvent event, ICmdDataFlowContext context, JSONObject reqJson) throws CmdException {
        Assert.hasKeyAndValue(reqJson, "complaintId", "投诉ID不能为空");
        Assert.hasKeyAndValue(reqJson, "communityId", "必填，请填写小区信息");
        Assert.hasKeyAndValue(reqJson, "context", "必填，请填写批注");
    }

    @Override
    public void doCmd(CmdEvent event, ICmdDataFlowContext context, JSONObject reqJson) throws CmdException {

        String userId = CmdContextUtils.getUserId(context);

        UserDto userDto = new UserDto();
        userDto.setUserId(userId);
        List<UserDto> userDtos = userV1InnerServiceSMOImpl.queryUsers(userDto);

        Assert.listOnlyOne(userDtos, "用户未登录");

        ComplaintDto complaintDto = new ComplaintDto();
        complaintDto.setComplaintId(reqJson.getString("complaintId"));
        complaintDto.setCommunityId(reqJson.getString("communityId"));

        List<ComplaintDto> complaintDtos = complaintV1InnerServiceSMOImpl.queryComplaints(complaintDto);
        Assert.listOnlyOne(complaintDtos, "未存在或存在多条投诉单");

        // todo 修改投诉状态
        ComplaintPo complaintPo = new ComplaintPo();
        complaintPo.setComplaintId(complaintDtos.get(0).getComplaintId());
        complaintPo.setState(ComplaintDto.STATE_FINISH);
        complaintV1InnerServiceSMOImpl.updateComplaint(complaintPo);

        ComplaintEventPo complaintEventPo = new ComplaintEventPo();
        complaintEventPo.setEventId(GenerateCodeFactory.getGeneratorId("11"));
        complaintEventPo.setCreateUserId(userDtos.get(0).getUserId());
        complaintEventPo.setCreateUserName(userDtos.get(0).getName());
        complaintEventPo.setComplaintId(complaintDtos.get(0).getComplaintId());
        complaintEventPo.setRemark(reqJson.getString("context"));

        complaintEventPo.setEventType(ComplaintEventDto.EVENT_TYPE_DO);
        complaintEventPo.setCommunityId(complaintDtos.get(0).getCommunityId());

        complaintEventV1InnerServiceSMOImpl.saveComplaintEvent(complaintEventPo);

        // TODO 投诉建议完成发推送-jpush
        // appPage: /pages/complaint/complaintList  // 已完成
        // appPage: /pages/complaint/appraiseComplaint?complaintId=xxxx&communityId=xxxx   // 待评价

        JPushMessageDto messageDto = new JPushMessageDto();
        messageDto.setCommunityId(complaintDtos.get(0).getCommunityId());
        messageDto.setUserId(complaintDtos.get(0).getStartUserId());
        messageDto.setPageUrl("/pages/complaint/complaintList");
        messageDto.setTitle("投诉建议");
        messageDto.setContent("您的投诉建议已完成");
        messageDto.setPageType("appPage");
        JPushMessageQueue.addMsg(messageDto);

        UserNotificationPo userNotificationPo = new UserNotificationPo();
        userNotificationPo.setCommunityId(complaintDtos.get(0).getCommunityId());
        userNotificationPo.setUserId(userId);
        userNotificationPo.setNotificationId(0);
        userNotificationPo.setName("投诉建议");
        userNotificationPo.setTitle("投诉建议");
        userNotificationPo.setContent("您的投诉建议已完成");
        userNotificationPo.setPageType(0);
        userNotificationPo.setAppPageUrl("/pages/complaint/complaintList");
        userNotificationPo.setH5PageUrl("");
        userNotificationPo.setBgImage("");
        userNotificationPo.setType(2);
        userNotificationInnerServiceSMOImpl.saveUserNotification(userNotificationPo);

        String pageUrl = "/pages/complaint/appraiseComplaint?complaintId=" + complaintDtos.get(0).getComplaintId() +
                "&communityId=" + complaintDtos.get(0).getCommunityId();

        JPushMessageDto messageDto1 = new JPushMessageDto();
        messageDto1.setCommunityId(complaintDtos.get(0).getCommunityId());
        messageDto1.setUserId(complaintDtos.get(0).getStartUserId());
        messageDto1.setPageUrl(pageUrl);
        messageDto1.setTitle("投诉建议");
        messageDto1.setContent("您的投诉建议已完成，请对我们服务进行评价");
        messageDto1.setPageType("appPage");
        JPushMessageQueue.addMsg(messageDto1);

        userNotificationPo = new UserNotificationPo();
        userNotificationPo.setCommunityId(complaintDtos.get(0).getCommunityId());
        userNotificationPo.setUserId(userId);
        userNotificationPo.setNotificationId(0);
        userNotificationPo.setName("投诉建议");
        userNotificationPo.setTitle("投诉建议");
        userNotificationPo.setContent("您的投诉建议已完成，请对我们服务进行评价");
        userNotificationPo.setPageType(0);
        userNotificationPo.setAppPageUrl(pageUrl);
        userNotificationPo.setH5PageUrl("");
        userNotificationPo.setBgImage("");
        userNotificationPo.setType(2);
        userNotificationInnerServiceSMOImpl.saveUserNotification(userNotificationPo);

        context.setResponseEntity(ResultVo.success());
    }
}
