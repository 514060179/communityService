package com.newland.property.user.cmd.owner;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.newland.property.core.annotation.NewlandPropertyCmd;
import com.newland.property.core.annotation.PropertyTransactional;
import com.newland.property.core.context.ICmdDataFlowContext;
import com.newland.property.core.event.cmd.Cmd;
import com.newland.property.core.event.cmd.CmdEvent;
import com.newland.property.core.factory.GenerateCodeFactory;
import com.newland.property.dto.fee.FeeConfigDto;
import com.newland.property.dto.fee.FeeDto;
import com.newland.property.dto.machine.CarInoutDto;
import com.newland.property.dto.owner.OwnerCarDto;
import com.newland.property.dto.parking.ParkingAreaDto;
import com.newland.property.dto.parking.ParkingSpaceDto;
import com.newland.property.dto.payFee.PayFeeBatchDto;
import com.newland.property.dto.user.UserDto;
import com.newland.property.intf.common.ICarInoutInnerServiceSMO;
import com.newland.property.intf.community.IParkingAreaInnerServiceSMO;
import com.newland.property.intf.community.IParkingSpaceInnerServiceSMO;
import com.newland.property.intf.community.IParkingSpaceV1InnerServiceSMO;
import com.newland.property.intf.fee.IFeeConfigInnerServiceSMO;
import com.newland.property.intf.fee.IFeeInnerServiceSMO;
import com.newland.property.intf.fee.IPayFeeBatchV1InnerServiceSMO;
import com.newland.property.intf.user.IOwnerCarAttrInnerServiceSMO;
import com.newland.property.intf.user.IOwnerCarInnerServiceSMO;
import com.newland.property.intf.user.IOwnerCarV1InnerServiceSMO;
import com.newland.property.intf.user.IUserInnerServiceSMO;
import com.newland.property.job.util.DoorUtil;
import com.newland.property.po.car.OwnerCarPo;
import com.newland.property.po.fee.PayFeePo;
import com.newland.property.po.owner.OwnerCarAttrPo;
import com.newland.property.po.parking.ParkingSpacePo;
import com.newland.property.po.payFee.PayFeeBatchPo;
import com.newland.property.utils.cache.MappingCache;
import com.newland.property.utils.constant.CommonConstant;
import com.newland.property.utils.exception.CmdException;
import com.newland.property.utils.util.*;
import com.newland.property.vo.api.feeConfig.ApiFeeConfigDataVo;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@NewlandPropertyCmd(serviceCode = "owner.saveOwnerCar")
public class SaveOwnerCarCmd extends Cmd {


    @Autowired
    private IFeeConfigInnerServiceSMO feeConfigInnerServiceSMOImpl;

    @Autowired
    private IParkingSpaceInnerServiceSMO parkingSpaceInnerServiceSMOImpl;

    @Autowired
    private IParkingAreaInnerServiceSMO parkingAreaInnerServiceSMO;

    @Autowired
    private IOwnerCarInnerServiceSMO ownerCarInnerServiceSMOImpl;

    @Autowired
    private IOwnerCarAttrInnerServiceSMO ownerCarAttrInnerServiceSMOImpl;

    @Autowired
    private IOwnerCarV1InnerServiceSMO ownerCarV1InnerServiceSMOImpl;

    @Autowired
    private IParkingSpaceV1InnerServiceSMO parkingSpaceV1InnerServiceSMOImpl;

    @Autowired
    private ICarInoutInnerServiceSMO carInoutInnerServiceSMOImpl;

    @Autowired
    private IFeeInnerServiceSMO feeInnerServiceSMOImpl;

    @Autowired
    private IUserInnerServiceSMO userInnerServiceSMOImpl;

    @Autowired
    private IPayFeeBatchV1InnerServiceSMO payFeeBatchV1InnerServiceSMOImpl;

