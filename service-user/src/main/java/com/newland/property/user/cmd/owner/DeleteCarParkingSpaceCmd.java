package com.newland.property.user.cmd.owner;

import com.alibaba.fastjson.JSONObject;
import com.newland.property.core.annotation.NewlandPropertyCmd;
import com.newland.property.core.context.ICmdDataFlowContext;
import com.newland.property.core.event.cmd.Cmd;
import com.newland.property.core.event.cmd.CmdEvent;
import com.newland.property.dto.fee.FeeDto;
import com.newland.property.dto.owner.OwnerCarDto;
import com.newland.property.dto.parking.ParkingAreaDto;
import com.newland.property.dto.parking.ParkingSpaceDto;
import com.newland.property.intf.community.IParkingAreaInnerServiceSMO;
import com.newland.property.intf.community.IParkingSpaceInnerServiceSMO;
import com.newland.property.intf.community.IParkingSpaceV1InnerServiceSMO;
import com.newland.property.intf.fee.IFeeInnerServiceSMO;
import com.newland.property.intf.user.IOwnerCarInnerServiceSMO;
import com.newland.property.intf.user.IOwnerCarV1InnerServiceSMO;
import com.newland.property.job.util.DoorUtil;
import com.newland.property.po.car.OwnerCarPo;
import com.newland.property.po.parking.ParkingSpacePo;
import com.newland.property.utils.exception.CmdException;
import com.newland.property.utils.util.Assert;
import com.newland.property.utils.util.BeanConvertUtil;
import com.newland.property.utils.util.StringUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@NewlandPropertyCmd(serviceCode = "owner.deleteCarParkingSpace")
public class DeleteCarParkingSpaceCmd extends Cmd {

    @Autowired
    private IOwnerCarInnerServiceSMO ownerCarInnerServiceSMOImpl;

    @Autowired
    private IFeeInnerServiceSMO feeInnerServiceSMOImpl;

    @Autowired
    private IParkingSpaceInnerServiceSMO parkingSpaceInnerServiceSMOImpl;

    @Autowired
    private IParkingSpaceV1InnerServiceSMO parkingSpaceV1InnerServiceSMOImpl;

    @Autowired
    private IOwnerCarV1InnerServiceSMO ownerCarV1InnerServiceSMOImpl;

    @Autowired
    private IParkingAreaInnerServiceSMO parkingAreaInnerServiceSMO;

    @Override
    public void validate(CmdEvent event, ICmdDataFlowContext context, JSONObject reqJson) throws CmdException {
        Assert.jsonObjectHaveKey(reqJson, "communityId", "未包含小区ID");
//        Assert.jsonObjectHaveKey(reqJson, "carId", "请求报文中未包含carId");
        Assert.hasLength(reqJson.getString("communityId"), "小区ID不能为空");

        OwnerCarDto ownerCarDto = new OwnerCarDto();
        ownerCarDto.setCarId(reqJson.getString("carId"));
        ownerCarDto.setCommunityId(reqJson.getString("communityId"));
        ownerCarDto.setStatusCd("0");
        List<OwnerCarDto> ownerCarDtos = ownerCarInnerServiceSMOImpl.queryOwnerCars(ownerCarDto);
        if (ownerCarDtos != null && ownerCarDtos.size() > 1) {
            throw new IllegalArgumentException("有多个车辆绑定此车位，请先删除车辆！");
        }

        String state = ownerCarDtos.get(0).getState();

        if (StringUtil.isEmpty(state) || state.equals(OwnerCarDto.STATE_FINISH)) {
            //throw new IllegalArgumentException("车位已经释放无需释放");
        }

//        if (ownerCarDtos.get(0).getEndTime().getTime() > DateUtil.getCurrentDate().getTime()) {
//            throw new IllegalArgumentException("车位租用还未结束不能释放");
//        }
        reqJson.put("ownerCarDto", ownerCarDtos.get(0));
    }

