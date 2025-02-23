package com.newland.property.community.cmd.room;

import com.alibaba.fastjson.JSONObject;
import com.newland.property.core.annotation.NewlandPropertyCmd;
import com.newland.property.core.context.ICmdDataFlowContext;
import com.newland.property.core.event.cmd.Cmd;
import com.newland.property.core.event.cmd.CmdEvent;
import com.newland.property.core.factory.GenerateCodeFactory;
import com.newland.property.dto.room.RoomDto;
import com.newland.property.dto.owner.OwnerDto;
import com.newland.property.dto.owner.OwnerRoomRelDto;
import com.newland.property.intf.community.ICommunityInnerServiceSMO;
import com.newland.property.intf.community.IRoomInnerServiceSMO;
import com.newland.property.intf.community.IRoomV1InnerServiceSMO;
import com.newland.property.intf.community.IUnitInnerServiceSMO;
import com.newland.property.intf.user.IOwnerRoomRelInnerServiceSMO;
import com.newland.property.intf.user.IOwnerRoomRelV1InnerServiceSMO;
import com.newland.property.intf.user.IOwnerV1InnerServiceSMO;
import com.newland.property.po.owner.OwnerPo;
import com.newland.property.po.owner.OwnerRoomRelPo;
import com.newland.property.po.room.RoomPo;
import com.newland.property.utils.exception.CmdException;
import com.newland.property.utils.util.Assert;
import com.newland.property.utils.util.BeanConvertUtil;
import com.newland.property.utils.util.StringUtil;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

@NewlandPropertyCmd(serviceCode = "room.saveOwnerShops")
public class SaveOwnerShopsCmd extends Cmd {

    @Autowired
    private IUnitInnerServiceSMO unitInnerServiceSMOImpl;

    @Autowired
    private ICommunityInnerServiceSMO communityInnerServiceSMOImpl;

    @Autowired
    private IOwnerRoomRelInnerServiceSMO ownerRoomRelInnerServiceSMOImpl;

    @Autowired
    private IRoomInnerServiceSMO roomInnerServiceSMOImpl;

    @Autowired
    private IOwnerRoomRelV1InnerServiceSMO ownerRoomRelV1InnerServiceSMOImpl;

    @Autowired
    private IRoomV1InnerServiceSMO roomV1InnerServiceSMOImpl;

    @Autowired
    private IOwnerV1InnerServiceSMO ownerV1InnerServiceSMOImpl;

    @Override
    public void validate(CmdEvent event, ICmdDataFlowContext context, JSONObject reqJson) throws CmdException {
        Assert.jsonObjectHaveKey(reqJson, "communityId", "请求报文中未包含communityId节点");
        Assert.jsonObjectHaveKey(reqJson, "roomId", "请求报文中未包含roomId节点");
        Assert.jsonObjectHaveKey(reqJson, "ownerName", "请求报文中未包含ownerName节点");
        Assert.jsonObjectHaveKey(reqJson, "tel", "请求报文中未包含tel节点");
        Assert.jsonObjectHaveKey(reqJson, "startTime", "请求报文中未包含tel节点");
        Assert.jsonObjectHaveKey(reqJson, "endTime", "请求报文中未包含tel节点");
        Assert.jsonObjectHaveKey(reqJson, "shopsState", "请求报文中未包含状态节点");
    }

    @Override
    public void doCmd(CmdEvent event, ICmdDataFlowContext context, JSONObject reqJson) throws CmdException {
        int flag = 0;
        if (!reqJson.containsKey("ownerId")
                || reqJson.getString("ownerId").startsWith("-")
                || StringUtil.isEmpty(reqJson.getString("ownerId"))
        ) {
            OwnerPo ownerPo = new OwnerPo();
            ownerPo.setUserId("-1");
            ownerPo.setAge("1");
            ownerPo.setCommunityId(reqJson.getString("communityId"));
            ownerPo.setIdCard("");
            ownerPo.setLink(reqJson.getString("tel"));
            if (reqJson.containsKey("areaCode")) {
                ownerPo.setLink(reqJson.getString("areaCode"));
            }
            ownerPo.setSex("1");
            ownerPo.setMemberId(GenerateCodeFactory.getGeneratorId(GenerateCodeFactory.CODE_PREFIX_ownerId));
            ownerPo.setName(reqJson.getString("ownerName"));
            ownerPo.setOwnerId(ownerPo.getMemberId()); //业主 所以和成员ID需要一样
            ownerPo.setOwnerTypeCd(OwnerDto.OWNER_TYPE_CD_OWNER);
            ownerPo.setRemark(reqJson.getString("remark"));
            ownerPo.setState("2000");
            flag = ownerV1InnerServiceSMOImpl.saveOwner(ownerPo);
            if (flag < 1) {
                throw new IllegalArgumentException("保存业主失败");
            }
            reqJson.put("ownerId", ownerPo.getOwnerId());
        }

        //查询商铺是否为 出租 或者空闲
        RoomDto roomDto = new RoomDto();
        roomDto.setRoomId(reqJson.getString("roomId"));
        roomDto.setCommunityId(reqJson.getString("communityId"));
        List<RoomDto> roomDtos = roomInnerServiceSMOImpl.queryRooms(roomDto);

        Assert.listOnlyOne(roomDtos, "商铺不存在");

        if (!"2006,2008".contains(roomDtos.get(0).getState())) {
            throw new IllegalArgumentException("当前商铺状态不允许操作");
        }

        //判断房屋是有租客
        OwnerRoomRelDto ownerRoomRelDto = new OwnerRoomRelDto();
        ownerRoomRelDto.setRoomId(reqJson.getString("roomId"));
        List<OwnerRoomRelDto> ownerRoomRelDtos = ownerRoomRelInnerServiceSMOImpl.queryOwnerRoomRels(ownerRoomRelDto);

        if (ownerRoomRelDtos != null && ownerRoomRelDtos.size() > 0) {
            JSONObject businessUnit = new JSONObject();
            businessUnit.put("relId", ownerRoomRelDtos.get(0).getRelId());
            OwnerRoomRelPo roomPo = BeanConvertUtil.covertBean(businessUnit, OwnerRoomRelPo.class);
            flag = ownerRoomRelV1InnerServiceSMOImpl.deleteOwnerRoomRel(roomPo);
            if (flag < 1) {
                throw new IllegalArgumentException("删除已有房屋关系失败");
            }
        }

        //添加房屋关系
        reqJson.put("state", "2001");
        JSONObject businessUnit = new JSONObject();
        businessUnit.putAll(reqJson);
        businessUnit.put("relId", GenerateCodeFactory.getGeneratorId(GenerateCodeFactory.CODE_PREFIX_relId));
        businessUnit.put("userId", "-1");
        OwnerRoomRelPo ownerRoomRelPo = BeanConvertUtil.covertBean(businessUnit, OwnerRoomRelPo.class);
        ownerRoomRelV1InnerServiceSMOImpl.saveOwnerRoomRel(ownerRoomRelPo);
        //更新房屋信息为售出
        reqJson.put("state", reqJson.getString("shopsState"));
        businessUnit = new JSONObject();
        businessUnit.putAll(reqJson);
        businessUnit.put("userId", "-1");
        RoomPo roomPo = BeanConvertUtil.covertBean(businessUnit, RoomPo.class);
        flag = roomV1InnerServiceSMOImpl.updateRoom(roomPo);
        if (flag < 1) {
            throw new IllegalArgumentException("删除已有房屋关系失败");
        }
    }


}
