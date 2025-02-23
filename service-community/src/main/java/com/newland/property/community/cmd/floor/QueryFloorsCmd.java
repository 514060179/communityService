package com.newland.property.community.cmd.floor;

import com.alibaba.fastjson.JSONObject;
import com.newland.property.core.annotation.NewlandPropertyCmd;
import com.newland.property.core.context.ICmdDataFlowContext;
import com.newland.property.core.event.cmd.Cmd;
import com.newland.property.core.event.cmd.CmdEvent;
import com.newland.property.dto.floor.FloorDto;
import com.newland.property.intf.community.IFloorInnerServiceSMO;
import com.newland.property.doc.annotation.*;
import com.newland.property.utils.exception.CmdException;
import com.newland.property.utils.util.Assert;
import com.newland.property.utils.util.BeanConvertUtil;
import com.newland.property.vo.api.ApiFloorDataVo;
import com.newland.property.vo.api.ApiFloorVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.List;


@NewlandPropertyCmdDoc(title = "查询楼栋",
        description = "用于外系统查询楼栋信息功能",
        httpMethod = "get",
        url = "http://{ip}:{port}/app/floor.queryFloors",
        resource = "communityDoc",
        author = "吴学文",
        serviceCode = "floor.queryFloors",
        seq = 8
)

@NewlandPropertyParamsDoc(params = {
        @NewlandPropertyParamDoc(name = "communityId", length = 30, remark = "小区ID"),
        @NewlandPropertyParamDoc(name = "page", type = "int",length = 11, remark = "页数"),
        @NewlandPropertyParamDoc(name = "row", type = "int",length = 11, remark = "行数"),
})

@NewlandPropertyResponseDoc(
        params = {
                @NewlandPropertyParamDoc(name = "code", type = "int", length = 11, defaultValue = "0", remark = "返回编号，0 成功 其他失败"),
                @NewlandPropertyParamDoc(name = "msg", type = "String", length = 250, defaultValue = "成功", remark = "描述"),
                @NewlandPropertyParamDoc(name = "apiFloorDataVoList", type = "Array", remark = "有效数据"),
                @NewlandPropertyParamDoc(parentNodeName = "apiFloorDataVoList",name = "floorId", type = "String", remark = "楼栋ID"),
                @NewlandPropertyParamDoc(parentNodeName = "apiFloorDataVoList",name = "floorNum", type = "String", remark = "楼栋编号"),
        }
)

@NewlandPropertyExampleDoc(
        reqBody="http://{ip}:{port}/app/floor.queryFloors?page=1&row=10&communityId=123123",
        resBody="{'code':0,'msg':'成功','apiFloorDataVoList':[{'floorId':'123123','floorNum':'123213'}]}"
)

@NewlandPropertyCmd(serviceCode = "floor.queryFloors")
public class QueryFloorsCmd extends Cmd {

    @Autowired
    private IFloorInnerServiceSMO floorInnerServiceSMOImpl;

    @Override
    public void validate(CmdEvent event, ICmdDataFlowContext cmdDataFlowContext, JSONObject reqJson) {
        Assert.jsonObjectHaveKey(reqJson, "page", "请求中未包含page信息");
        Assert.jsonObjectHaveKey(reqJson, "row", "请求中未包含row信息");
        Assert.jsonObjectHaveKey(reqJson, "communityId", "请求中未包含communityId信息");
        Assert.isInteger(reqJson.getString("page"), "page不是有效数字");
        Assert.isInteger(reqJson.getString("row"), "row不是有效数字");
    }

    @Override
    public void doCmd(CmdEvent event, ICmdDataFlowContext cmdDataFlowContext, JSONObject reqJson) throws CmdException {
        //int page = reqJson.getInteger("page");
        int row = reqJson.getInteger("row");
        //String communityId = reqJson.getString("communityId");

        ApiFloorVo apiFloorVo = new ApiFloorVo();

        //查询总记录数
        int total = floorInnerServiceSMOImpl.queryFloorsCount(BeanConvertUtil.covertBean(reqJson, FloorDto.class));
        apiFloorVo.setTotal(total);
        if (total > 0) {
            List<FloorDto> floorDtoList = floorInnerServiceSMOImpl.queryFloors(BeanConvertUtil.covertBean(reqJson, FloorDto.class));
            apiFloorVo.setApiFloorDataVoList(BeanConvertUtil.covertBeanList(floorDtoList, ApiFloorDataVo.class));
        }

        apiFloorVo.setRecords((int) Math.ceil((double) total / (double) row));

        ResponseEntity<String> responseEntity = new ResponseEntity<String>(JSONObject.toJSONString(apiFloorVo), HttpStatus.OK);
        cmdDataFlowContext.setResponseEntity(responseEntity);
    }
}
