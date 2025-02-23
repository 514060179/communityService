package com.newland.property.community.cmd.parkingArea;

import com.alibaba.fastjson.JSONObject;
import com.newland.property.core.annotation.NewlandPropertyCmd;
import com.newland.property.core.context.ICmdDataFlowContext;
import com.newland.property.core.event.cmd.Cmd;
import com.newland.property.core.event.cmd.CmdEvent;
import com.newland.property.dto.parking.ParkingAreaDto;
import com.newland.property.intf.community.IParkingAreaInnerServiceSMO;
import com.newland.property.doc.annotation.*;
import com.newland.property.utils.exception.CmdException;
import com.newland.property.utils.util.Assert;
import com.newland.property.utils.util.BeanConvertUtil;
import com.newland.property.vo.api.parkingArea.ApiParkingAreaDataVo;
import com.newland.property.vo.api.parkingArea.ApiParkingAreaVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.ArrayList;
import java.util.List;


@NewlandPropertyCmdDoc(title = "查询停车场",
        description = "用户业主手机端赠送停车劵时使用，也可以用于其他的场景，根据小区ID查询，当前小区的所有停车场",
        httpMethod = "get",
        url = "http://{ip}:{port}/app/parkingArea.listParkingAreas",
        resource = "communityDoc",
        author = "吴学文",
        serviceCode = "parkingArea.listParkingAreas",
        seq = 20
)

@NewlandPropertyParamsDoc(params = {
        @NewlandPropertyParamDoc(name = "communityId", length = 30, remark = "小区ID"),
        @NewlandPropertyParamDoc(name = "page",type = "int",length = 11, remark = "分页页数"),
        @NewlandPropertyParamDoc(name = "row",type = "int", length = 11, remark = "分页行数"),
})

@NewlandPropertyResponseDoc(
        params = {
                @NewlandPropertyParamDoc(name = "code", type = "int", length = 11, defaultValue = "0", remark = "返回编号，0 成功 其他失败"),
                @NewlandPropertyParamDoc(name = "msg", type = "String", length = 250, defaultValue = "成功", remark = "描述"),
                @NewlandPropertyParamDoc(name = "parkingAreas", type = "Array", remark = "有效数据"),
                @NewlandPropertyParamDoc(parentNodeName = "parkingAreas",name = "paId", type = "String", remark = "停车场ID"),
                @NewlandPropertyParamDoc(parentNodeName = "parkingAreas",name = "num", type = "String", remark = "停车场编号"),
        }
)

@NewlandPropertyExampleDoc(
        reqBody="http://{ip}:{port}/app/parkingArea.listParkingAreas?num=&typeCd=&paId=&page=1&row=10&communityId=2022112555490011",
        resBody="{\"page\":0,\"parkingAreas\":[{\"attrs\":[{\"attrId\":\"112022112796270047\",\"communityId\":\"2022112555490011\",\"listShow\":\"Y\",\"paId\":\"102022112706900045\",\"page\":-1,\"records\":0,\"row\":0,\"specCd\":\"6185-17861\",\"specName\":\"外部编码\",\"specType\":\"2233\",\"statusCd\":\"0\",\"total\":0,\"value\":\"123\"}],\"createTime\":\"2022-11-27 01:48:27\",\"num\":\"A\",\"paId\":\"102022112706900045\",\"remark\":\"\",\"typeCd\":\"1001\"}],\"records\":1,\"rows\":0,\"total\":1}"
)
@NewlandPropertyCmd(serviceCode = "parkingArea.listParkingAreas")
public class ListParkingAreasCmd extends Cmd {

    @Autowired
    private IParkingAreaInnerServiceSMO parkingAreaInnerServiceSMOImpl;

    @Override
    public void validate(CmdEvent event, ICmdDataFlowContext cmdDataFlowContext, JSONObject reqJson) throws CmdException {
        Assert.hasKeyAndValue(reqJson, "communityId", "必填，请填写小区信息");
        super.validatePageInfo(reqJson);
    }

    @Override
    public void doCmd(CmdEvent event, ICmdDataFlowContext cmdDataFlowContext, JSONObject reqJson) throws CmdException {
        ParkingAreaDto parkingAreaDto = BeanConvertUtil.covertBean(reqJson, ParkingAreaDto.class);

        int count = parkingAreaInnerServiceSMOImpl.queryParkingAreasCount(parkingAreaDto);

        List<ApiParkingAreaDataVo> parkingAreas = null;
        ApiParkingAreaDataVo parkingAreaDataVo = null;
        List<ParkingAreaDto> parkingAreaDtos = null;

        if (count > 0) {
            parkingAreas = new ArrayList<>();
            parkingAreaDtos = parkingAreaInnerServiceSMOImpl.queryParkingAreas(parkingAreaDto);

            for (ParkingAreaDto tmpParkingAreaDto : parkingAreaDtos) {
                parkingAreaDataVo = BeanConvertUtil.covertBean(tmpParkingAreaDto, ApiParkingAreaDataVo.class);
                parkingAreaDataVo.setAttrs(tmpParkingAreaDto.getAttrs());
                parkingAreas.add(parkingAreaDataVo);
            }
        } else {
            parkingAreas = new ArrayList<>();
        }

        ApiParkingAreaVo apiParkingAreaVo = new ApiParkingAreaVo();

        apiParkingAreaVo.setTotal(count);
        apiParkingAreaVo.setRecords((int) Math.ceil((double) count / (double) reqJson.getInteger("row")));
        apiParkingAreaVo.setParkingAreas(parkingAreas);

        ResponseEntity<String> responseEntity = new ResponseEntity<String>(JSONObject.toJSONString(apiParkingAreaVo), HttpStatus.OK);

        cmdDataFlowContext.setResponseEntity(responseEntity);
    }
}
