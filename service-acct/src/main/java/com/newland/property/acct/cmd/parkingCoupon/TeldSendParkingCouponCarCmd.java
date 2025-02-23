package com.newland.property.acct.cmd.parkingCoupon;

import com.alibaba.fastjson.JSONObject;
import com.newland.property.core.annotation.NewlandPropertyCmd;
import com.newland.property.core.annotation.PropertyTransactional;
import com.newland.property.core.context.ICmdDataFlowContext;
import com.newland.property.core.event.cmd.Cmd;
import com.newland.property.core.event.cmd.CmdEvent;
import com.newland.property.core.factory.GenerateCodeFactory;
import com.newland.property.dto.parking.ParkingCouponCarDto;
import com.newland.property.dto.parking.ParkingCouponShopDto;
import com.newland.property.dto.shop.ShopDto;
import com.newland.property.dto.shop.StoreShopDto;
import com.newland.property.dto.store.StoreUserDto;
import com.newland.property.intf.acct.IParkingCouponCarV1InnerServiceSMO;
import com.newland.property.intf.acct.IParkingCouponShopV1InnerServiceSMO;
import com.newland.property.intf.store.IStoreShopV1InnerServiceSMO;
import com.newland.property.intf.store.IStoreUserV1InnerServiceSMO;
import com.newland.property.po.parking.ParkingCouponCarPo;
import com.newland.property.po.parking.ParkingCouponShopPo;
import com.newland.property.utils.cache.MappingCache;
import com.newland.property.utils.exception.CmdException;
import com.newland.property.utils.lock.DistributedLock;
import com.newland.property.utils.util.Assert;
import com.newland.property.utils.util.DateUtil;
import com.newland.property.utils.util.StringUtil;
import com.newland.property.utils.util.TeldUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.text.ParseException;
import java.util.List;

/**
 * 特来电同步车牌号给物业系统
 *
 *
 * /app/ext/parkingCoupon.teldSendParkingCouponCar/992020051967020024
 * /app/ext/login.getTokenForTeld/992020051967020024
 */
@NewlandPropertyCmd(serviceCode = "notification_charge_end_order_info")
public class TeldSendParkingCouponCarCmd extends Cmd {

    @Autowired
    private IStoreUserV1InnerServiceSMO storeUserV1InnerServiceSMOImpl;

    @Autowired
    private IStoreShopV1InnerServiceSMO storeShopV1InnerServiceSMOImpl;

    @Autowired
    private IParkingCouponShopV1InnerServiceSMO parkingCouponShopV1InnerServiceSMOImpl;

    @Autowired
    private IParkingCouponCarV1InnerServiceSMO parkingCouponCarV1InnerServiceSMOImpl;

    public static final String CODE_PREFIX_ID = "10";

    @Override
    public void validate(CmdEvent event, ICmdDataFlowContext context, JSONObject reqJson) throws CmdException, ParseException {
        Assert.hasKeyAndValue(reqJson, "Data", "未包含加密报文");

    }

    @Override
    @PropertyTransactional
    public void doCmd(CmdEvent event, ICmdDataFlowContext context, JSONObject reqJson) throws CmdException, ParseException {
        String signKey = MappingCache.getValue("TELD_DOMAIN", "SIGN_KEY");
        String aesKey = MappingCache.getValue("TELD_DOMAIN", "AES_KEY");
        String aesIv = MappingCache.getValue("TELD_DOMAIN", "AES_IV");

        String data = TeldUtil.Decrypt(reqJson.getString("Data"), aesKey, aesIv);

        if (StringUtil.isEmpty(data)) {
            throw new CmdException("未包含报文");
        }

        JSONObject dataObj = JSONObject.parseObject(data);

        String StartChargeSeq = dataObj.getString("StartChargeSeq");
        int ConfirmResult = 0;
        int PlateAutResult = 1;
        int PlateAutFailReason = 0;
        try {
            doSend(context, dataObj);
        } catch (Exception e) {
            e.printStackTrace();
            ConfirmResult = 1;
            PlateAutResult = 2;
            PlateAutFailReason = 6;
        }

        JSONObject paramOut = new JSONObject();
        paramOut.put("StartChargeSeq", StartChargeSeq);
        paramOut.put("ConfirmResult", ConfirmResult);
        paramOut.put("PlateAutResult", PlateAutResult);
        paramOut.put("PlateAutFailReason", PlateAutFailReason);
        String paramStr = "";
        try {
            paramStr = TeldUtil.generateReturnParam(paramOut.toJSONString(), aesKey, aesIv, signKey, reqJson.getString("OperatorID"));
        } catch (Exception e) {
            e.printStackTrace();
        }

        context.setResponseEntity(new ResponseEntity(paramStr, HttpStatus.OK));

    }

