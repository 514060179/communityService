package com.newland.property.community.cmd.inspectionRoute;

import com.alibaba.fastjson.JSONObject;
import com.newland.property.core.annotation.NewlandPropertyCmd;
import com.newland.property.core.context.ICmdDataFlowContext;
import com.newland.property.core.event.cmd.Cmd;
import com.newland.property.core.event.cmd.CmdEvent;
import com.newland.property.dto.inspection.InspectionRouteDto;
import com.newland.property.intf.community.IInspectionRouteInnerServiceSMO;
import com.newland.property.utils.exception.CmdException;
import com.newland.property.utils.util.Assert;
import com.newland.property.utils.util.BeanConvertUtil;
import com.newland.property.vo.api.inspectionRoute.ApiInspectionRouteDataVo;
import com.newland.property.vo.api.inspectionRoute.ApiInspectionRouteVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.ArrayList;
import java.util.List;

@NewlandPropertyCmd(serviceCode = "inspectionRoute.listInspectionRoutes")
public class ListInspectionRoutesCmd extends Cmd {

    @Autowired
    private IInspectionRouteInnerServiceSMO inspectionRouteInnerServiceSMOImpl;

    @Override
    public void validate(CmdEvent event, ICmdDataFlowContext context, JSONObject reqJson) throws CmdException {
        super.validatePageInfo(reqJson);
        Assert.hasKeyAndValue(reqJson, "communityId", "小区ID不能为空");
    }

    @Override
    public void doCmd(CmdEvent event, ICmdDataFlowContext context, JSONObject reqJson) throws CmdException {
        InspectionRouteDto inspectionRouteDto = BeanConvertUtil.covertBean(reqJson, InspectionRouteDto.class);

        int count = inspectionRouteInnerServiceSMOImpl.queryInspectionRoutesCount(inspectionRouteDto);

        List<ApiInspectionRouteDataVo> inspectionRoutes = null;

        if (count > 0) {
            inspectionRoutes = BeanConvertUtil.covertBeanList(inspectionRouteInnerServiceSMOImpl.queryInspectionRoutes(inspectionRouteDto), ApiInspectionRouteDataVo.class);
        } else {
            inspectionRoutes = new ArrayList<>();
        }

        ApiInspectionRouteVo apiInspectionRouteVo = new ApiInspectionRouteVo();

        apiInspectionRouteVo.setTotal(count);
        apiInspectionRouteVo.setRecords((int) Math.ceil((double) count / (double) reqJson.getInteger("row")));
        apiInspectionRouteVo.setInspectionRoutes(inspectionRoutes);

        ResponseEntity<String> responseEntity = new ResponseEntity<String>(JSONObject.toJSONString(apiInspectionRouteVo), HttpStatus.OK);

        context.setResponseEntity(responseEntity);
    }
}
