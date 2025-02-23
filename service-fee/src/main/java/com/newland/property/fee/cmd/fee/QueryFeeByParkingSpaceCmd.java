package com.newland.property.fee.cmd.fee;

import com.alibaba.fastjson.JSONObject;
import com.newland.property.core.annotation.NewlandPropertyCmd;
import com.newland.property.core.context.ICmdDataFlowContext;
import com.newland.property.core.event.cmd.Cmd;
import com.newland.property.core.event.cmd.CmdEvent;
import com.newland.property.dto.fee.FeeDto;
import com.newland.property.dto.owner.OwnerCarDto;
import com.newland.property.dto.parking.ParkingSpaceDto;
import com.newland.property.intf.community.IParkingSpaceInnerServiceSMO;
import com.newland.property.intf.fee.IFeeInnerServiceSMO;
import com.newland.property.intf.user.IOwnerCarInnerServiceSMO;
import com.newland.property.utils.exception.CmdException;
import com.newland.property.utils.util.Assert;
import com.newland.property.utils.util.BeanConvertUtil;
import com.newland.property.vo.api.ApiParkingSpaceFeeVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.List;

@NewlandPropertyCmd(serviceCode = "fee.queryFeeByParkingSpace")
public class QueryFeeByParkingSpaceCmd extends Cmd {

    @Autowired
    private IFeeInnerServiceSMO feeInnerServiceSMOImpl;

    @Autowired
    private IParkingSpaceInnerServiceSMO parkingSpaceInnerServiceSMOImpl;


    @Autowired
    private IOwnerCarInnerServiceSMO ownerCarInnerServiceSMOImpl;

    @Override
    public void validate(CmdEvent event, ICmdDataFlowContext context, JSONObject reqJson) throws CmdException {
        Assert.jsonObjectHaveKey(reqJson, "communityId", "请求中未包含communityId信息");
        Assert.jsonObjectHaveKey(reqJson, "psId", "请求中未包含psId信息");
    }

    @Override
    public void doCmd(CmdEvent event, ICmdDataFlowContext context, JSONObject reqJson) throws CmdException {
        FeeDto feeDtoParamIn = BeanConvertUtil.covertBean(reqJson, FeeDto.class);
        feeDtoParamIn.setPayerObjId(reqJson.getString("psId"));

        List<FeeDto> feeDtos = feeInnerServiceSMOImpl.queryFees(feeDtoParamIn);
        ResponseEntity<String> responseEntity = null;
        if (feeDtos == null || feeDtos.size() == 0) {
            responseEntity = new ResponseEntity<String>("{}", HttpStatus.OK);
            context.setResponseEntity(responseEntity);
            return ;
        }

        FeeDto feeDto = feeDtos.get(0);

        ApiParkingSpaceFeeVo apiFeeVo = BeanConvertUtil.covertBean(feeDto, ApiParkingSpaceFeeVo.class);

        //停车位信息
        ParkingSpaceDto parkingSpaceDto = BeanConvertUtil.covertBean(reqJson, ParkingSpaceDto.class);
        List<ParkingSpaceDto> parkingSpaceDtos = parkingSpaceInnerServiceSMOImpl.queryParkingSpaces(parkingSpaceDto);

        Assert.listOnlyOne(parkingSpaceDtos, "未查询到或查询多条 车位信息");

        parkingSpaceDto = parkingSpaceDtos.get(0);

        BeanConvertUtil.covertBean(parkingSpaceDto, apiFeeVo);

        //查询车辆信息
        OwnerCarDto ownerCarDto = BeanConvertUtil.covertBean(reqJson, OwnerCarDto.class);

        List<OwnerCarDto> ownerCarDtos = ownerCarInnerServiceSMOImpl.queryOwnerCars(ownerCarDto);
        Assert.listOnlyOne(ownerCarDtos, "未查询到或查询多条 车辆信息");
        ownerCarDto = ownerCarDtos.get(0);

        BeanConvertUtil.covertBean(ownerCarDto, apiFeeVo);


        responseEntity = new ResponseEntity<String>(JSONObject.toJSONString(apiFeeVo), HttpStatus.OK);


        context.setResponseEntity(responseEntity);
    }
}
