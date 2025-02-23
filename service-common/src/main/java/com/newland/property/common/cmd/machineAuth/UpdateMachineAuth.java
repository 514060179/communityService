package com.newland.property.common.cmd.machineAuth;

import com.alibaba.fastjson.JSONObject;
import com.newland.property.core.annotation.NewlandPropertyCmd;
import com.newland.property.core.annotation.PropertyTransactional;
import com.newland.property.core.context.ICmdDataFlowContext;
import com.newland.property.core.event.cmd.Cmd;
import com.newland.property.core.event.cmd.CmdEvent;
import com.newland.property.core.log.LoggerFactory;
import com.newland.property.dto.machine.MachineAuthDto;
import com.newland.property.dto.user.UserDto;
import com.newland.property.intf.common.IMachineAuthInnerServiceSMO;
import com.newland.property.intf.user.IUserInnerServiceSMO;
import com.newland.property.po.machine.MachineAuthPo;
import com.newland.property.utils.exception.CmdException;
import com.newland.property.utils.util.Assert;
import com.newland.property.utils.util.BeanConvertUtil;
import com.newland.property.vo.ResultVo;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * 修改员工门禁授权
 *
 * @author fqz
 * @date 2022-10-29
 */
@NewlandPropertyCmd(serviceCode = "machineAuth.updateMachineAuth")
public class UpdateMachineAuth extends Cmd {

    @Autowired
    private IMachineAuthInnerServiceSMO machineAuthInnerServiceSMOImpl;

    @Autowired
    private IUserInnerServiceSMO userInnerServiceSMOImpl;

    private static Logger logger = LoggerFactory.getLogger(UpdateMachineAuth.class);

    @Override
    public void validate(CmdEvent event, ICmdDataFlowContext context, JSONObject reqJson) throws CmdException, ParseException {
        Assert.jsonObjectHaveKey(reqJson, "authId", "请求报文中未包含授权ID");
        Assert.jsonObjectHaveKey(reqJson, "communityId", "请求报文中未包含小区信息");
        Assert.jsonObjectHaveKey(reqJson, "startTime", "请求报文中未包含开始时间");
        Assert.jsonObjectHaveKey(reqJson, "endTime", "请求报文中未包含结束时间");
        Assert.jsonObjectHaveKey(reqJson, "personId", "请求报文中未包含员工ID");
        Assert.jsonObjectHaveKey(reqJson, "machineId", "请求报文中未包含设备ID");
        MachineAuthDto machineAuthDto = new MachineAuthDto();
        machineAuthDto.setMachineId(reqJson.getString("machineId"));
        machineAuthDto.setPersonId(reqJson.getString("personId"));
        List<MachineAuthDto> machineAuthDtos = machineAuthInnerServiceSMOImpl.queryMachineAuths(machineAuthDto);
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        if (machineAuthDtos != null && machineAuthDtos.size() > 0) {
            for (MachineAuthDto machineAuth : machineAuthDtos) {
                if (machineAuth.getAuthId().equals(reqJson.getString("authId"))) {
                    continue;
                } else {
                    //获取结束时间
                    Date endTime = simpleDateFormat.parse(machineAuth.getEndTime());
                    //开始时间
                    Date newStartTime = simpleDateFormat.parse(reqJson.getString("startTime"));
                    int i = newStartTime.compareTo(endTime);
                    if (i < 0) { //上次结束时间大于本次开始时间
                        throw new IllegalArgumentException("该时间段内该员工已经授权过该设备，无法再次进行授权！");
                    }
                }
            }
        }
    }

    @Override
    @PropertyTransactional
    public void doCmd(CmdEvent event, ICmdDataFlowContext context, JSONObject reqJson) throws CmdException, ParseException {
        MachineAuthPo machineAuthPo = BeanConvertUtil.covertBean(reqJson, MachineAuthPo.class);
        UserDto userDto = new UserDto();
        userDto.setUserId(reqJson.getString("personId"));
        List<UserDto> users = userInnerServiceSMOImpl.getUsers(userDto);
        Assert.listOnlyOne(users, "查询员工错误！");
        machineAuthPo.setPersonName(users.get(0).getName());
        int flag = machineAuthInnerServiceSMOImpl.updateMachineAuth(machineAuthPo);
        if (flag < 1) {
            throw new CmdException("修改员工门禁授权失败");
        }
        context.setResponseEntity(ResultVo.success());
    }
}
