package com.newland.property.user.cmd.owner;

import com.alibaba.fastjson.JSONObject;
import com.newland.property.core.annotation.NewlandPropertyCmd;
import com.newland.property.core.context.ICmdDataFlowContext;
import com.newland.property.core.event.cmd.Cmd;
import com.newland.property.core.event.cmd.CmdEvent;
import com.newland.property.core.factory.GenerateCodeFactory;
import com.newland.property.dto.fee.FeeConfigDto;
import com.newland.property.dto.fee.FeeDto;
import com.newland.property.dto.owner.OwnerCarDto;
import com.newland.property.dto.parking.ParkingAreaDto;
import com.newland.property.dto.parking.ParkingSpaceDto;
import com.newland.property.dto.payFee.PayFeeBatchDto;
import com.newland.property.dto.user.UserDto;
import com.newland.property.intf.community.IParkingAreaInnerServiceSMO;
import com.newland.property.intf.community.IParkingSpaceInnerServiceSMO;
import com.newland.property.intf.community.IParkingSpaceV1InnerServiceSMO;
import com.newland.property.intf.fee.IFeeConfigInnerServiceSMO;
import com.newland.property.intf.fee.IFeeInnerServiceSMO;
import com.newland.property.intf.fee.IPayFeeBatchV1InnerServiceSMO;
import com.newland.property.intf.user.IOwnerCarInnerServiceSMO;
import com.newland.property.intf.user.IOwnerCarV1InnerServiceSMO;
import com.newland.property.intf.user.IUserInnerServiceSMO;
import com.newland.property.job.util.DoorUtil;
import com.newland.property.po.car.OwnerCarPo;
import com.newland.property.po.fee.PayFeePo;
import com.newland.property.po.parking.ParkingSpacePo;
import com.newland.property.po.payFee.PayFeeBatchPo;
import com.newland.property.utils.constant.CommonConstant;
import com.newland.property.utils.exception.CmdException;
import com.newland.property.utils.util.Assert;
import com.newland.property.utils.util.BeanConvertUtil;
import com.newland.property.utils.util.StringUtil;
import com.newland.property.vo.api.feeConfig.ApiFeeConfigDataVo;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

@NewlandPropertyCmd(serviceCode = "owner.carAddParkingSpace")
public class CarAddParkingSpaceCmd extends Cmd {

    @Autowired
    private IOwnerCarInnerServiceSMO ownerCarInnerServiceSMOImpl;


    @Autowired
    private IParkingSpaceInnerServiceSMO parkingSpaceInnerServiceSMOImpl;

    @Autowired
    private IParkingSpaceV1InnerServiceSMO parkingSpaceV1InnerServiceSMOImpl;

    @Autowired
    private IOwnerCarV1InnerServiceSMO ownerCarV1InnerServiceSMOImpl;


    @Autowired
    private IFeeConfigInnerServiceSMO feeConfigInnerServiceSMOImpl;

    @Autowired
    private IUserInnerServiceSMO userInnerServiceSMOImpl;

    @Autowired
    private IPayFeeBatchV1InnerServiceSMO payFeeBatchV1InnerServiceSMOImpl;

    @Autowired
    private IFeeInnerServiceSMO feeInnerServiceSMOImpl;

    @Autowired
    private IParkingAreaInnerServiceSMO parkingAreaInnerServiceSMO;

    @Override
    public void validate(CmdEvent event, ICmdDataFlowContext context, JSONObject reqJson) throws CmdException {
        Assert.jsonObjectHaveKey(reqJson, "communityId", "未包含小区ID");
        Assert.jsonObjectHaveKey(reqJson, "carId", "请求报文中未包含carId");
        Assert.jsonObjectHaveKey(reqJson, "startTime", "请求报文中未包含startTime");
        Assert.jsonObjectHaveKey(reqJson, "endTime", "请求报文中未包含startTime");
        Assert.jsonObjectHaveKey(reqJson, "psId", "请求报文中未包含psId");
        Assert.hasLength(reqJson.getString("communityId"), "小区ID不能为空");

        OwnerCarDto ownerCarDto = new OwnerCarDto();
        ownerCarDto.setCarId(reqJson.getString("carId"));
        ownerCarDto.setCommunityId(reqJson.getString("communityId"));
        List<OwnerCarDto> ownerCarDtos = ownerCarInnerServiceSMOImpl.queryOwnerCars(ownerCarDto);
        Assert.listOnlyOne(ownerCarDtos, "未找到车辆信息");

        String state = ownerCarDtos.get(0).getState();

        if (!StringUtil.isEmpty(state) && !state.equals(OwnerCarDto.STATE_DELETE) && !state.equals(OwnerCarDto.STATE_FINISH)) {
            //throw new IllegalArgumentException("已有车位无需续租");
        }


    }

