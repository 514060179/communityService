package com.newland.property.job.cmd.print;

import com.alibaba.fastjson.JSONObject;
import com.newland.property.core.annotation.NewlandPropertyCmd;
import com.newland.property.core.context.ICmdDataFlowContext;
import com.newland.property.core.event.cmd.Cmd;
import com.newland.property.core.event.cmd.CmdEvent;
import com.newland.property.dto.machine.MachinePrinterDto;
import com.newland.property.dto.user.UserDto;
import com.newland.property.intf.common.IMachinePrinterV1InnerServiceSMO;
import com.newland.property.intf.user.IUserV1InnerServiceSMO;
import com.newland.property.job.printer.IPrinter;
import com.newland.property.utils.exception.CmdException;
import com.newland.property.utils.factory.ApplicationContextFactory;
import com.newland.property.utils.util.Assert;
import com.newland.property.vo.ResultVo;
import org.springframework.beans.factory.annotation.Autowired;

import java.text.ParseException;
import java.util.List;

/**
 * 打印 交费明细
 */
@NewlandPropertyCmd(serviceCode = "print.printPayFeeDetail")
public class PrintPayFeeDetailCmd extends Cmd {

    @Autowired
    private IMachinePrinterV1InnerServiceSMO machinePrinterV1InnerServiceSMOImpl;

    @Autowired
    private IUserV1InnerServiceSMO userV1InnerServiceSMOImpl;

    @Override
    public void validate(CmdEvent event, ICmdDataFlowContext context, JSONObject reqJson) throws CmdException, ParseException {

        Assert.hasKeyAndValue(reqJson, "communityId", "未包含小区");
        Assert.hasKeyAndValue(reqJson, "detailId", "未包含交费明细");
        Assert.hasKeyAndValue(reqJson, "machineId", "未包含云打印机");
        Assert.hasKeyAndValue(reqJson, "quantity", "未包含打印数量");


    }

    @Override
    public void doCmd(CmdEvent event, ICmdDataFlowContext context, JSONObject reqJson) throws CmdException, ParseException {
        String userId = context.getReqHeaders().get("user-id");
        UserDto userDto = new UserDto();
        userDto.setUserId(userId);
        List<UserDto> userDtos = userV1InnerServiceSMOImpl.queryUsers(userDto);
        Assert.listOnlyOne(userDtos,"用户不存在");
        MachinePrinterDto machinePrinterDto = new MachinePrinterDto();
        machinePrinterDto.setCommunityId(reqJson.getString("communityId"));
        machinePrinterDto.setMachineId(reqJson.getString("machineId"));
        List<MachinePrinterDto> machinePrinterDtos = machinePrinterV1InnerServiceSMOImpl.queryMachinePrinters(machinePrinterDto);

        Assert.listOnlyOne(machinePrinterDtos, "云打印机不存在");

        IPrinter printer = ApplicationContextFactory.getBean(machinePrinterDtos.get(0).getImplBean(), IPrinter.class);

        if (printer == null) {
            throw new CmdException("打印机异常，未包含适配器");
        }

        ResultVo resultVo = printer.printPayFeeDetail(reqJson.getString("detailId").split(","),
                reqJson.getString("communityId"),
                reqJson.getIntValue("quantity"),
                machinePrinterDtos.get(0),userDtos.get(0).getName());

        context.setResponseEntity(ResultVo.createResponseEntity(resultVo));
    }
}
