package com.newland.property.dev.cmd.service;

import com.alibaba.fastjson.JSONObject;
import com.newland.property.core.annotation.NewlandPropertyCmd;
import com.newland.property.core.context.ICmdDataFlowContext;
import com.newland.property.core.event.cmd.Cmd;
import com.newland.property.core.event.cmd.CmdEvent;
import com.newland.property.dto.service.ServiceDto;
import com.newland.property.intf.community.IServiceInnerServiceSMO;
import com.newland.property.utils.exception.CmdException;
import com.newland.property.utils.util.BeanConvertUtil;
import com.newland.property.vo.api.service.ApiServiceDataVo;
import com.newland.property.vo.api.service.ApiServiceVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.ArrayList;
import java.util.List;

@NewlandPropertyCmd(serviceCode = "service.listServices")
public class ListServicesCmd extends Cmd {


    @Autowired
    private IServiceInnerServiceSMO serviceInnerServiceSMOImpl;

    @Override
    public void validate(CmdEvent event, ICmdDataFlowContext context, JSONObject reqJson) throws CmdException {
        super.validatePageInfo(reqJson);
    }

    @Override
    public void doCmd(CmdEvent event, ICmdDataFlowContext context, JSONObject reqJson) throws CmdException {

        ServiceDto serviceDto = BeanConvertUtil.covertBean(reqJson, ServiceDto.class);

        int count = serviceInnerServiceSMOImpl.queryServicesCount(serviceDto);

        List<ApiServiceDataVo> services = null;

        if (count > 0) {
            services = BeanConvertUtil.covertBeanList(serviceInnerServiceSMOImpl.queryServices(serviceDto), ApiServiceDataVo.class);
        } else {
            services = new ArrayList<>();
        }

        ApiServiceVo apiServiceVo = new ApiServiceVo();

        apiServiceVo.setTotal(count);
        apiServiceVo.setRecords((int) Math.ceil((double) count / (double) reqJson.getInteger("row")));
        apiServiceVo.setServices(services);

        ResponseEntity<String> responseEntity = new ResponseEntity<String>(JSONObject.toJSONString(apiServiceVo), HttpStatus.OK);

        context.setResponseEntity(responseEntity);
    }
}
