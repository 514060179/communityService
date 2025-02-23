package com.newland.property.user.cmd.owner;

import com.alibaba.fastjson.JSONObject;
import com.newland.property.core.annotation.NewlandPropertyCmd;
import com.newland.property.core.context.ICmdDataFlowContext;
import com.newland.property.core.event.cmd.Cmd;
import com.newland.property.core.event.cmd.CmdEvent;
import com.newland.property.core.factory.SendSmsFactory;
import com.newland.property.dto.owner.OwnerDto;
import com.newland.property.dto.payFeeQrcode.PayFeeQrcodeDto;
import com.newland.property.dto.room.RoomDto;
import com.newland.property.intf.community.IRoomV1InnerServiceSMO;
import com.newland.property.intf.fee.IPayFeeQrcodeV1InnerServiceSMO;
import com.newland.property.intf.user.IOwnerInnerServiceSMO;
import com.newland.property.intf.user.IOwnerV1InnerServiceSMO;
import com.newland.property.utils.cache.CommonCache;
import com.newland.property.utils.exception.CmdException;
import com.newland.property.utils.util.Assert;
import com.newland.property.utils.util.StringUtil;
import com.newland.property.utils.util.ValidatorUtil;
import com.newland.property.vo.ResultVo;
import org.springframework.beans.factory.annotation.Autowired;

import java.text.ParseException;
import java.util.List;

/**
 * 二维码 查询业主
 */
@NewlandPropertyCmd(serviceCode = "owner.getQrcodeOwner")
public class GetQrcodeOwnerCmd extends Cmd {


    @Autowired
    private IOwnerV1InnerServiceSMO ownerV1InnerServiceSMOImpl;

    @Autowired
    private IRoomV1InnerServiceSMO roomV1InnerServiceSMOImpl;

    @Autowired
    private IOwnerInnerServiceSMO ownerInnerServiceSMOImpl;

    @Autowired
    private IPayFeeQrcodeV1InnerServiceSMO payFeeQrcodeV1InnerServiceSMOImpl;

    @Override
    public void validate(CmdEvent event, ICmdDataFlowContext context, JSONObject reqJson) throws CmdException, ParseException {
        Assert.hasKeyAndValue(reqJson, "queryWay", "未包含查询方式");
        Assert.hasKeyAndValue(reqJson, "communityId", "未包含小区");
        Assert.hasKeyAndValue(reqJson, "pfqId", "未包含二维码信息");

        if ("room".equals(reqJson.getString("queryWay"))) {
            Assert.hasKeyAndValue(reqJson, "roomNum", "未包含房屋信息");
        } else {
            Assert.hasKeyAndValue(reqJson, "link", "未包含手机号");
            if (!ValidatorUtil.isMobile(reqJson.getString("link"))) {
                throw new IllegalArgumentException("手机号格式错误");
            }
        }


        PayFeeQrcodeDto payFeeQrcodeDto = new PayFeeQrcodeDto();
        payFeeQrcodeDto.setPfqId(reqJson.getString("pfqId"));
        payFeeQrcodeDto.setCommunityId(reqJson.getString("communityId"));
        List<PayFeeQrcodeDto> payFeeQrcodeDtos = payFeeQrcodeV1InnerServiceSMOImpl.queryPayFeeQrcodes(payFeeQrcodeDto);

        Assert.listOnlyOne(payFeeQrcodeDtos, "二维码不存在");

        //todo 不校验验证码
        if (!"ON".equals(payFeeQrcodeDtos.get(0).getSmsValidate())) {
            return;
        }
        Assert.hasKeyAndValue(reqJson, "msgCode", "未包含验证码信息");

        OwnerDto ownerDto = computeOwner(reqJson);
        String smsCode = CommonCache.getValue(ownerDto.getLink() + SendSmsFactory.VALIDATE_CODE);

        if (!StringUtil.isEmpty(smsCode) && smsCode.contains("-")) {
            smsCode = smsCode.substring(0, smsCode.indexOf("-"));
        }

        if (!reqJson.getString("msgCode").equals(smsCode)) {
            throw new CmdException("验证码错误");
        }

    }

    @Override
    public void doCmd(CmdEvent event, ICmdDataFlowContext context, JSONObject reqJson) throws CmdException, ParseException {

        OwnerDto ownerDto = computeOwner(reqJson);

        String ownerName = ownerDto.getName();


        JSONObject data = new JSONObject();
        data.put("ownerId",ownerDto.getOwnerId());
        data.put("roomId",ownerDto.getRoomId());
        data.put("ownerName",StringUtil.maskName(ownerName));

        context.setResponseEntity(ResultVo.createResponseEntity(data));
    }

    /**
     * 计算业主手机号
     *
     * @param reqJson
     * @return
     */
    private OwnerDto computeOwner(JSONObject reqJson) {

        if ("phone".equals(reqJson.getString("queryWay"))) {
            OwnerDto ownerDto = new OwnerDto();
            ownerDto.setLink(reqJson.getString("link"));
            ownerDto.setCommunityId(reqJson.getString("communityId"));
            ownerDto.setOwnerTypeCd(OwnerDto.OWNER_TYPE_CD_OWNER);
            List<OwnerDto> ownerDtos = ownerV1InnerServiceSMOImpl.queryOwners(ownerDto);
            if (ownerDtos == null || ownerDtos.size() < 1) {
                throw new CmdException("业主不存在");
            }
            return ownerDtos.get(0);
        }
        String roomNumStr = reqJson.getString("roomNum");
        String[] roomNums = roomNumStr.split("-", 3);
        if (roomNums == null || roomNums.length != 3) {
            throw new CmdException("业主不存在");
        }

        RoomDto roomDto = new RoomDto();
        roomDto.setFloorNum(roomNums[0]);
        roomDto.setUnitNum(roomNums[1]);
        roomDto.setRoomNum(roomNums[2]);
        roomDto.setCommunityId(reqJson.getString("communityId"));
        List<RoomDto> roomDtos = roomV1InnerServiceSMOImpl.queryRooms(roomDto);

        Assert.listOnlyOne(roomDtos, "房屋不存在");


        OwnerDto ownerDto = new OwnerDto();
        ownerDto.setRoomId(roomDtos.get(0).getRoomId());
        ownerDto.setCommunityId(reqJson.getString("communityId"));
        ownerDto.setOwnerTypeCd(OwnerDto.OWNER_TYPE_CD_OWNER);
        List<OwnerDto> ownerDtos = ownerInnerServiceSMOImpl.queryOwners(ownerDto);
        if (ownerDtos == null || ownerDtos.size() < 1) {
            throw new CmdException("业主不存在");
        }
        ownerDtos.get(0).setRoomId(roomDtos.get(0).getRoomId());
        return ownerDtos.get(0);
    }
}
