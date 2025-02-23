package com.newland.property.community.cmd.unit;

import com.alibaba.fastjson.JSONObject;
import com.newland.property.core.annotation.NewlandPropertyCmd;
import com.newland.property.core.context.ICmdDataFlowContext;
import com.newland.property.core.event.cmd.Cmd;
import com.newland.property.core.event.cmd.CmdEvent;
import com.newland.property.dto.unit.UnitDto;
import com.newland.property.dto.data.DataPrivilegeStaffDto;
import com.newland.property.intf.community.IDataPrivilegeUnitV1InnerServiceSMO;
import com.newland.property.intf.community.IFloorInnerServiceSMO;
import com.newland.property.intf.community.IUnitInnerServiceSMO;
import com.newland.property.doc.annotation.*;
import com.newland.property.utils.exception.CmdException;
import com.newland.property.utils.util.Assert;
import com.newland.property.utils.util.BeanConvertUtil;
import com.newland.property.vo.api.ApiUnitVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.List;


@NewlandPropertyCmdDoc(title = "查询单元",
        description = "用于外系统查询单元信息功能",
        httpMethod = "get",
        url = "http://{ip}:{port}/app/unit.queryUnits",
        resource = "communityDoc",
        author = "吴学文",
        serviceCode = "unit.queryUnits",
        seq = 12
)

@NewlandPropertyParamsDoc(params = {
        @NewlandPropertyParamDoc(name = "communityId", length = 30, remark = "小区ID"),
        @NewlandPropertyParamDoc(name = "floorId", length = 30, remark = "楼栋ID"),
        @NewlandPropertyParamDoc(name = "page", type = "int",length = 11, remark = "页数"),
        @NewlandPropertyParamDoc(name = "row", type = "int",length = 11, remark = "行数"),
})

@NewlandPropertyResponseDoc(
        params = {
                @NewlandPropertyParamDoc(name = "unitId", type = "String", length = 30,  remark = "单元ID"),
                @NewlandPropertyParamDoc(name = "unitNum",  type = "String", length = 250,  remark = "单元编号"),
                @NewlandPropertyParamDoc(name = "seq",  type = "int", length = 11,  remark = "排序"),
        }
)

@NewlandPropertyExampleDoc(
        reqBody="http://{ip}:{port}/app/unit.queryUnits?page=1&row=10&communityId=123123&floorId=123",
        resBody="{'unitId':'123123','unitNum':'123123','seq':1}"
)

@NewlandPropertyCmd(serviceCode = "unit.queryUnits")
public class QueryUnitsCmd extends Cmd {
    @Autowired
    private IUnitInnerServiceSMO unitInnerServiceSMOImpl;

    @Autowired
    private IFloorInnerServiceSMO floorInnerServiceSMOImpl;

    @Autowired
    private IDataPrivilegeUnitV1InnerServiceSMO dataPrivilegeUnitV1InnerServiceSMOImpl;

    @Override
    public void validate(CmdEvent event, ICmdDataFlowContext cmdDataFlowContext, JSONObject reqJson) {
        Assert.jsonObjectHaveKey(reqJson, "communityId", "请求中未包含communityId信息");
        //Assert.jsonObjectHaveKey(reqJson, "floorId", "请求中未包含floorId信息");
        //校验小区楼ID和小区是否有对应关系
//        int total = floorInnerServiceSMOImpl.queryFloorsCount(BeanConvertUtil.covertBean(reqJson, FloorDto.class));
//
//        if (total < 1) {
//            throw new IllegalArgumentException("传入小区楼ID不是该小区的楼");
//        }
    }

    @Override
    public void doCmd(CmdEvent event, ICmdDataFlowContext cmdDataFlowContext, JSONObject reqJson) throws CmdException {
        UnitDto unitDto = BeanConvertUtil.covertBean(reqJson, UnitDto.class);
        unitDto.setUserId("");

        String staffId = cmdDataFlowContext.getReqHeaders().get("user-id");
        DataPrivilegeStaffDto dataPrivilegeStaffDto = new DataPrivilegeStaffDto();
        dataPrivilegeStaffDto.setStaffId(staffId);
        String[] unitIds = dataPrivilegeUnitV1InnerServiceSMOImpl.queryDataPrivilegeUnitsByStaff(dataPrivilegeStaffDto);

        if(unitIds != null && unitIds.length>0){
            unitDto.setUnitIds(unitIds);
        }

        List<UnitDto> unitDtoList = unitInnerServiceSMOImpl.queryUnits(unitDto);

        List<ApiUnitVo> apiUnitVos = BeanConvertUtil.covertBeanList(unitDtoList, ApiUnitVo.class);

        ResponseEntity<String> responseEntity = new ResponseEntity<String>(JSONObject.toJSONString(apiUnitVos), HttpStatus.OK);
        cmdDataFlowContext.setResponseEntity(responseEntity);
    }
}
