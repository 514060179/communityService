package com.newland.property.community.cmd.unit;

import com.alibaba.fastjson.JSONObject;
import com.newland.property.core.annotation.NewlandPropertyCmd;
import com.newland.property.core.annotation.PropertyTransactional;
import com.newland.property.core.context.ICmdDataFlowContext;
import com.newland.property.core.event.cmd.Cmd;
import com.newland.property.core.event.cmd.CmdEvent;
import com.newland.property.dto.floor.FloorDto;
import com.newland.property.dto.room.RoomDto;
import com.newland.property.dto.unit.UnitDto;
import com.newland.property.intf.community.IFloorInnerServiceSMO;
import com.newland.property.intf.community.IRoomV1InnerServiceSMO;
import com.newland.property.intf.community.IUnitInnerServiceSMO;
import com.newland.property.intf.community.IUnitV1InnerServiceSMO;
import com.newland.property.po.unit.UnitPo;
import com.newland.property.doc.annotation.*;
import com.newland.property.utils.exception.CmdException;
import com.newland.property.utils.util.Assert;
import com.newland.property.utils.util.BeanConvertUtil;
import com.newland.property.vo.ResultVo;
import org.springframework.beans.factory.annotation.Autowired;



@NewlandPropertyCmdDoc(title = "删除单元",
        description = "用于外系统删除单元信息功能",
        httpMethod = "post",
        url = "http://{ip}:{port}/app/unit.deleteUnit",
        resource = "communityDoc",
        author = "吴学文",
        serviceCode = "unit.deleteUnit",
        seq = 11
)

@NewlandPropertyParamsDoc(params = {
        @NewlandPropertyParamDoc(name = "communityId", length = 30, remark = "小区ID"),
        @NewlandPropertyParamDoc(name = "unitId", length = 30, remark = "单元ID"),
})

@NewlandPropertyResponseDoc(
        params = {
                @NewlandPropertyParamDoc(name = "code", type = "int", length = 11, defaultValue = "0", remark = "返回编号，0 成功 其他失败"),
                @NewlandPropertyParamDoc(name = "msg", type = "String", length = 250, defaultValue = "成功", remark = "描述"),
        }
)

@NewlandPropertyExampleDoc(
        reqBody="{\"unitId\":\"123123\",\"communityId\":\"2022081539020475\"}",
        resBody="{'code':0,'msg':'成功'}"
)

@NewlandPropertyCmd(serviceCode = "unit.deleteUnit")
public class DeleteUnitCmd extends Cmd {
    @Autowired
    private IFloorInnerServiceSMO floorInnerServiceSMOImpl;

    @Autowired
    private IUnitInnerServiceSMO unitInnerServiceSMOImpl;

    @Autowired
    private IUnitV1InnerServiceSMO unitV1InnerServiceSMOImpl;

    @Autowired
    private IRoomV1InnerServiceSMO roomV1InnerServiceSMOImpl;

    @Override
    public void validate(CmdEvent event, ICmdDataFlowContext cmdDataFlowContext, JSONObject reqJson) {
        Assert.jsonObjectHaveKey(reqJson, "communityId", "请求报文中未包含communityId节点");
        Assert.jsonObjectHaveKey(reqJson, "floorId", "请求报文中未包含floorId节点");
        Assert.jsonObjectHaveKey(reqJson, "unitId", "请求报文中未包含unitId节点");

        FloorDto floorDto = new FloorDto();
        floorDto.setCommunityId(reqJson.getString("communityId"));
        floorDto.setFloorId(reqJson.getString("floorId"));
        //校验小区楼ID和小区是否有对应关系
        int total = floorInnerServiceSMOImpl.queryFloorsCount(floorDto);

        if (total < 1) {
            throw new IllegalArgumentException("传入小区楼ID不是该小区的楼");
        }

        //校验 小区楼ID 和单元ID是否有关系
        UnitDto unitDto = new UnitDto();
        unitDto.setFloorId(reqJson.getString("floorId"));
        unitDto.setUnitId(reqJson.getString("unitId"));
        total = unitInnerServiceSMOImpl.queryUnitsCount(unitDto);
        if (total < 1) {
            throw new IllegalArgumentException("传入单元不是该小区的楼的单元");
        }

        RoomDto roomDto = new RoomDto();
        roomDto.setUnitId(reqJson.getString("unitId"));
        roomDto.setCommunityId(reqJson.getString("communityId"));
        int count = roomV1InnerServiceSMOImpl.queryRoomsCount(roomDto);
        if(count > 0){
            throw new IllegalArgumentException("单元下存在房屋 请先删除房屋");

        }
    }

    @Override
    @PropertyTransactional
    public void doCmd(CmdEvent event, ICmdDataFlowContext cmdDataFlowContext, JSONObject reqJson) throws CmdException {
        JSONObject businessUnit = new JSONObject();
        businessUnit.put("unitId", reqJson.getString("unitId"));
        UnitPo unitPo = BeanConvertUtil.covertBean(businessUnit, UnitPo.class);
        int flag = unitV1InnerServiceSMOImpl.deleteUnit(unitPo);

        if (flag < 1) {
            throw new CmdException("删除单元失败");
        }

        cmdDataFlowContext.setResponseEntity(ResultVo.success());
    }
}
