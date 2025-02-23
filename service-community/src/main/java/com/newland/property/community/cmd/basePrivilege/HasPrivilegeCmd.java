package com.newland.property.community.cmd.basePrivilege;

import com.alibaba.fastjson.JSONObject;
import com.newland.property.core.annotation.NewlandPropertyCmd;
import com.newland.property.core.context.ICmdDataFlowContext;
import com.newland.property.core.event.cmd.Cmd;
import com.newland.property.core.event.cmd.CmdEvent;
import com.newland.property.core.log.LoggerFactory;
import com.newland.property.dto.privilege.HasPrivilegeDto;
import com.newland.property.intf.community.IMenuInnerServiceSMO;
import com.newland.property.utils.exception.CmdException;
import com.newland.property.utils.util.Assert;
import com.newland.property.vo.ResultVo;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.List;

@NewlandPropertyCmd(serviceCode = "check.user.hasPrivilege")
public class HasPrivilegeCmd extends Cmd {


    private final static Logger logger = LoggerFactory.getLogger(HasPrivilegeCmd.class);

    @Autowired
    private IMenuInnerServiceSMO menuInnerServiceSMOImpl;

    @Override
    public void validate(CmdEvent event, ICmdDataFlowContext context, JSONObject reqJson) throws CmdException {
        Assert.hasKeyAndValue(reqJson, "userId", "请求报文中未包含userId节点");
        Assert.hasKeyAndValue(reqJson, "pId", "请求报文中未包含pId节点");
    }

    @Override
    public void doCmd(CmdEvent event, ICmdDataFlowContext context, JSONObject reqJson) throws CmdException {
        ResponseEntity<String> responseEntity = null;

        //根据名称查询用户信息
        HasPrivilegeDto hasPrivilegeDto = new HasPrivilegeDto();
        hasPrivilegeDto.setUserId(reqJson.getString("userId"));
        hasPrivilegeDto.setpId(reqJson.getString("pId"));
        List<HasPrivilegeDto> privilegeDtos = menuInnerServiceSMOImpl.hasPrivilege(hasPrivilegeDto);

        if (privilegeDtos == null || privilegeDtos.size() < 1) {
            context.setResponseEntity(ResultVo.error("没有权限操作", HttpStatus.UNAUTHORIZED));
            return;
        }

        responseEntity = new ResponseEntity<String>("成功", HttpStatus.OK);
        context.setResponseEntity(responseEntity);
    }
}
