package com.newland.property.community.cmd.floor;

import com.alibaba.fastjson.JSONObject;
import com.newland.property.core.annotation.NewlandPropertyCmd;
import com.newland.property.core.annotation.PropertyTransactional;
import com.newland.property.core.context.ICmdDataFlowContext;
import com.newland.property.core.event.cmd.Cmd;
import com.newland.property.core.event.cmd.CmdEvent;
import com.newland.property.core.factory.GenerateCodeFactory;
import com.newland.property.dto.floor.FloorDto;
import com.newland.property.intf.community.IFloorInnerServiceSMO;
import com.newland.property.intf.community.IFloorV1InnerServiceSMO;
import com.newland.property.po.floor.FloorPo;
import com.newland.property.doc.annotation.*;
import com.newland.property.utils.constant.ResponseConstant;
import com.newland.property.utils.exception.CmdException;
import com.newland.property.utils.exception.ListenerExecuteException;
import com.newland.property.utils.util.Assert;
import com.newland.property.utils.util.BeanConvertUtil;
import com.newland.property.vo.ResultVo;
import org.springframework.beans.factory.annotation.Autowired;


@NewlandPropertyCmdDoc(title = "添加楼栋",
        description = "用于外系统添加楼栋信息功能",
        httpMethod = "post",
        url = "http://{ip}:{port}/app/floor.saveFloor",
        resource = "communityDoc",
        author = "吴学文",
        serviceCode = "floor.saveFloor",
        seq = 5
)

@NewlandPropertyParamsDoc(params = {
        @NewlandPropertyParamDoc(name = "communityId", length = 30, remark = "小区ID"),
        @NewlandPropertyParamDoc(name = "name", length = 64, remark = "名称"),
        @NewlandPropertyParamDoc(name = "floorArea", length = 64, remark = "面积"),
        @NewlandPropertyParamDoc(name = "floorNum", length = 64, remark = "编号"),
        @NewlandPropertyParamDoc(name = "seq", type = "int", length = 11, remark = "排序"),
})

@NewlandPropertyResponseDoc(
        params = {
                @NewlandPropertyParamDoc(name = "code", type = "int", length = 11, defaultValue = "0", remark = "返回编号，0 成功 其他失败"),
                @NewlandPropertyParamDoc(name = "msg", type = "String", length = 250, defaultValue = "成功", remark = "描述"),
        }
)

@NewlandPropertyExampleDoc(
        reqBody = "{\"floorId\":\"\",\"name\":\"2号楼\",\"floorName\":\"\",\"floorArea\":\"22\",\"floorNum\":\"2\",\"remark\":\"2\",\"errorInfo\":\"\",\"seq\":\"22\",\"communityId\":\"2022081539020475\"}",
        resBody = "{'code':0,'msg':'成功'}"
)


@NewlandPropertyCmd(serviceCode = "floor.saveFloor")
public class SaveFloorCmd extends Cmd {
    @Autowired
    private IFloorInnerServiceSMO floorInnerServiceSMOImpl;

    @Autowired
    private IFloorV1InnerServiceSMO floorV1InnerServiceSMOImpl;

    @Override
    public void validate(CmdEvent event, ICmdDataFlowContext cmdDataFlowContext, JSONObject reqJson) {
        Assert.jsonObjectHaveKey(reqJson, "name", "请求报文中未包含name");
        Assert.jsonObjectHaveKey(reqJson, "userId", "请求报文中未包含userId");
        Assert.jsonObjectHaveKey(reqJson, "floorNum", "请求报文中未包含floorNum");
        Assert.jsonObjectHaveKey(reqJson, "communityId", "请求报文中未包含communityId");
        FloorDto floorDto = new FloorDto();
        floorDto.setFloorNum(reqJson.getString("floorNum"));
        floorDto.setCommunityId(reqJson.getString("communityId"));


        int floorCount = floorInnerServiceSMOImpl.queryFloorsCount(floorDto);

        if (floorCount > 0) {
            throw new ListenerExecuteException(ResponseConstant.RESULT_CODE_ERROR, "楼栋编号已经存在");
        }

        floorDto = new FloorDto();
        floorDto.setFloorName(reqJson.getString("name"));
        floorDto.setCommunityId(reqJson.getString("communityId"));

        floorCount = floorInnerServiceSMOImpl.queryFloorsCount(floorDto);

        if (floorCount > 0) {
            throw new ListenerExecuteException(ResponseConstant.RESULT_CODE_ERROR, "楼栋名称已经存在");
        }
    }

    @Override
    @PropertyTransactional
    public void doCmd(CmdEvent event, ICmdDataFlowContext cmdDataFlowContext, JSONObject reqJson) throws CmdException {
        String floorId = GenerateCodeFactory.getGeneratorId(GenerateCodeFactory.CODE_PREFIX_floorId);
        JSONObject businessFloor = new JSONObject();
        businessFloor.put("floorId", floorId);
        businessFloor.put("name", reqJson.getString("name"));
        businessFloor.put("remark", reqJson.getString("remark"));
        businessFloor.put("userId", reqJson.getString("userId"));
        businessFloor.put("floorNum", reqJson.getString("floorNum"));
        businessFloor.put("communityId", reqJson.getString("communityId"));
        businessFloor.put("floorArea", reqJson.getString("floorArea"));
        businessFloor.put("seq", reqJson.getString("seq"));
        FloorPo floorPo = BeanConvertUtil.covertBean(businessFloor, FloorPo.class);
        int flag = floorV1InnerServiceSMOImpl.saveFloor(floorPo);

        if (flag < 1) {
            throw new CmdException("保存楼栋失败");
        }

        cmdDataFlowContext.setResponseEntity(ResultVo.success());
    }
}
