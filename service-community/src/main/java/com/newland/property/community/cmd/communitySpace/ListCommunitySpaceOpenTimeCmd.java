package com.newland.property.community.cmd.communitySpace;

import com.alibaba.fastjson.JSONObject;
import com.newland.property.core.annotation.NewlandPropertyCmd;
import com.newland.property.core.context.ICmdDataFlowContext;
import com.newland.property.core.event.cmd.Cmd;
import com.newland.property.core.event.cmd.CmdEvent;
import com.newland.property.core.log.LoggerFactory;
import com.newland.property.dto.community.CommunitySpaceOpenTimeDto;
import com.newland.property.intf.community.ICommunitySpaceOpenTimeV1InnerServiceSMO;
import com.newland.property.doc.annotation.*;
import com.newland.property.utils.exception.CmdException;
import com.newland.property.utils.util.Assert;
import com.newland.property.vo.ResultVo;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.text.ParseException;
import java.util.List;

@NewlandPropertyCmdDoc(title = "查询可预约时间场地",
        description = "查询系统中的查询可预约时间场地",
        httpMethod = "get",
        url = "http://{ip}:{port}/app/communitySpace.listCommunitySpaceOpenTime",
        resource = "communityDoc",
        author = "吴学文",
        serviceCode = "communitySpace.listCommunitySpaceOpenTime"
)

@NewlandPropertyParamsDoc(params = {
        @NewlandPropertyParamDoc(name = "spaceId", length = 30, remark = "场地ID"),
        @NewlandPropertyParamDoc(name = "communityId", length = 30, remark = "小区ID"),
        @NewlandPropertyParamDoc(name = "appointmentTime", length = 30, remark = "预约日期 YYYY-MM-DD"),
})

@NewlandPropertyResponseDoc(
        params = {
                @NewlandPropertyParamDoc(name = "code", type = "int", length = 11, defaultValue = "0", remark = "返回编号，0 成功 其他失败"),
                @NewlandPropertyParamDoc(name = "msg", type = "String", length = 250, defaultValue = "成功", remark = "描述"),
                @NewlandPropertyParamDoc(name = "data", type = "Array", remark = "有效数据"),
                @NewlandPropertyParamDoc(parentNodeName = "data", name = "hours", type = "String", remark = "小时"),
        }
)

@NewlandPropertyExampleDoc(
        reqBody = "http://{ip}:{port}/app/communitySpace.listCommunitySpaceOpenTime?spaceId=123&appointmentTime=2022-01-01&communityId=2022081539020475",
        resBody = "{\"code\":0,\"data\":[{\"hours\":1,\"hours\":2}],\"msg\":\"成功\",\"page\":0,\"records\":1,\"rows\":0,\"total\":2}"
)
@NewlandPropertyCmd(serviceCode = "communitySpace.listCommunitySpaceOpenTime")
public class ListCommunitySpaceOpenTimeCmd extends Cmd{

    private static Logger logger = LoggerFactory.getLogger(ListCommunitySpacePersonCmd.class);


    @Autowired
    private ICommunitySpaceOpenTimeV1InnerServiceSMO communitySpaceOpenTimeV1InnerServiceSMOImpl;

    @Override
    public void validate(CmdEvent event, ICmdDataFlowContext context, JSONObject reqJson) throws CmdException {

        Assert.hasKeyAndValue(reqJson,"appointmentTime","预约时间");
        Assert.hasKeyAndValue(reqJson,"communityId","小区ID");
    }

    @Override
    public void doCmd(CmdEvent event, ICmdDataFlowContext context, JSONObject reqJson) throws CmdException, ParseException {

        CommunitySpaceOpenTimeDto communitySpaceOpenTimeDto = new CommunitySpaceOpenTimeDto();
        communitySpaceOpenTimeDto.setSpaceId(reqJson.getString("spaceId"));
        communitySpaceOpenTimeDto.setVenueId(reqJson.getString("venueId"));
        communitySpaceOpenTimeDto.setAppointmentTime(reqJson.getString("appointmentTime"));
        List<CommunitySpaceOpenTimeDto> communitySpaceOpenTimeDtos = communitySpaceOpenTimeV1InnerServiceSMOImpl.queryCommunitySpaceOpenTimes(communitySpaceOpenTimeDto);

        ResultVo resultVo = new ResultVo(1, communitySpaceOpenTimeDtos.size(), communitySpaceOpenTimeDtos);

        ResponseEntity<String> responseEntity = new ResponseEntity<String>(resultVo.toString(), HttpStatus.OK);

        context.setResponseEntity(responseEntity);
    }
}