    @Override
    public void validate(CmdEvent event, ICmdDataFlowContext context, JSONObject reqJson) throws CmdException {
        Assert.hasKeyAndValue(reqJson, "communityId", "未包含小区ID");
        Assert.hasKeyAndValue(reqJson, "ownerId", "请求报文中未包含ownerId");
        Assert.hasKeyAndValue(reqJson, "carNum", "请求报文中未包含carNum");
        Assert.hasKeyAndValue(reqJson, "carType", "请求报文中未包含carType");

        if (OwnerCarDto.LEASE_TYPE_MONTH.equals(reqJson.getString("leaseType"))) {
            Assert.hasKeyAndValue(reqJson, "startTime", "请求报文中未包含开始时间");
            Assert.hasKeyAndValue(reqJson, "endTime", "请求报文中未包含结束时间");
            Assert.isDate(reqJson.getString("startTime"),DateUtil.DATE_FORMATE_STRING_B,"开始时间格式错误");
            Assert.isDate(reqJson.getString("endTime"),DateUtil.DATE_FORMATE_STRING_B,"结束时间格式错误");
        }

        //检查车位是否是空闲状态
        ParkingSpaceDto parkingSpaceDto = new ParkingSpaceDto();
        if(StringUtils.isNotBlank(reqJson.getString("psId"))){
            parkingSpaceDto.setPsId(reqJson.getString("psId"));
        }
        List<ParkingSpaceDto> parkingSpaceDtos = parkingSpaceInnerServiceSMOImpl.queryParkingSpaces(parkingSpaceDto);
        //Assert.listOnlyOne(parkingSpaceDtos, "查询车位错误！");
        //获取车位状态
//        String state = parkingSpaceDtos.get(0).getState();
//        if (StringUtil.isEmpty(state) || !"F".equals(state)) {
//            throw new IllegalArgumentException("该车位不是空闲状态！");
//        }
        //校验车牌号是否存在
        OwnerCarDto ownerCarDto = new OwnerCarDto();
        ownerCarDto.setCommunityId(reqJson.getString("communityId"));
        ownerCarDto.setCarNum(reqJson.getString("carNum"));
        ownerCarDto.setAreaNum(reqJson.getString("areaNum"));
//        if(parkingSpaceDtos.size() > 0){
//            ownerCarDto.setPaIds(new String[]{parkingSpaceDtos.get(0).getPaId()});
//        }

        ownerCarDto.setCarTypeCds(new String[]{OwnerCarDto.CAR_TYPE_PRIMARY, OwnerCarDto.CAR_TYPE_MEMBER}); // 临时车除外
        int count = ownerCarInnerServiceSMOImpl.queryOwnerCarsCount(ownerCarDto);

        if (count > 0) {
            throw new IllegalArgumentException("车辆已存在");
        }

        //判断临时车 是否在场
        String parkingIn = MappingCache.getValue("TEMP_CAR_IN_PARKING");

        if (!"ON".equals(parkingIn)) {
            return;
        }

        ownerCarDto = new OwnerCarDto();
        ownerCarDto.setCommunityId(reqJson.getString("communityId"));
        ownerCarDto.setCarNum(reqJson.getString("carNum"));
        ownerCarDto.setCarTypeCds(new String[]{OwnerCarDto.CAR_TYPE_TEMP}); // 临时车除外
        count = ownerCarInnerServiceSMOImpl.queryOwnerCarsCount(ownerCarDto);
        if (count < 1) {
            return;
        }

        CarInoutDto carInoutDto = new CarInoutDto();
        carInoutDto.setCarNum(reqJson.getString("carNum"));
        carInoutDto.setStates(new String[]{CarInoutDto.STATE_PAY, CarInoutDto.STATE_IN, CarInoutDto.STATE_REPAY});
        List<CarInoutDto> carInoutDtos = carInoutInnerServiceSMOImpl.queryCarInouts(carInoutDto);
        if (carInoutDtos != null && carInoutDtos.size() > 0) {
            throw new CmdException("车辆在场，请出场后再办理月租车");
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

    @Override
    @PropertyTransactional
    @Transactional
    public void doCmd(CmdEvent event, ICmdDataFlowContext context, JSONObject reqJson) throws CmdException {
        if (!reqJson.containsKey("leaseType")) {
            reqJson.put("leaseType", OwnerCarDto.LEASE_TYPE_MONTH);
        }

        if (!OwnerCarDto.LEASE_TYPE_MONTH.equals(reqJson.getString("leaseType"))) {
            reqJson.put("startTime", DateUtil.getFormatTimeString(new Date(), DateUtil.DATE_FORMATE_STRING_B));
            reqJson.put("endTime", "2050-01-01");
        }
        JSONObject businessOwnerCar = new JSONObject();
        businessOwnerCar.putAll(reqJson);
        businessOwnerCar.put("memberId", GenerateCodeFactory.getGeneratorId(GenerateCodeFactory.CODE_PREFIX_carId));
        if (!reqJson.containsKey("carId") || reqJson.getString("carId").startsWith("-")) {
            businessOwnerCar.put("carId", businessOwnerCar.getString("memberId"));
        }
        OwnerCarPo ownerCarPo = BeanConvertUtil.covertBean(businessOwnerCar, OwnerCarPo.class);
        ownerCarPo.setState(OwnerCarDto.STATE_NORMAL);

        //没有指定时为主要车辆
        if (!reqJson.containsKey("carTypeCd") || StringUtil.isEmpty(reqJson.getString("carTypeCd"))) {
            ownerCarPo.setCarTypeCd(OwnerCarDto.CAR_TYPE_PRIMARY);
        }
        //添加车辆属性
        dealOwnerCarAttr(reqJson, ownerCarPo);

        OwnerCarDto ownerCarDto = BeanConvertUtil.covertBean(businessOwnerCar, OwnerCarDto.class);

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
                payFeePo.setbId("-1");
                ownerCarDto.setBalanceMoney(feeConfigs.get(0).getAdditionalAmount());
                ownerCarPo.setBalanceMoney(feeConfigs.get(0).getAdditionalAmount());
                List<PayFeePo> payFeePos = new ArrayList(){{add(payFeePo);}};
                feeInnerServiceSMOImpl.saveFee(payFeePos);
            }
        }
        int flag = ownerCarV1InnerServiceSMOImpl.saveOwnerCar(ownerCarPo);
        if (flag < 1) {
            throw new CmdException("保存车辆属性失败");
        }
        //发起道尔新增月租车请求
        //获取停车场信息
        ParkingAreaDto parkingAreaDto = parkingAreaInnerServiceSMO.getFullParkAreaInfo(ownerCarPo.getAreaNum());
        try {
            DoorUtil.createMonthlyCar(parkingAreaDto.getThirdAreaNum(), ownerCarDto);
        } catch (Exception e) {
            e.printStackTrace();
        }
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
        ParkingSpacePo parkingSpacePo = BeanConvertUtil.covertBean(businessParkingSpace, ParkingSpacePo.class);
        parkingSpacePo.setState("H"); //S 出售  H 出租  F 空闲
        flag = parkingSpaceV1InnerServiceSMOImpl.updateParkingSpace(parkingSpacePo);
        if (flag < 1) {
            throw new CmdException("修改车位状态失败");
        }

    }


    private void dealOwnerCarAttr(JSONObject paramInJson, OwnerCarPo ownerCarPo) {

        if (!paramInJson.containsKey("attrs")) {
            return;
        }

        JSONArray attrs = paramInJson.getJSONArray("attrs");
        if (attrs.size() < 1) {
            return;
        }
        JSONObject attr = null;
        int flag = 0;
        for (int attrIndex = 0; attrIndex < attrs.size(); attrIndex++) {
            attr = attrs.getJSONObject(attrIndex);
            OwnerCarAttrPo ownerCarAttrPo = new OwnerCarAttrPo();
            ownerCarAttrPo.setAttrId(GenerateCodeFactory.getAttrId());
            ownerCarAttrPo.setCommunityId(ownerCarPo.getCommunityId());
            ownerCarAttrPo.setCarId(ownerCarPo.getCarId());
            ownerCarAttrPo.setSpecCd(attr.getString("specCd"));
            ownerCarAttrPo.setValue(attr.getString("value"));
            flag = ownerCarAttrInnerServiceSMOImpl.saveOwnerCarAttr(ownerCarAttrPo);
            if (flag < 1) {
                throw new CmdException("保存车辆属性失败");
            }
        }

    }
}
