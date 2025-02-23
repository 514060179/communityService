package com.newland.property.community.cmd.room;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.newland.property.core.annotation.NewlandPropertyCmd;
import com.newland.property.core.annotation.PropertyTransactional;
import com.newland.property.core.context.ICmdDataFlowContext;
import com.newland.property.core.event.cmd.Cmd;
import com.newland.property.core.event.cmd.CmdEvent;
import com.newland.property.core.factory.GenerateCodeFactory;
import com.newland.property.dto.room.RoomDto;
import com.newland.property.dto.unit.UnitDto;
import com.newland.property.intf.community.IRoomAttrV1InnerServiceSMO;
import com.newland.property.intf.community.IRoomV1InnerServiceSMO;
import com.newland.property.intf.community.IUnitInnerServiceSMO;
import com.newland.property.intf.user.IOwnerRoomRelV1InnerServiceSMO;
import com.newland.property.po.owner.OwnerRoomRelPo;
import com.newland.property.po.room.RoomAttrPo;
import com.newland.property.po.room.RoomPo;
import com.newland.property.doc.annotation.*;
import com.newland.property.utils.constant.CommonConstant;
import com.newland.property.utils.exception.CmdException;
import com.newland.property.utils.util.Assert;
import com.newland.property.utils.util.BeanConvertUtil;
import com.newland.property.utils.util.DateUtil;
import com.newland.property.utils.util.StringUtil;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

@NewlandPropertyCmdDoc(title = "添加房屋",
        description = "对应后台 添加房屋功能",
        httpMethod = "post",
        url = "http://{ip}:{port}/app/room.saveRoom",
        resource = "communityDoc",
        author = "吴学文",
        serviceCode = "room.saveRoom",
        seq = 13
)

@NewlandPropertyParamsDoc(params = {
        @NewlandPropertyParamDoc(name = "communityId", length = 30, remark = "小区ID"),
        @NewlandPropertyParamDoc(name = "roomNum", length = 64, remark = "房号"),
        @NewlandPropertyParamDoc(name = "layer", length = 12, remark = "层数"),
        @NewlandPropertyParamDoc(name = "unitId", length = 30, remark = "单元ID"),
        @NewlandPropertyParamDoc(name = "section", length = 12, remark = "房屋个数"),
        @NewlandPropertyParamDoc(name = "apartment", length = 12, remark = "固定 10102"),
        @NewlandPropertyParamDoc(name = "builtUpArea", length = 12, remark = "建筑面积"),
        @NewlandPropertyParamDoc(name = "feeCoefficient", length = 12, remark = "算费系数 固定1"),
        @NewlandPropertyParamDoc(name = "state", length = 12, remark = "状态 2002\t未销售"),
        @NewlandPropertyParamDoc(name = "roomSubType", length = 12, remark = "房屋类型 110\t住宅\n" +
                "120\t办公室\n" +
                "119\t宿舍"),
        @NewlandPropertyParamDoc(name = "roomArea", length = 12, remark = "室内面积"),
        @NewlandPropertyParamDoc(name = "roomRent", length = 12, remark = "办公室 或者宿舍 时租金"),
        @NewlandPropertyParamDoc(name = "remark", length = 512, remark = "备注"),
})

@NewlandPropertyResponseDoc(
        params = {
                @NewlandPropertyParamDoc(name = "code", type = "int", length = 11, defaultValue = "0", remark = "返回编号，0 成功 其他失败"),
                @NewlandPropertyParamDoc(name = "msg", type = "String", length = 250, defaultValue = "成功", remark = "描述"),
        }
)

@NewlandPropertyExampleDoc(
        reqBody = "{\n" +
                "\t\"roomNum\": \"88488\",\n" +
                "\t\"layer\": \"1\",\n" +
                "\t\"unitId\":\"123123123\",\n" +
                "\t\"section\": \"0\",\n" +
                "\t\"apartment\": \"10102\",\n" +
                "\t\"builtUpArea\": \"110\",\n" +
                "\t\"feeCoefficient\": \"1.00\",\n" +
                "\t\"state\": \"2002\",\n" +
                "\t\"remark\": \"sdf\",\n" +
                "\t\"roomSubType\": \"110\",\n" +
                "\t\"roomArea\": \"110\",\n" +
                "\t\"roomRent\": \"0\",\n" +
                "\t\"communityId\": \"2022121921870161\",\n" +
                "}",
        resBody = "{\"code\":0,\"msg\":\"成功\"}"
)

