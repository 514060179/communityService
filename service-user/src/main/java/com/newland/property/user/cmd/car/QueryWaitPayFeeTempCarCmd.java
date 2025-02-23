package com.newland.property.user.cmd.car;

import com.alibaba.fastjson.JSONObject;
import com.newland.property.core.annotation.NewlandPropertyCmd;
import com.newland.property.core.context.ICmdDataFlowContext;
import com.newland.property.core.event.cmd.Cmd;
import com.newland.property.core.event.cmd.CmdEvent;
import com.newland.property.dto.machine.CarInoutDetailDto;
import com.newland.property.dto.machine.CarInoutDto;
import com.newland.property.dto.owner.OwnerCarDto;
import com.newland.property.dto.owner.OwnerCarOpenUserDto;
import com.newland.property.intf.common.ICarInoutDetailInnerServiceSMO;
import com.newland.property.intf.user.IOwnerCarOpenUserV1InnerServiceSMO;
import com.newland.property.utils.exception.CmdException;
import com.newland.property.utils.util.Assert;
import com.newland.property.utils.util.StringUtil;
import com.newland.property.vo.ResultVo;
import org.springframework.beans.factory.annotation.Autowired;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

/**
 * 查询待缴费临时车
 */
@NewlandPropertyCmd(serviceCode = "car.queryWaitPayFeeTempCar")
public class QueryWaitPayFeeTempCarCmd extends Cmd {

    @Autowired
    private IOwnerCarOpenUserV1InnerServiceSMO ownerCarOpenUserV1InnerServiceSMOImpl;

    @Autowired
    private ICarInoutDetailInnerServiceSMO carInoutDetailInnerServiceSMOImpl;

    @Override
    public void validate(CmdEvent event, ICmdDataFlowContext context, JSONObject reqJson) throws CmdException {
        Assert.hasKeyAndValue(reqJson, "openId", "未包含开放用户");
    }

    @Override
    public void doCmd(CmdEvent event, ICmdDataFlowContext context, JSONObject reqJson) throws CmdException, ParseException {

        List<OwnerCarDto> ownerCarDtos = new ArrayList<>();
        OwnerCarDto ownerCarDto = null;
        if (!StringUtil.jsonHasKayAndValue(reqJson, "machineId")) {
            OwnerCarOpenUserDto ownerCarOpenUserDto = new OwnerCarOpenUserDto();
            ownerCarOpenUserDto.setOpenId(reqJson.getString("openId"));
            List<OwnerCarOpenUserDto> ownerCarOpenUserDtos = ownerCarOpenUserV1InnerServiceSMOImpl.queryOwnerCarOpenUsers(ownerCarOpenUserDto);
            for (OwnerCarOpenUserDto tmpOwnerCarOpenUserDto : ownerCarOpenUserDtos) {
                ownerCarDto = new OwnerCarDto();
                ownerCarDto.setCarNum(tmpOwnerCarOpenUserDto.getCarNum());
                ownerCarDtos.add(ownerCarDto);
            }
            context.setResponseEntity(ResultVo.createResponseEntity(ownerCarDtos));
            return;
        }


        //查询是否有车牌号

        CarInoutDetailDto carInoutDetailDto = new CarInoutDetailDto();
        carInoutDetailDto.setMachineId(reqJson.getString("machineId"));
        carInoutDetailDto.setState(CarInoutDto.STATE_IN_FAIL);
        carInoutDetailDto.setCarInout(CarInoutDetailDto.CAR_INOUT_OUT);
        carInoutDetailDto.setPage(1);
        carInoutDetailDto.setRow(1);
        List<CarInoutDetailDto> carInoutDetailDtos = carInoutDetailInnerServiceSMOImpl.queryCarInoutDetails(carInoutDetailDto);

        if (carInoutDetailDtos == null || carInoutDetailDtos.size() < 1) {
            context.setResponseEntity(ResultVo.createResponseEntity(ownerCarDtos));
            return;
        }

        for (CarInoutDetailDto tmpCarInoutDetailDto : carInoutDetailDtos) {
            ownerCarDto = new OwnerCarDto();
            ownerCarDto.setCarNum(tmpCarInoutDetailDto.getCarNum());
            ownerCarDtos.add(ownerCarDto);
        }
        context.setResponseEntity(ResultVo.createResponseEntity(ownerCarDtos));

    }
}