    @Transactional
    @Override
    public void doCmd(CmdEvent event, ICmdDataFlowContext context, JSONObject reqJson) throws CmdException {

        OwnerCarDto ownerCarDto = (OwnerCarDto) reqJson.get("ownerCarDto");
        FeeDto feeDto = new FeeDto();
        feeDto.setPayerObjId(reqJson.getString("carId"));
        feeDto.setCommunityId(reqJson.getString("communityId"));
        feeDto.setPayerObjType(FeeDto.PAYER_OBJ_TYPE_CAR);
        feeDto.setState(FeeDto.STATE_FINISH);
        List<FeeDto> feeDtoList = feeInnerServiceSMOImpl.queryFees(feeDto);
        boolean oweFee = false;
        if (feeDtoList == null || feeDtoList.size() < 1) {
            oweFee = true;
        }
        for (FeeDto tmpFeeDto : feeDtoList) {

            if (tmpFeeDto.getEndTime().getTime() <= ownerCarDto.getEndTime().getTime()) {
                oweFee = true;
                break;
            } else {
                throw new IllegalArgumentException("费用未开始！");
            }
        }

        if (OwnerCarDto.STATE_FINISH.equals(ownerCarDto.getState())) {
            oweFee = false;
        }
        OwnerCarPo ownerCarPo = new OwnerCarPo();
//        ownerCarPo.setPsId("-1");
//        ownerCarPo.setCarNum(reqJson.getString("carNum"));
        ownerCarPo.setCarId(reqJson.getString("carId"));
        ownerCarPo.setMemberId(reqJson.getString("memberId"));
        ownerCarPo.setCommunityId(reqJson.getString("communityId"));
        List<OwnerCarDto> exist = ownerCarV1InnerServiceSMOImpl.queryOwnerCars(BeanConvertUtil.covertBean(ownerCarPo, OwnerCarDto.class));
        if (exist.size() < 1) {
            throw new IllegalArgumentException("车辆不存在");
        }
        if (oweFee) {
            ownerCarPo.setState(OwnerCarDto.STATE_DELETE);
        }
        int flag = ownerCarV1InnerServiceSMOImpl.updateOwnerCar(ownerCarPo);
        if (flag < 1) {
            throw new IllegalArgumentException("修改车辆出错");
        }
        //道尔注销
        ParkingAreaDto parkingAreaDto = parkingAreaInnerServiceSMO.getFullParkAreaInfo(exist.get(0).getAreaNum());
        try {
            DoorUtil.deleteMonthlyCar(parkingAreaDto.getThirdAreaNum(), reqJson.getString("carNum"));
        } catch (Exception e) {
            e.printStackTrace();
        }
        reqJson.put("carNumType", ParkingSpaceDto.STATE_FREE);
        reqJson.put("psId", ownerCarDto.getPsId());
        ParkingSpaceDto parkingSpaceDto = new ParkingSpaceDto();
        parkingSpaceDto.setCommunityId(reqJson.getString("communityId"));
        parkingSpaceDto.setPsId(reqJson.getString("psId"));
        List<ParkingSpaceDto> parkingSpaceDtos = parkingSpaceInnerServiceSMOImpl.queryParkingSpaces(parkingSpaceDto);

        if (parkingSpaceDtos == null || parkingSpaceDtos.size() != 1) {
            //throw new ListenerExecuteException(ResponseConstant.RESULT_CODE_ERROR, "未查询到停车位信息" + JSONObject.toJSONString(parkingSpaceDto));
            return;
        }

        parkingSpaceDto = parkingSpaceDtos.get(0);

        JSONObject businessParkingSpace = new JSONObject();

        businessParkingSpace.putAll(BeanConvertUtil.beanCovertMap(parkingSpaceDto));
        businessParkingSpace.put("state", reqJson.getString("carNumType"));
        ParkingSpacePo parkingSpacePo = BeanConvertUtil.covertBean(businessParkingSpace, ParkingSpacePo.class);
        flag = parkingSpaceV1InnerServiceSMOImpl.updateParkingSpace(parkingSpacePo);
        if (flag < 1) {
            throw new IllegalArgumentException("修改车辆出错");
        }
    }
}
