package com.newland.property.community.cmd.floor;

import com.alibaba.fastjson.JSONObject;
import com.newland.property.core.annotation.NewlandPropertyCmd;
import com.newland.property.core.annotation.PropertyTransactional;
import com.newland.property.core.context.ICmdDataFlowContext;
import com.newland.property.core.event.cmd.Cmd;
import com.newland.property.core.event.cmd.CmdEvent;
import com.newland.property.dto.room.RoomDto;
import com.newland.property.dto.unit.UnitDto;
import com.newland.property.intf.community.IFloorInnerServiceSMO;
import com.newland.property.intf.community.IFloorV1InnerServiceSMO;
import com.newland.property.intf.community.IRoomV1InnerServiceSMO;
import com.newland.property.intf.community.IUnitV1InnerServiceSMO;
import com.newland.property.po.floor.FloorPo;
import com.newland.property.po.unit.UnitPo;
import com.newland.property.doc.annotation.*;
import com.newland.property.utils.exception.CmdException;
import com.newland.property.utils.util.Assert;
import com.newland.property.utils.util.BeanConvertUtil;
import com.newland.property.vo.ResultVo;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;


@NewlandPropertyCmdDoc(title = "删除楼栋",
        description = "用于外系统删除楼栋信息功能",
        httpMethod = "post",
        url = "http://{ip}:{port}/app/floor.deleteFloor",
        resource = "communityDoc",
        author = "吴学文",
        serviceCode = "floor.deleteFloor",
        seq = 7
)

@NewlandPropertyParamsDoc(params = {
        @NewlandPropertyParamDoc(name = "communityId", length = 30, remark = "小区ID"),
        @NewlandPropertyParamDoc(name = "floorId", length = 30, remark = "楼栋ID"),
})

@NewlandPropertyResponseDoc(
        params = {
                @NewlandPropertyParamDoc(name = "code", type = "int", length = 11, defaultValue = "0", remark = "返回编号，0 成功 其他失败"),
                @NewlandPropertyParamDoc(name = "msg", type = "String", length = 250, defaultValue = "成功", remark = "描述"),
        }
)

@NewlandPropertyExampleDoc(
        reqBody = "{\"floorId\":\"123123\",\"communityId\":\"2022081539020475\"}",
        resBody = "{'code':0,'msg':'成功'}"
)

@NewlandPropertyCmd(serviceCode = "floor.deleteFloor")
public class DeleteFloorCmd extends Cmd {
    @Autowired
    private IFloorInnerServiceSMO floorInnerServiceSMOImpl;

    @Autowired
    private IFloorV1InnerServiceSMO floorV1InnerServiceSMOImpl;

    @Autowired
    private IUnitV1InnerServiceSMO unitV1InnerServiceSMOImpl;

    @Autowired
    private IRoomV1InnerServiceSMO roomV1InnerServiceSMOImpl;

    @Override
    public void validate(CmdEvent event, ICmdDataFlowContext cmdDataFlowContext, JSONObject reqJson) {
        Assert.jsonObjectHaveKey(reqJson, "floorId", "请求报文中未包含floorId");
        Assert.jsonObjectHaveKey(reqJson, "communityId", "请求报文中未包含communityId");

//        UnitDto unitDto = new UnitDto();
//        unitDto.setFloorId(reqJson.getString("floorId"));
//        unitDto.setCommunityId(reqJson.getString("communityId"));
////        unitDto.setRoomUnit(UnitDto.ROOM_UNIT_Y);
//        int count = unitV1InnerServiceSMOImpl.queryUnitsCount(unitDto);
//        if (count > 0) {
//            throw new IllegalArgumentException("请先删除单元 再删除楼栋");
//        }

        //todo 校验 楼栋下是否有 房屋或者商铺
        RoomDto roomDto = new RoomDto();
        roomDto.setFloorId(reqJson.getString("floorId"));
        roomDto.setCommunityId(reqJson.getString("communityId"));
        int count = roomV1InnerServiceSMOImpl.queryRoomsCount(roomDto);
        if (count > 0) {
            throw new IllegalArgumentException("请先删除楼栋下的房屋或者商铺");
        }
    }

    @Override
    @PropertyTransactional
    public void doCmd(CmdEvent event, ICmdDataFlowContext cmdDataFlowContext, JSONObject reqJson) throws CmdException {
        FloorPo floorPo = BeanConvertUtil.covertBean(reqJson, FloorPo.class);
        int flag = floorV1InnerServiceSMOImpl.deleteFloor(floorPo);

        if (flag < 1) {
            throw new CmdException("删除楼栋失败");
        }

        //todo 删除楼栋下的单元
        UnitDto unitDto = new UnitDto();
        unitDto.setFloorId(reqJson.getString("floorId"));
        unitDto.setCommunityId(reqJson.getString("communityId"));
        List<UnitDto> unitDtos = unitV1InnerServiceSMOImpl.queryUnits(unitDto);

        if (unitDtos == null || unitDtos.size() < 1) {
            return;
        }

        UnitPo unitPo = null;
        for (UnitDto tmpUnitDto : unitDtos) {
            unitPo = new UnitPo();
            unitPo.setUnitId(tmpUnitDto.getUnitId());
            unitV1InnerServiceSMOImpl.deleteUnit(unitPo);
        }


        cmdDataFlowContext.setResponseEntity(ResultVo.success());
    }
}