    private void doSend(ICmdDataFlowContext context, JSONObject dataObj) {
        String userId = context.getReqHeaders().get("user-id");


        StoreUserDto storeUserDto = new StoreUserDto();
        storeUserDto.setUserId(userId);
        List<StoreUserDto> storeUserDtos = storeUserV1InnerServiceSMOImpl.queryStoreUsers(storeUserDto);
        Assert.listOnlyOne(storeUserDtos, "商户不存在");

        StoreShopDto storeShopDto = new StoreShopDto();
        storeShopDto.setStoreId(storeUserDtos.get(0).getStoreId());
        List<ShopDto> storeShops = storeShopV1InnerServiceSMOImpl.queryStoreShops(storeShopDto);
        if (storeShops == null || storeShops.size() < 1) {
            Assert.listOnlyOne(storeUserDtos, "商户店铺不存在");
        }

        ParkingCouponShopDto parkingCouponShopDto = new ParkingCouponShopDto();
        parkingCouponShopDto.setHasQuantity("Y");
        parkingCouponShopDto.setShopId(storeShops.get(0).getShopId());
        List<ParkingCouponShopDto> parkingCouponShopDtos = parkingCouponShopV1InnerServiceSMOImpl.queryParkingCouponShops(parkingCouponShopDto);

        if (parkingCouponShopDtos == null || parkingCouponShopDtos.size() < 1) { //停车券已用完
            throw new CmdException("停车券已用完");
        }


        String carNum = dataObj.getString("PlateNum");

        int quantity = 0;

        int flag = 0;
        String requestId = DistributedLock.getLockUUID();
        String key = this.getClass().getSimpleName() + parkingCouponShopDtos.get(0).getCouponShopId();
        try {
            DistributedLock.waitGetDistributedLock(key, requestId);
            parkingCouponShopDto = new ParkingCouponShopDto();
            parkingCouponShopDto.setCouponShopId(parkingCouponShopDtos.get(0).getCouponShopId());
            parkingCouponShopDto.setShopId(storeShops.get(0).getShopId());
            parkingCouponShopDtos = parkingCouponShopV1InnerServiceSMOImpl.queryParkingCouponShops(parkingCouponShopDto);
            quantity = Integer.parseInt(parkingCouponShopDtos.get(0).getQuantity());
            if (quantity < 1) {
                throw new CmdException("停车劵不足，请购买");
            }
            ParkingCouponShopPo parkingCouponShopPo = new ParkingCouponShopPo();
            parkingCouponShopPo.setCouponShopId(parkingCouponShopDtos.get(0).getCouponShopId());
            parkingCouponShopPo.setQuantity((quantity - 1) + "");
            flag = parkingCouponShopV1InnerServiceSMOImpl.updateParkingCouponShop(parkingCouponShopPo);
            if (flag < 1) {
                throw new CmdException("优惠券递减失败");
            }
        } finally {
            DistributedLock.releaseDistributedLock(requestId, key);
        }

        String StartChargeSeq = dataObj.getString("StartChargeSeq");
        JSONObject remarkObj = new JSONObject();
        remarkObj.put("StartChargeSeq", StartChargeSeq);

        ParkingCouponCarPo parkingCouponCarPo = new ParkingCouponCarPo();
        parkingCouponCarPo.setCarNum(carNum);
        parkingCouponCarPo.setGiveWay("1001");
        parkingCouponCarPo.setCouponShopId(parkingCouponShopDtos.get(0).getCouponShopId());
        parkingCouponCarPo.setShopId(parkingCouponShopDtos.get(0).getShopId());
        parkingCouponCarPo.setPccId(GenerateCodeFactory.getGeneratorId(CODE_PREFIX_ID));
        parkingCouponCarPo.setCouponId(parkingCouponShopDtos.get(0).getCouponId());
        parkingCouponCarPo.setCommunityId(parkingCouponShopDtos.get(0).getCommunityId());
        parkingCouponCarPo.setStartTime(DateUtil.getNow(DateUtil.DATE_FORMATE_STRING_A));
        parkingCouponCarPo.setEndTime(DateUtil.getAddDayString(DateUtil.getCurrentDate(), DateUtil.DATE_FORMATE_STRING_A, 1));
        parkingCouponCarPo.setPaId(parkingCouponShopDtos.get(0).getPaId());
        parkingCouponCarPo.setState(ParkingCouponCarDto.STATE_WAIT);
        parkingCouponCarPo.setTypeCd(parkingCouponShopDtos.get(0).getTypeCd());
        parkingCouponCarPo.setValue(parkingCouponShopDtos.get(0).getValue());
        parkingCouponCarPo.setRemark(remarkObj.toJSONString());

        flag = parkingCouponCarV1InnerServiceSMOImpl.saveParkingCouponCar(parkingCouponCarPo);

        if (flag < 1) {
            throw new CmdException("保存数据失败");
        }
    }
}
