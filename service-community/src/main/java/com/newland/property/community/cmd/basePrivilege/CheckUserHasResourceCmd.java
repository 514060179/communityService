package com.newland.property.community.cmd.basePrivilege;

import com.alibaba.fastjson.JSONObject;
import com.newland.property.core.annotation.NewlandPropertyCmd;
import com.newland.property.core.context.ICmdDataFlowContext;
import com.newland.property.core.event.cmd.Cmd;
import com.newland.property.core.event.cmd.CmdEvent;
import com.newland.property.core.log.LoggerFactory;
import com.newland.property.dto.privilege.BasePrivilegeDto;
import com.newland.property.intf.community.IMenuInnerServiceSMO;
import com.newland.property.utils.exception.CmdException;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.Map;

@NewlandPropertyCmd(serviceCode = "basePrivilege.CheckUserHasResourceListener")
public class CheckUserHasResourceCmd extends Cmd {
    private final static Logger logger = LoggerFactory.getLogger(CheckUserHasResourceCmd.class);

    @Autowired
    private IMenuInnerServiceSMO menuInnerServiceSMOImpl;

    @Override
    public void validate(CmdEvent event, ICmdDataFlowContext context, JSONObject reqJson) throws CmdException {

    }

    @Override
    public void doCmd(CmdEvent event, ICmdDataFlowContext context, JSONObject reqJson) throws CmdException {
        logger.debug("请求信息：{}", reqJson);
        ResponseEntity<String> responseEntity = null;
        BasePrivilegeDto basePrivilegeDto = new BasePrivilegeDto();
        basePrivilegeDto.setResource(reqJson.getString("resource"));
        basePrivilegeDto.setUserId(reqJson.getString("userId"));
        List<Map> privileges = menuInnerServiceSMOImpl.checkUserHasResource(basePrivilegeDto);

        JSONObject data = new JSONObject();
        data.put("privileges", privileges);
        responseEntity = new ResponseEntity<String>(data.toJSONString(), HttpStatus.OK);
        context.setResponseEntity(responseEntity);
    }
}