@NewlandPropertyCmd(serviceCode = "room.saveRoom")
public class SaveRoomCmd extends Cmd {

    @Autowired
    private IUnitInnerServiceSMO unitInnerServiceSMOImpl;

    @Autowired
    private IRoomV1InnerServiceSMO roomV1InnerServiceSMOImpl;

    @Autowired
    private IOwnerRoomRelV1InnerServiceSMO ownerRoomRelV1InnerServiceSMOImpl;

    @Autowired
    private IRoomAttrV1InnerServiceSMO roomAttrV1InnerServiceSMOImpl;

    @Override
    public void validate(CmdEvent event, ICmdDataFlowContext context, JSONObject reqJson) throws CmdException {
        Assert.jsonObjectHaveKey(reqJson, "communityId", "请求报文中未包含communityId节点");
        Assert.jsonObjectHaveKey(reqJson, "unitId", "请求报文中未包含unitId节点");
        Assert.jsonObjectHaveKey(reqJson, "roomNum", "请求报文中未包含roomNum节点");
        Assert.jsonObjectHaveKey(reqJson, "layer", "请求报文中未包含layer节点");
        Assert.jsonObjectHaveKey(reqJson, "section", "请求报文中未包含section节点");
        Assert.jsonObjectHaveKey(reqJson, "apartment", "请求报文中未包含apartment节点");
        Assert.jsonObjectHaveKey(reqJson, "state", "请求报文中未包含state节点");
        Assert.jsonObjectHaveKey(reqJson, "builtUpArea", "请求报文中未包含builtUpArea节点");
        Assert.jsonObjectHaveKey(reqJson, "feeCoefficient", "请求报文中未包含feeCoefficient节点");
        Assert.isInteger(reqJson.getString("section"), "房间数不是有效数字");
        Assert.isMoney(reqJson.getString("builtUpArea"), "建筑面积数据格式错误");
        Assert.isMoney(reqJson.getString("feeCoefficient"), "房屋单价数据格式错误");
        if (!reqJson.containsKey("roomSubType")) {
            reqJson.put("roomSubType", RoomDto.ROOM_SUB_TYPE_PERSON);
        }
        if (!reqJson.containsKey("roomRent")) {
            reqJson.put("roomRent", "0");
        }
        if (!reqJson.containsKey("roomArea")) {
            reqJson.put("roomRent", reqJson.getString("builtUpArea"));
        }
        if (!RoomDto.STATE_FREE.equals(reqJson.getString("state"))) {
            Assert.hasKeyAndValue(reqJson, "ownerId", "未包含业主信息");
        }
        /*if (!"1010".equals(reqJson.getString("apartment")) && !"2020".equals(reqJson.getString("apartment"))) {
            throw new IllegalArgumentException("不是有效房屋户型 传入数据错误");
        }*/
        if (!"2001".equals(reqJson.getString("state"))
                && !"2002".equals(reqJson.getString("state"))
                && !"2003".equals(reqJson.getString("state"))
                && !"2004".equals(reqJson.getString("state"))
                && !"2005".equals(reqJson.getString("state"))
                && !"2009".equals(reqJson.getString("state"))) {
            throw new IllegalArgumentException("不是有效房屋状态 传入数据错误");
        }
        UnitDto unitDto = new UnitDto();
        unitDto.setCommunityId(reqJson.getString("communityId"));
        unitDto.setUnitId(reqJson.getString("unitId"));
        //校验小区楼ID和小区是否有对应关系
        List<UnitDto> units = unitInnerServiceSMOImpl.queryUnitsByCommunityId(unitDto);
        if (units == null || units.size() < 1) {
            throw new IllegalArgumentException("传入单元ID不是该小区的单元");
        }
        reqJson.put("unitNum", units.get(0).getUnitNum());
    }

