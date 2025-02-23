package com.newland.property.community.cmd.ownerRepair;

import com.alibaba.fastjson.JSONObject;
import com.newland.property.core.annotation.NewlandPropertyCmd;
import com.newland.property.core.context.ICmdDataFlowContext;
import com.newland.property.core.event.cmd.Cmd;
import com.newland.property.core.event.cmd.CmdEvent;
import com.newland.property.dto.repair.RepairDto;
import com.newland.property.intf.community.IRepairInnerServiceSMO;
import com.newland.property.doc.annotation.*;
import com.newland.property.utils.exception.CmdException;
import com.newland.property.utils.util.Assert;
import com.newland.property.utils.util.BeanConvertUtil;
import com.newland.property.vo.ResultVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

/**
 * 查询员工待办 维修工单
 */

@NewlandPropertyCmdDoc(title = "员工待办维修单",
        description = "查询员工待办维修单",
        httpMethod = "get",
        url = "http://{ip}:{port}/app/ownerRepair.listUndoRepairsByStaff",
        resource = "communityDoc",
        author = "吴学文",
        serviceCode = "ownerRepair.listUndoRepairsByStaff",
        seq = 17
)

@NewlandPropertyParamsDoc(
        params = {
                @NewlandPropertyParamDoc(name = "communityId", length = 30, remark = "必填，小区ID"),
                @NewlandPropertyParamDoc(name = "staffId", length = 30, remark = "必填，员工ID"),
        })

@NewlandPropertyResponseDoc(
        params = {
                @NewlandPropertyParamDoc(name = "code", type = "int", length = 11, defaultValue = "0", remark = "返回编号，0 成功 其他失败"),
                @NewlandPropertyParamDoc(name = "msg", type = "String", length = 250, defaultValue = "成功", remark = "描述"),
                @NewlandPropertyParamDoc(name = "data", type = "Array", remark = "有效数据"),
                @NewlandPropertyParamDoc(parentNodeName = "data",name = "appointmentTime", type = "String", remark = "预约时间"),
                @NewlandPropertyParamDoc(parentNodeName = "data",name = "repairName", type = "String", remark = "报修名称"),
                @NewlandPropertyParamDoc(parentNodeName = "data",name = "repairId", type = "String", remark = "工单ID"),
                @NewlandPropertyParamDoc(parentNodeName = "data",name = "context", type = "String", remark = "报修内容"),
        }
)

@NewlandPropertyExampleDoc(
        reqBody="{'username':'wuxw','passwd':'admin'}",
        resBody="{\"code\":0,\"data\":[{\"appointmentTime\":\"2023-02-18 23:34:23\",\"bId\":\"202023021803780003\"," +
                "\"communityId\":\"2023013154290059\",\"context\":\"飞蛾打印机报修单测试\",\"page\":-1,\"preRuId\":\"832023021884560007\"," +
                "\"preStaffId\":\"302023013185270087\",\"preStaffName\":\"0131演示物业\",\"publicArea\":\"T\",\"records\":0,\"repairChannel\":\"T\"," +
                "\"repairId\":\"822023021809940002\",\"repairName\":\"飞蛾打印机报修单测试\",\"repairObjId\":\"2023013154290059\"," +
                "\"repairObjName\":\"0131培训小区\",\"repairObjType\":\"001\",\"repairType\":\"102023020172340620\",\"repairTypeName\":\"水电报修\"," +
                "\"repairWay\":\"200\",\"returnVisitFlag\":\"001\",\"returnVisitFlagName\":\"都不回访\",\"row\":0,\"ruId\":\"832023021876140009\"," +
                "\"startRuId\":\"832023021897810004\",\"state\":\"1100\",\"stateName\":\"接单\",\"statusCd\":\"0\",\"tel\":\"18909714455\"," +
                "\"total\":0}],\"msg\":\"成功\",\"page\":0,\"records\":1,\"rows\":0,\"total\":1}"
)
@NewlandPropertyCmd(serviceCode = "ownerRepair.listUndoRepairsByStaff")
public class ListUndoRepairsByStaffCmd extends Cmd {

    @Autowired
    private IRepairInnerServiceSMO repairInnerServiceSMOImpl;

    @Override
    public void validate(CmdEvent event, ICmdDataFlowContext context, JSONObject reqJson) throws CmdException {
        super.validatePageInfo(reqJson);
        Assert.hasKeyAndValue(reqJson, "communityId", "请求中未包含小区ID");
        Assert.hasKeyAndValue(reqJson, "staffId", "请求中未包含员工信息");
    }

    /**
     * 报修待办
     *
     * @param event   事件对象
     * @param context 数据上文对象
     * @param reqJson 请求报文
     * @throws CmdException
     * @throws ParseException
     */
    @Override
    public void doCmd(CmdEvent event, ICmdDataFlowContext context, JSONObject reqJson) throws CmdException, ParseException {
        RepairDto ownerRepairDto = BeanConvertUtil.covertBean(reqJson, RepairDto.class);
        //获取用户id
        int count = repairInnerServiceSMOImpl.queryStaffRepairsCount(ownerRepairDto);
        List<RepairDto> ownerRepairs = null;
        if (count > 0) {
            ownerRepairs = repairInnerServiceSMOImpl.queryStaffRepairs(ownerRepairDto);
        } else {
            ownerRepairs = new ArrayList<>();
        }
        ResponseEntity<String> responseEntity = ResultVo.createResponseEntity((int) Math.ceil((double) count / (double) reqJson.getInteger("row")), count, ownerRepairs);
        context.setResponseEntity(responseEntity);
    }
}
