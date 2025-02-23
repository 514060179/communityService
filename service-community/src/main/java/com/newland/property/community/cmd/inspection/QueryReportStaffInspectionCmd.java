package com.newland.property.community.cmd.inspection;

import com.alibaba.fastjson.JSONObject;
import com.newland.property.core.annotation.NewlandPropertyCmd;
import com.newland.property.core.context.ICmdDataFlowContext;
import com.newland.property.core.event.cmd.Cmd;
import com.newland.property.core.event.cmd.CmdEvent;
import com.newland.property.dto.inspection.InspectionStaffDto;
import com.newland.property.intf.community.IInspectionPlanStaffV1InnerServiceSMO;
import com.newland.property.doc.annotation.*;
import com.newland.property.utils.exception.CmdException;
import com.newland.property.utils.util.Assert;
import com.newland.property.utils.util.BeanConvertUtil;
import com.newland.property.vo.ResultVo;
import org.springframework.beans.factory.annotation.Autowired;

import java.text.ParseException;
import java.util.List;

/**
 * 员工巡检情况统计
 *
 *
 */

@NewlandPropertyCmdDoc(title = "员工巡检情况统计",
        description = "物业手机端员工巡检情况统计，方便老板查看",
        httpMethod = "get",
        url = "http://{ip}:{port}/app/inspection.queryReportStaffInspection",
        resource = "communityDoc",
        author = "吴学文",
        serviceCode = "inspection.queryReportStaffInspection"
)

@NewlandPropertyParamsDoc(params = {
        @NewlandPropertyParamDoc(name = "communityId", length = 30, remark = "小区信息"),
        @NewlandPropertyParamDoc(name = "queryTime", length = 30, remark = "查询日期 YYYY-MM-DD"),
})

@NewlandPropertyResponseDoc(
        params = {
                @NewlandPropertyParamDoc(name = "code", type = "int", length = 11, defaultValue = "0", remark = "返回编号，0 成功 其他失败"),
                @NewlandPropertyParamDoc(name = "msg", type = "String", length = 250, defaultValue = "成功", remark = "描述"),
                @NewlandPropertyParamDoc(name = "data", type = "Object", remark = "有效数据"),
                @NewlandPropertyParamDoc(parentNodeName = "data",name = "staffName", type = "String", remark = "员工名称"),
                @NewlandPropertyParamDoc(parentNodeName = "data",name = "finishCount", type = "String", remark = "已巡检"),
                @NewlandPropertyParamDoc(parentNodeName = "data",name = "waitCount", type = "String", remark = "未巡检"),
        }
)

@NewlandPropertyExampleDoc(
        reqBody="http://{ip}:{port}/app/inspection.queryReportStaffInspection?communityId=12323&queryTime=2022-11-11",
        resBody="{'code':0,'msg':'成功','data':{'staffName':'123123','finishCount':'123213','waitCount':123123}}"
)

@NewlandPropertyCmd(serviceCode = "inspection.queryReportStaffInspection")
public class QueryReportStaffInspectionCmd extends Cmd{

    @Autowired
    private IInspectionPlanStaffV1InnerServiceSMO inspectionPlanStaffV1InnerServiceSMOImpl;
    @Override
    public void validate(CmdEvent event, ICmdDataFlowContext context, JSONObject reqJson) throws CmdException {
        Assert.hasKeyAndValue(reqJson,"communityId","未包含小区信息");
        Assert.hasKeyAndValue(reqJson,"queryTime","未包含查询日期");
    }

    @Override
    public void doCmd(CmdEvent event, ICmdDataFlowContext context, JSONObject reqJson) throws CmdException, ParseException {


        InspectionStaffDto inspectionStaffDto = BeanConvertUtil.covertBean(reqJson, InspectionStaffDto.class);

        List<InspectionStaffDto> inspectionStaffDtos = inspectionPlanStaffV1InnerServiceSMOImpl.queryStaffInspectionReport(inspectionStaffDto);

        context.setResponseEntity(ResultVo.createResponseEntity(inspectionStaffDtos));
    }
}
