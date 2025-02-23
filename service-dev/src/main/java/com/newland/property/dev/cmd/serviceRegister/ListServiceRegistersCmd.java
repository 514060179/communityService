package com.newland.property.dev.cmd.serviceRegister;

import com.alibaba.fastjson.JSONObject;
import com.newland.property.core.annotation.NewlandPropertyCmd;
import com.newland.property.core.context.ICmdDataFlowContext;
import com.newland.property.core.event.cmd.Cmd;
import com.newland.property.core.event.cmd.CmdEvent;
import com.newland.property.dto.service.RouteDto;
import com.newland.property.intf.community.IRouteInnerServiceSMO;
import com.newland.property.utils.exception.CmdException;
import com.newland.property.utils.util.BeanConvertUtil;
import com.newland.property.vo.api.serviceRegister.ApiServiceRegisterDataVo;
import com.newland.property.vo.api.serviceRegister.ApiServiceRegisterVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.ArrayList;
import java.util.List;

@NewlandPropertyCmd(serviceCode = "serviceRegister.listServiceRegisters")
public class ListServiceRegistersCmd extends Cmd {

    @Autowired
    private IRouteInnerServiceSMO routeInnerServiceSMOIMpl;

    @Override
    public void validate(CmdEvent event, ICmdDataFlowContext context, JSONObject reqJson) throws CmdException {
        super.validatePageInfo(reqJson);
    }

    @Override
    public void doCmd(CmdEvent event, ICmdDataFlowContext context, JSONObject reqJson) throws CmdException {

        RouteDto routeDto = BeanConvertUtil.covertBean(reqJson, RouteDto.class);

        int count = routeInnerServiceSMOIMpl.queryRoutesCount(routeDto);

        List<ApiServiceRegisterDataVo> serviceRegisters = null;

        if (count > 0) {
            serviceRegisters = BeanConvertUtil.covertBeanList(routeInnerServiceSMOIMpl.queryRoutes(routeDto), ApiServiceRegisterDataVo.class);
        } else {
            serviceRegisters = new ArrayList<>();
        }

        ApiServiceRegisterVo apiServiceRegisterVo = new ApiServiceRegisterVo();

        apiServiceRegisterVo.setTotal(count);
        apiServiceRegisterVo.setRecords((int) Math.ceil((double) count / (double) reqJson.getInteger("row")));
        apiServiceRegisterVo.setServiceRegisters(serviceRegisters);

        ResponseEntity<String> responseEntity = new ResponseEntity<String>(JSONObject.toJSONString(apiServiceRegisterVo), HttpStatus.OK);

        context.setResponseEntity(responseEntity);
    }
}
