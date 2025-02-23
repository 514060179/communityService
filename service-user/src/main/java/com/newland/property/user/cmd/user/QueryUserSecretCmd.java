package com.newland.property.user.cmd.user;

import com.alibaba.fastjson.JSONObject;
import com.newland.property.core.annotation.NewlandPropertyCmd;
import com.newland.property.core.context.ICmdDataFlowContext;
import com.newland.property.core.event.cmd.Cmd;
import com.newland.property.core.event.cmd.CmdEvent;
import com.newland.property.dto.user.UserAttrDto;
import com.newland.property.dto.user.UserDto;
import com.newland.property.intf.user.IUserInnerServiceSMO;
import com.newland.property.utils.exception.CmdException;
import com.newland.property.utils.util.Assert;
import com.newland.property.utils.util.BeanConvertUtil;
import com.newland.property.vo.ResultVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;

import java.util.List;

@NewlandPropertyCmd(serviceCode = "user.queryUserSecret")
public class QueryUserSecretCmd extends Cmd {

    @Autowired
    private IUserInnerServiceSMO userInnerServiceSMOImpl;

    @Override
    public void validate(CmdEvent event, ICmdDataFlowContext context, JSONObject reqJson) throws CmdException {
        Assert.hasKeyAndValue(reqJson, "userId", "请求报文中未包含用户ID");
    }

    @Override
    public void doCmd(CmdEvent event, ICmdDataFlowContext context, JSONObject reqJson) throws CmdException {
        ResponseEntity<String> responseEntity = null;
        UserDto userDto = BeanConvertUtil.covertBean(reqJson, UserDto.class);

        List<UserDto> userDtos = userInnerServiceSMOImpl.getUsers(userDto);

        if (userDtos == null || userDtos.size() < 1) {
            responseEntity = ResultVo.createResponseEntity(ResultVo.CODE_ERROR, "未找到用户信息");
            context.setResponseEntity(responseEntity);
            return;
        }

        userDto = userDtos.get(0);
        List<UserAttrDto> userAttrDtos = userDto.getUserAttrs();
        String key = "";
        String openId = "";
        for (UserAttrDto userAttrDto : userAttrDtos) {
            if (UserAttrDto.SPEC_KEY.equals(userAttrDto.getSpecCd())) {
                key = userAttrDto.getValue();
            }

            if (UserAttrDto.SPEC_OPEN_ID.equals(userAttrDto.getSpecCd())) {
                openId = userAttrDto.getValue();
            }
        }
        userDto.setKey(key);
        userDto.setOpenId(openId);
        responseEntity = ResultVo.createResponseEntity(ResultVo.CODE_OK, "成功", userDto);
        context.setResponseEntity(responseEntity);
    }
}
