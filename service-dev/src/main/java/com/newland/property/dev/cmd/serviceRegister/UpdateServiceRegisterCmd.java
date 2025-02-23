package com.newland.property.dev.cmd.serviceRegister;

import com.alibaba.fastjson.JSONObject;
import com.newland.property.core.annotation.NewlandPropertyCmd;
import com.newland.property.core.context.ICmdDataFlowContext;
import com.newland.property.core.event.cmd.Cmd;
import com.newland.property.core.event.cmd.CmdEvent;
import com.newland.property.dto.service.RouteDto;
import com.newland.property.intf.community.IRouteInnerServiceSMO;
import com.newland.property.utils.constant.ResponseConstant;
import com.newland.property.utils.exception.CmdException;
import com.newland.property.utils.exception.ListenerExecuteException;
import com.newland.property.utils.util.Assert;
import com.newland.property.utils.util.BeanConvertUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

@NewlandPropertyCmd(serviceCode = "serviceRegister.updateServiceRegister")
public class UpdateServiceRegisterCmd extends Cmd {
    @Autowired
    private IRouteInnerServiceSMO routeInnerServiceSMOImpl;
    @Override
    public void validate(CmdEvent event, ICmdDataFlowContext context, JSONObject reqJson) throws CmdException {
        Assert.hasKeyAndValue(reqJson, "id", "绑定ID不能为空");
        Assert.hasKeyAndValue(reqJson, "appId", "必填，请填写应用ID");
        Assert.hasKeyAndValue(reqJson, "serviceId", "必填，请填写服务ID");
        Assert.hasKeyAndValue(reqJson, "orderTypeCd", "必填，请填写订单类型");
        Assert.hasKeyAndValue(reqJson, "invokeLimitTimes", "必填，请填写调用次数");
        Assert.hasKeyAndValue(reqJson, "invokeModel", "可填，请填写消息队列，订单在异步调用时使用");
    }

    @Override
    public void doCmd(CmdEvent event, ICmdDataFlowContext context, JSONObject reqJson) throws CmdException {
        RouteDto routeDto = BeanConvertUtil.covertBean(reqJson, RouteDto.class);


        int count = routeInnerServiceSMOImpl.updateRoute(routeDto);


        if (count < 1) {
            throw new ListenerExecuteException(ResponseConstant.RESULT_CODE_ERROR, "修改数据失败");
        }

        ResponseEntity<String> responseEntity = new ResponseEntity<String>("", HttpStatus.OK);

        context.setResponseEntity(responseEntity);
    }
}