    @Override
    @PropertyTransactional
    public void doCmd(CmdEvent event, ICmdDataFlowContext context, JSONObject reqJson) throws CmdException {
        if ("0".equals(reqJson.getString("unitNum"))) { // 处理为商铺
            reqJson.put("roomType", RoomDto.ROOM_TYPE_SHOPS);
        } else {
            reqJson.put("roomType", RoomDto.ROOM_TYPE_ROOM);
        }
        reqJson.put("roomId", GenerateCodeFactory.getGeneratorId(GenerateCodeFactory.CODE_PREFIX_roomId));
        reqJson.put("userId", context.getReqHeaders().get(CommonConstant.HTTP_USER_ID));
        RoomPo roomPo = BeanConvertUtil.covertBean(reqJson, RoomPo.class);
        roomV1InnerServiceSMOImpl.saveRoom(roomPo);
        if (RoomDto.STATE_FREE.equals(roomPo.getState())) {
            return;
        }
        if (!reqJson.containsKey("startTime")) {
            reqJson.put("startTime", DateUtil.getNow(DateUtil.DATE_FORMATE_STRING_A));
        }
        if (!reqJson.containsKey("endTime")) {
            reqJson.put("endTime", "2037-01-01 00:00:00");
        }
        OwnerRoomRelPo ownerRoomRelPo = new OwnerRoomRelPo();
        ownerRoomRelPo.setRelId(GenerateCodeFactory.getGeneratorId(GenerateCodeFactory.CODE_PREFIX_relId));
        ownerRoomRelPo.setRoomId(roomPo.getRoomId());
        ownerRoomRelPo.setOwnerId(reqJson.getString("ownerId"));
        ownerRoomRelPo.setStartTime(reqJson.getString("startTime"));
        ownerRoomRelPo.setEndTime(reqJson.getString("endTime"));
        ownerRoomRelPo.setState("2001");
        ownerRoomRelPo.setRemark("添加房屋直接绑定");
        ownerRoomRelPo.setOperate("ADD");
        ownerRoomRelPo.setUserId("-1");
        int flag = ownerRoomRelV1InnerServiceSMOImpl.saveOwnerRoomRel(ownerRoomRelPo);
        if (flag < 1) {
            throw new CmdException("添加业主房屋关系");
        }
        JSONArray attrs = reqJson.getJSONArray("attrs");
        if (attrs == null || attrs.size() < 1) {
            return;
        }
        JSONObject attr = null;
        int count = 0;
        for (int attrIndex = 0; attrIndex < attrs.size(); attrIndex++) {
            attr = attrs.getJSONObject(attrIndex);
            attr.put("roomId", reqJson.getString("roomId"));
            if (!attr.containsKey("attrId") || attr.getString("attrId").startsWith("-") || StringUtil.isEmpty(attr.getString("attrId"))) {
                RoomAttrPo roomAttrPo = new RoomAttrPo();
                roomAttrPo.setAttrId(GenerateCodeFactory.getAttrId());
                roomAttrPo.setRoomId(attr.getString("roomId"));
                roomAttrPo.setSpecCd(attr.getString("specCd"));
                roomAttrPo.setValue(attr.getString("value"));
                count = roomAttrV1InnerServiceSMOImpl.saveRoomAttr(roomAttrPo);
                if (count < 1) {
                    throw new CmdException("保存单元失败");
                }
                continue;
            }
            RoomAttrPo roomAttrPo = new RoomAttrPo();
            roomAttrPo.setAttrId(attr.getString("attrId"));
            roomAttrPo.setRoomId(attr.getString("roomId"));
            roomAttrPo.setSpecCd(attr.getString("specCd"));
            roomAttrPo.setValue(attr.getString("value"));
            count = roomAttrV1InnerServiceSMOImpl.updateRoomAttr(roomAttrPo);
            if (count < 1) {
                throw new CmdException("保存单元失败");
            }
        }
    }
}
