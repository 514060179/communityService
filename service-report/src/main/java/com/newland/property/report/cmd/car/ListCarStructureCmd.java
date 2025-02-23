package com.newland.property.report.cmd.car;

import com.alibaba.fastjson.JSONObject;
import com.newland.property.core.annotation.NewlandPropertyCmd;
import com.newland.property.core.context.ICmdDataFlowContext;
import com.newland.property.core.event.cmd.Cmd;
import com.newland.property.core.event.cmd.CmdEvent;
import com.newland.property.dto.owner.OwnerCarDto;
import com.newland.property.intf.report.IReportCommunityInnerServiceSMO;
import com.newland.property.utils.exception.CmdException;
import com.newland.property.utils.util.Assert;
import com.newland.property.utils.util.BeanConvertUtil;
import com.newland.property.vo.ResultVo;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

/*
     查询车位结构图
 */
@NewlandPropertyCmd(serviceCode = "car.listCarStructure")
public class ListCarStructureCmd extends Cmd {

    @Autowired
    private IReportCommunityInnerServiceSMO reportCommunityInnerServiceSMOImpl;

    @Override
    public void validate(CmdEvent event, ICmdDataFlowContext cmdDataFlowContext, JSONObject reqJson) {

        Assert.hasKeyAndValue(reqJson, "unitId", "未传入单元信息");
        Assert.hasKeyAndValue(reqJson, "communityId", "未传入房屋信息");

    }

    @Override
    public void doCmd(CmdEvent event, ICmdDataFlowContext cmdDataFlowContext, JSONObject reqJson) throws CmdException {

        OwnerCarDto carDto = BeanConvertUtil.covertBean(reqJson, OwnerCarDto.class);
        List<OwnerCarDto> ownerCarDtos = reportCommunityInnerServiceSMOImpl.queryCarStructures(carDto);

        cmdDataFlowContext.setResponseEntity(ResultVo.createResponseEntity(ownerCarDtos));
    }


}
