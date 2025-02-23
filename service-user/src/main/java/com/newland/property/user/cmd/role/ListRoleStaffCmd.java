package com.newland.property.user.cmd.role;

import com.alibaba.fastjson.JSONObject;
import com.newland.property.core.annotation.NewlandPropertyCmd;
import com.newland.property.core.context.ICmdDataFlowContext;
import com.newland.property.core.event.cmd.Cmd;
import com.newland.property.core.event.cmd.CmdEvent;
import com.newland.property.dto.privilege.PrivilegeUserDto;
import com.newland.property.dto.user.UserDto;
import com.newland.property.intf.user.IPrivilegeUserV1InnerServiceSMO;
import com.newland.property.utils.exception.CmdException;
import com.newland.property.utils.util.Assert;
import com.newland.property.vo.ResultVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.ArrayList;
import java.util.List;

@NewlandPropertyCmd(serviceCode = "role.listRoleStaff")
public class ListRoleStaffCmd extends Cmd {

    @Autowired
    private IPrivilegeUserV1InnerServiceSMO privilegeUserV1InnerServiceSMOImpl;

    @Override
    public void validate(CmdEvent event, ICmdDataFlowContext context, JSONObject reqJson) throws CmdException {
        Assert.hasKeyAndValue(reqJson, "roleId", "未包含角色");
    }

    @Override
    public void doCmd(CmdEvent event, ICmdDataFlowContext context, JSONObject reqJson) throws CmdException {

        String storeId = context.getReqHeaders().get("store-id");

        PrivilegeUserDto privilegeUserDto = new PrivilegeUserDto();
        privilegeUserDto.setpId(reqJson.getString("roleId"));
        privilegeUserDto.setStoreId(storeId);
        privilegeUserDto.setPrivilegeFlag(PrivilegeUserDto.PRIVILEGE_FLAG_GROUP);
        privilegeUserDto.setUserName(reqJson.getString("searchUserName"));
        privilegeUserDto.setPage(Integer.parseInt(reqJson.getString("page")));
        privilegeUserDto.setRow(Integer.parseInt(reqJson.getString("row")));

        int count = privilegeUserV1InnerServiceSMOImpl.queryPrivilegeUserInfoCount(privilegeUserDto);

        List<UserDto> staffsDtos = null;

        if (count > 0) {
            staffsDtos = privilegeUserV1InnerServiceSMOImpl.queryPrivilegeUserInfos(privilegeUserDto);
        } else {
            staffsDtos = new ArrayList<>();
        }

        ResultVo resultVo = new ResultVo((int) Math.ceil((double) count / (double) reqJson.getInteger("row")), count, staffsDtos);

        ResponseEntity<String> responseEntity = new ResponseEntity<String>(resultVo.toString(), HttpStatus.OK);

        context.setResponseEntity(responseEntity);
    }
}
