package com.newland.property.community.cmd.ownerRepair;

import com.alibaba.fastjson.JSONObject;
import com.newland.property.core.annotation.NewlandPropertyCmd;
import com.newland.property.core.context.CmdContextUtils;
import com.newland.property.core.context.ICmdDataFlowContext;
import com.newland.property.core.event.cmd.Cmd;
import com.newland.property.core.event.cmd.CmdEvent;
import com.newland.property.core.factory.GenerateCodeFactory;
import com.newland.property.dto.repair.RepairDto;
import com.newland.property.dto.repair.RepairUserDto;
import com.newland.property.dto.user.UserDto;
import com.newland.property.intf.community.IRepairInnerServiceSMO;
import com.newland.property.intf.community.IRepairPoolV1InnerServiceSMO;
import com.newland.property.intf.community.IRepairUserInnerServiceSMO;
import com.newland.property.intf.community.IRepairUserV1InnerServiceSMO;
import com.newland.property.intf.user.IUserV1InnerServiceSMO;
import com.newland.property.po.owner.RepairPoolPo;
import com.newland.property.po.owner.RepairUserPo;
import com.newland.property.utils.exception.CmdException;
import com.newland.property.utils.util.Assert;
import com.newland.property.utils.util.DateUtil;
import com.newland.property.utils.util.ListUtil;
import org.springframework.beans.factory.annotation.Autowired;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

@NewlandPropertyCmd(serviceCode = "ownerRepair.repairStart")
public class RepairStartCmd extends Cmd {

    @Autowired
    private IRepairInnerServiceSMO repairInnerServiceSMOImpl;

    @Autowired
    private IRepairUserInnerServiceSMO repairUserInnerServiceSMOImpl;

    @Autowired
    private IRepairPoolV1InnerServiceSMO repairPoolV1InnerServiceSMOImpl;

    @Autowired
    private IRepairUserV1InnerServiceSMO repairUserV1InnerServiceSMOImpl;

    @Autowired
    private IUserV1InnerServiceSMO userV1InnerServiceSMOImpl;

    @Override
    public void validate(CmdEvent event, ICmdDataFlowContext context, JSONObject reqJson) throws CmdException {
        Assert.hasKeyAndValue(reqJson, "repairId", "未包含报修单信息");
        Assert.hasKeyAndValue(reqJson, "communityId", "未包含小区信息");
    }

    @Override
    public void doCmd(CmdEvent event, ICmdDataFlowContext context, JSONObject reqJson) throws CmdException, ParseException {

        String userId = CmdContextUtils.getUserId(context);
        UserDto userDto = new UserDto();
        userDto.setUserId(userId);
        List<UserDto> userDtos = userV1InnerServiceSMOImpl.queryUsers(userDto);

        Assert.listOnlyOne(userDtos, "用户未登录");

        RepairDto repairDto = new RepairDto();
        repairDto.setRepairId(reqJson.getString("repairId"));
        //查询报修信息
        List<RepairDto> repairDtos = repairInnerServiceSMOImpl.queryRepairs(repairDto);
        Assert.listOnlyOne(repairDtos, "查询报修信息错误！");
        String state = repairDtos.get(0).getState();
        int flag = 0;
        if (!RepairDto.STATE_STOP.equals(state)) {
            return;
        }
        //状态是暂停状态
        RepairPoolPo repairPoolPo = new RepairPoolPo();
        repairPoolPo.setRepairId(reqJson.getString("repairId"));
        repairPoolPo.setState(RepairDto.STATE_TAKING); //状态变为接单状态
        flag = repairPoolV1InnerServiceSMOImpl.updateRepairPoolNew(repairPoolPo);
        if (flag < 1) {
            throw new CmdException("修改工单失败");
        }
        RepairUserDto repairUserDto = new RepairUserDto();
        repairUserDto.setRepairId(reqJson.getString("repairId"));
        repairUserDto.setState(RepairUserDto.STATE_STOP); //暂停状态
        //查询报修派单
        List<RepairUserDto> repairUserDtos = repairUserInnerServiceSMOImpl.queryRepairUsers(repairUserDto);
        if (ListUtil.isNull(repairUserDtos)) {
            throw new IllegalArgumentException("启动报修单错误！");
        }
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

        RepairUserPo repairUserPo = new RepairUserPo();
        repairUserPo.setRuId(GenerateCodeFactory.getGeneratorId(GenerateCodeFactory.CODE_PREFIX_ruId));
        repairUserPo.setRepairId(reqJson.getString("repairId"));
        repairUserPo.setbId("-1");
        repairUserPo.setCommunityId(reqJson.getString("communityId"));
        repairUserPo.setCreateTime(DateUtil.getNow(DateUtil.DATE_FORMATE_STRING_A));
        repairUserPo.setState(RepairUserDto.STATE_START);
        repairUserPo.setContext("启动报修流程");
        repairUserPo.setStaffId(userDtos.get(0).getUserId()); //当前处理员工id
        repairUserPo.setStaffName(userDtos.get(0).getName()); //当前处理员工名称

        repairUserPo.setPreStaffId(repairUserDtos.get(0).getStaffId());
        repairUserPo.setPreStaffName(repairUserDtos.get(0).getStaffName());
        repairUserPo.setStartTime(simpleDateFormat.format(new Date()));
        repairUserPo.setEndTime(simpleDateFormat.format(new Date()));
        repairUserPo.setPreRuId(repairUserDtos.get(0).getRuId());

        repairUserPo.setRepairEvent(RepairUserDto.REPAIR_EVENT_AUDIT_USER);
        flag = repairUserV1InnerServiceSMOImpl.saveRepairUserNew(repairUserPo);
        if (flag < 1) {
            throw new CmdException("添加报修工单信息失败！");
        }

    }
}
