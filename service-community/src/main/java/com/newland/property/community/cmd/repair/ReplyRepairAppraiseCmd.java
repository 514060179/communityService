package com.newland.property.community.cmd.repair;

import com.alibaba.fastjson.JSONObject;
import com.newland.property.core.annotation.NewlandPropertyCmd;
import com.newland.property.core.context.CmdContextUtils;
import com.newland.property.core.context.ICmdDataFlowContext;
import com.newland.property.core.event.cmd.Cmd;
import com.newland.property.core.event.cmd.CmdEvent;
import com.newland.property.core.factory.GenerateCodeFactory;
import com.newland.property.dto.repair.RepairUserDto;
import com.newland.property.dto.user.UserDto;
import com.newland.property.intf.community.IRepairUserV1InnerServiceSMO;
import com.newland.property.intf.user.IUserV1InnerServiceSMO;
import com.newland.property.po.owner.RepairUserPo;
import com.newland.property.utils.exception.CmdException;
import com.newland.property.utils.util.Assert;
import com.newland.property.utils.util.DateUtil;
import org.springframework.beans.factory.annotation.Autowired;

import java.text.ParseException;
import java.util.List;

/**
 * 报修评论回复
 */
@NewlandPropertyCmd(serviceCode = "repair.replyRepairAppraise")
public class ReplyRepairAppraiseCmd extends Cmd {

    @Autowired
    private IRepairUserV1InnerServiceSMO repairUserV1InnerServiceSMOImpl;

    @Autowired
    private IUserV1InnerServiceSMO userV1InnerServiceSMOImpl;


    @Override
    public void validate(CmdEvent event, ICmdDataFlowContext context, JSONObject reqJson) throws CmdException, ParseException {
        Assert.hasKeyAndValue(reqJson, "communityId", "未包含小区ID");
        Assert.hasKeyAndValue(reqJson, "replyContext", "未包含回复内容");
        Assert.hasKeyAndValue(reqJson, "ruId", "未包含评论ID");
    }

    @Override
    public void doCmd(CmdEvent event, ICmdDataFlowContext context, JSONObject reqJson) throws CmdException, ParseException {

        String userId = CmdContextUtils.getUserId(context);

        UserDto userDto = new UserDto();
        userDto.setUserId(userId);
        List<UserDto> userDtos = userV1InnerServiceSMOImpl.queryUsers(userDto);

        Assert.listOnlyOne(userDtos, "用户未登录");

        RepairUserDto repairUserDto = new RepairUserDto();
        repairUserDto.setRuId(reqJson.getString("ruId"));
        List<RepairUserDto> repairUserDtos = repairUserV1InnerServiceSMOImpl.queryRepairUserNews(repairUserDto);

        Assert.listOnlyOne(repairUserDtos, "评论不存在");

        RepairUserPo repairUserPo = new RepairUserPo();
        repairUserPo.setRuId(GenerateCodeFactory.getGeneratorId("11"));
        repairUserPo.setRepairId(repairUserDtos.get(0).getRepairId());
        repairUserPo.setUserId(userDtos.get(0).getUserId());
        repairUserPo.setCommunityId(repairUserDtos.get(0).getCommunityId());
        repairUserPo.setState(RepairUserDto.STATE_REPLY_APPRAISE);
        repairUserPo.setContext("回复" + repairUserDtos.get(0).getStaffName() + ":" + reqJson.getString("replyContext"));
        repairUserPo.setStaffId(userId);
        repairUserPo.setStaffName(userDtos.get(0).getName());
        repairUserPo.setPreStaffId(repairUserDtos.get(0).getStaffId());
        repairUserPo.setPreStaffName(repairUserDtos.get(0).getStaffName());
        repairUserPo.setStartTime(DateUtil.getFormatTimeStringA(repairUserDtos.get(0).getEndTime()));
        repairUserPo.setEndTime(DateUtil.getNow(DateUtil.DATE_FORMATE_STRING_A));
        repairUserPo.setRepairEvent(repairUserDtos.get(0).getRepairEvent());
        repairUserPo.setPreRuId(reqJson.getString("ruId"));
        repairUserV1InnerServiceSMOImpl.saveRepairUserNew(repairUserPo);
    }
}