    /**
     * 生成批次号
     *
     * @param reqJson
     */
    private void generatorBatch(JSONObject reqJson) {
        PayFeeBatchPo payFeeBatchPo = new PayFeeBatchPo();
        payFeeBatchPo.setBatchId(GenerateCodeFactory.getGeneratorId("12"));
        payFeeBatchPo.setCommunityId(reqJson.getString("communityId"));
        payFeeBatchPo.setCreateUserId(reqJson.getString("userId"));
        UserDto userDto = new UserDto();
        userDto.setUserId(reqJson.getString("userId"));
        List<UserDto> userDtos = userInnerServiceSMOImpl.getUsers(userDto);

        Assert.listOnlyOne(userDtos, "用户不存在");
        payFeeBatchPo.setCreateUserName(userDtos.get(0).getUserName());
        payFeeBatchPo.setState(PayFeeBatchDto.STATE_NORMAL);
        payFeeBatchPo.setMsg("正常");
        int flag = payFeeBatchV1InnerServiceSMOImpl.savePayFeeBatch(payFeeBatchPo);

        if (flag < 1) {
            throw new IllegalArgumentException("生成批次失败");
        }

        reqJson.put("batchId", payFeeBatchPo.getBatchId());
    }


    @Transactional
    @Override
    public void doCmd(CmdEvent event, ICmdDataFlowContext context, JSONObject reqJson) throws CmdException {
        OwnerCarPo ownerCarPo = BeanConvertUtil.covertBean(reqJson, OwnerCarPo.class);
        ParkingSpaceDto parkingSpaceDto = new ParkingSpaceDto();
        List<ParkingSpaceDto> parkingSpaceDtos;
        if(StringUtils.isNotBlank(ownerCarPo.getPsId())){
            parkingSpaceDtos = parkingSpaceInnerServiceSMOImpl.queryParkingSpaces(parkingSpaceDto);
//          Assert.listOnlyOne(parkingSpaceDtos, "查询车位信息错误！");

            if(parkingSpaceDtos.size() > 0){
                //获取车位状态(出售 S，出租 H ，空闲 F)
                String state = parkingSpaceDtos.get(0).getState();
                if (!StringUtil.isEmpty(state) && !"F".equals(state)) { //如果车位状态不是空闲的，就不能续租
                    throw new IllegalArgumentException("车位已被使用，无法继续续租！");
                }
                ownerCarPo.setState(OwnerCarDto.STATE_NORMAL);
            }
        }
        parkingSpaceDto.setPsId(ownerCarPo.getPsId());
        //获取当前状态
        OwnerCarDto ownerCarDto = new OwnerCarDto();
        ownerCarDto.setCarTypeCd("1001"); //业主车辆
        ownerCarDto.setCommunityId(ownerCarPo.getCommunityId());
        ownerCarDto.setCarNum(ownerCarPo.getCarNum());
        List<OwnerCarDto> ownerCarDtos = ownerCarInnerServiceSMOImpl.queryOwnerCars(ownerCarDto);
        if (ownerCarDtos.size() != 1) {
            throw new IllegalArgumentException("车辆信息错误");
        }
        ownerCarDto = ownerCarDtos.get(0);
        String lastState = ownerCarDto.getState();
        ownerCarPo.setState(OwnerCarDto.STATE_NORMAL);
        int flag = ownerCarV1InnerServiceSMOImpl.updateOwnerCar(ownerCarPo);
        if (flag < 1) {
            throw new IllegalArgumentException("修改车辆出错");
        }


        //新增收费项
        if(StringUtils.isNotBlank(ownerCarDto.getConfigId())){
            PayFeePo payFeePo = new PayFeePo();
            //获取config
            FeeConfigDto feeConfigDto = new FeeConfigDto();
            feeConfigDto.setConfigId(ownerCarPo.getConfigId());
            List<ApiFeeConfigDataVo> feeConfigs = BeanConvertUtil.covertBeanList(feeConfigInnerServiceSMOImpl.queryFeeConfigs(feeConfigDto), ApiFeeConfigDataVo.class);
            if(feeConfigs.size() > 0){
                //生成批次
                reqJson.put("communityId", ownerCarDto.getCommunityId());
                reqJson.put("userId", context.getReqHeaders().get(CommonConstant.HTTP_USER_ID));
                generatorBatch(reqJson);
                payFeePo.setbId(feeConfigs.get(0).getConfigId());
                payFeePo.setFeeId(GenerateCodeFactory.getGeneratorId(GenerateCodeFactory.CODE_PREFIX_feeId,true));
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                payFeePo.setStartTime(sdf.format(ownerCarDto.getStartTime()));
                payFeePo.setEndTime(sdf.format(ownerCarDto.getEndTime()));
                payFeePo.setState(FeeDto.STATE_DOING);
                payFeePo.setCommunityId(ownerCarDto.getCommunityId());
                payFeePo.setConfigId(ownerCarDto.getConfigId());
                payFeePo.setPayerObjId(ownerCarDto.getCarId());
                payFeePo.setPayerObjType(FeeDto.PAYER_OBJ_TYPE_CAR);
                payFeePo.setUserId(context.getReqHeaders().get(CommonConstant.HTTP_USER_ID));
                payFeePo.setIncomeObjId(reqJson.getString("storeId"));
                payFeePo.setBatchId(reqJson.get("batchId").toString());
                payFeePo.setFeeTypeCd(FeeConfigDto.FEE_TYPE_CD_PARKING);
                payFeePo.setFeeFlag("1003006");
                payFeePo.setAmount("-1");
                payFeePo.setStatusCd("0");
                payFeePo.setStatusCd("0");
                payFeePo.setbId("-1");
                ownerCarDto.setBalanceMoney(feeConfigs.get(0).getAdditionalAmount());
                ownerCarPo.setBalanceMoney(feeConfigs.get(0).getAdditionalAmount());
                List<PayFeePo> payFeePos = new ArrayList(){{add(payFeePo);}};
                feeInnerServiceSMOImpl.saveFee(payFeePos);
            }
        }

        //道尔开户
        if(lastState.equals(OwnerCarDto.STATE_DELETE)){
            ParkingAreaDto parkingAreaDto = parkingAreaInnerServiceSMO.getFullParkAreaInfo(ownerCarDto.getAreaNum());
            //DoorUtil.renewMonthlyCar(ownerCarPo.getCarNum(), ownerCarPo.getStartTime(), ownerCarPo.getEndTime(), ownerCarPo.getBalanceMoney(),  ownerCarPo.getPayType());
            try {
                DoorUtil.createMonthlyCar(parkingAreaDto.getThirdAreaNum(), ownerCarDto);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        //道尔续租
        else if(lastState.equals(OwnerCarDto.STATE_NORMAL)){
            ParkingAreaDto parkingAreaDto = parkingAreaInnerServiceSMO.getFullParkAreaInfo(ownerCarDto.getAreaNum());
            DoorUtil.renewMonthlyCar(parkingAreaDto.getThirdAreaNum(), ownerCarDto.getCarNum(), ownerCarPo.getStartTime(), ownerCarPo.getEndTime(), ownerCarDto.getBalanceMoney(),  ownerCarDto.getPayType());
        }


        reqJson.put("carNumType", ParkingSpaceDto.STATE_HIRE);
        parkingSpaceDto = new ParkingSpaceDto();
        parkingSpaceDto.setCommunityId(reqJson.getString("communityId"));
        parkingSpaceDto.setPsId(reqJson.getString("psId"));
        parkingSpaceDtos = parkingSpaceInnerServiceSMOImpl.queryParkingSpaces(parkingSpaceDto);

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
