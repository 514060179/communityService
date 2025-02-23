package com.newland.property.dev.cmd.app;

import com.alibaba.fastjson.JSONObject;
import com.newland.property.core.annotation.NewlandPropertyCmd;
import com.newland.property.core.context.ICmdDataFlowContext;
import com.newland.property.core.event.cmd.Cmd;
import com.newland.property.core.event.cmd.CmdEvent;
import com.newland.property.dto.app.AppDto;
import com.newland.property.intf.community.IAppInnerServiceSMO;
import com.newland.property.utils.constant.ResponseConstant;
import com.newland.property.utils.exception.CmdException;
import com.newland.property.utils.exception.ListenerExecuteException;
import com.newland.property.utils.util.Assert;
import com.newland.property.utils.util.BeanConvertUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

@NewlandPropertyCmd(serviceCode = "app.updateApp")
public class UpdateAppCmd extends Cmd {

    @Autowired
    private IAppInnerServiceSMO appInnerServiceSMOImpl;

    @Override
    public void validate(CmdEvent event, ICmdDataFlowContext cmdDataFlowContext, JSONObject reqJson) throws CmdException {
        Assert.hasKeyAndValue(reqJson, "appId", "应用Id不能为空");
        Assert.hasKeyAndValue(reqJson, "name", "必填，请填写应用名称");
    }

    @Override
    public void doCmd(CmdEvent event, ICmdDataFlowContext cmdDataFlowContext, JSONObject reqJson) throws CmdException {
        AppDto appDto = BeanConvertUtil.covertBean(reqJson, AppDto.class);
        int count = appInnerServiceSMOImpl.updateApp(appDto);
        if (count < 1) {
            throw new ListenerExecuteException(ResponseConstant.RESULT_CODE_ERROR, "修改数据失败");
        }
        ResponseEntity<String> responseEntity = new ResponseEntity<String>("", HttpStatus.OK);
        cmdDataFlowContext.setResponseEntity(responseEntity);
    }
}
