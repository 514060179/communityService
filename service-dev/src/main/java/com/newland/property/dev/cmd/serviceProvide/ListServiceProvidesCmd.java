package com.newland.property.dev.cmd.serviceProvide;

import com.alibaba.fastjson.JSONObject;
import com.newland.property.core.annotation.NewlandPropertyCmd;
import com.newland.property.core.context.ICmdDataFlowContext;
import com.newland.property.core.event.cmd.Cmd;
import com.newland.property.core.event.cmd.CmdEvent;
import com.newland.property.dto.service.ServiceProvideDto;
import com.newland.property.intf.community.IServiceInnerServiceSMO;
import com.newland.property.utils.exception.CmdException;
import com.newland.property.utils.util.BeanConvertUtil;
import com.newland.property.vo.api.serviceProvide.ApiServiceProvideDataVo;
import com.newland.property.vo.api.serviceProvide.ApiServiceProvideVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.ArrayList;
import java.util.List;

@NewlandPropertyCmd(serviceCode = "serviceProvide.listServiceProvides")
public class ListServiceProvidesCmd extends Cmd {

    @Autowired
    private IServiceInnerServiceSMO serviceInnerServiceSMOImpl;

    @Override
    public void validate(CmdEvent event, ICmdDataFlowContext context, JSONObject reqJson) throws CmdException {
        super.validatePageInfo(reqJson);
    }

    @Override
    public void doCmd(CmdEvent event, ICmdDataFlowContext context, JSONObject reqJson) throws CmdException {
        ServiceProvideDto serviceProvideDto = BeanConvertUtil.covertBean(reqJson, ServiceProvideDto.class);

        int count = serviceInnerServiceSMOImpl.queryServiceProvidesCount(serviceProvideDto);

        List<ApiServiceProvideDataVo> serviceProvides = null;

        if (count > 0) {
            serviceProvides = BeanConvertUtil.covertBeanList(serviceInnerServiceSMOImpl.queryServiceProvides(serviceProvideDto), ApiServiceProvideDataVo.class);
        } else {
            serviceProvides = new ArrayList<>();
        }

        ApiServiceProvideVo apiServiceProvideVo = new ApiServiceProvideVo();

        apiServiceProvideVo.setTotal(count);
        apiServiceProvideVo.setRecords((int) Math.ceil((double) count / (double) reqJson.getInteger("row")));
        apiServiceProvideVo.setServiceProvides(serviceProvides);

        ResponseEntity<String> responseEntity = new ResponseEntity<String>(JSONObject.toJSONString(apiServiceProvideVo), HttpStatus.OK);

        context.setResponseEntity(responseEntity);

    }
}
