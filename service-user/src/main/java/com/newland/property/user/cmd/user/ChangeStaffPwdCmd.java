package com.newland.property.user.cmd.user;

import com.alibaba.fastjson.JSONObject;
import com.newland.property.core.annotation.NewlandPropertyCmd;
import com.newland.property.core.context.Environment;
import com.newland.property.core.context.ICmdDataFlowContext;
import com.newland.property.core.event.cmd.Cmd;
import com.newland.property.core.event.cmd.CmdEvent;
import com.newland.property.core.factory.AuthenticationFactory;
import com.newland.property.dto.user.UserDto;
import com.newland.property.intf.user.IUserInnerServiceSMO;
import com.newland.property.intf.user.IUserV1InnerServiceSMO;
import com.newland.property.po.user.UserPo;
import com.newland.property.utils.exception.CmdException;
import com.newland.property.utils.util.Assert;
import com.newland.property.utils.util.BeanConvertUtil;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

@NewlandPropertyCmd(serviceCode = "user.changeStaffPwd")
public class ChangeStaffPwdCmd extends Cmd {
    @Autowired
    private IUserInnerServiceSMO userInnerServiceSMOImpl;
    @Autowired
    private IUserV1InnerServiceSMO userV1InnerServiceSMOImpl;
    @Override
    public void validate(CmdEvent event, ICmdDataFlowContext context, JSONObject reqJson) throws CmdException {
        Environment.isDevEnv();
    }

    @Override
    public void doCmd(CmdEvent event, ICmdDataFlowContext context, JSONObject reqJson) throws CmdException {

        Assert.jsonObjectHaveKey(reqJson, "userId", "请求参数中未包含userId 节点，请确认");
        Assert.jsonObjectHaveKey(reqJson, "oldPwd", "请求参数中未包含oldPwd 节点，请确认");
        Assert.jsonObjectHaveKey(reqJson, "newPwd", "请求参数中未包含newPwd 节点，请确认");

        reqJson.put("oldPwd", AuthenticationFactory.passwdMd5(reqJson.getString("oldPwd")));
        reqJson.put("newPwd", AuthenticationFactory.passwdMd5(reqJson.getString("newPwd")));

        modifyStaff(reqJson);
    }
    public void modifyStaff(JSONObject paramObj) {
        //校验json 格式中是否包含 name,email,levelCd,tel
        UserPo userPo = BeanConvertUtil.covertBean(builderStaffInfo(paramObj), UserPo.class);
        int flag = userV1InnerServiceSMOImpl.updateUser(userPo);
        if(flag <1){
            throw new CmdException("修改密码失败");
        }
    }

    /**
     * 构建员工信息
     *
     * @param paramObj
     * @return
     */
    private JSONObject builderStaffInfo(JSONObject paramObj) {

        UserDto userDto = new UserDto();
        userDto.setStatusCd("0");
        userDto.setUserId(paramObj.getString("userId"));
        List<UserDto> userDtos = userInnerServiceSMOImpl.getUserHasPwd(userDto);

        Assert.listOnlyOne(userDtos, "数据错误查询到多条用户信息或单条");

        JSONObject userInfo = JSONObject.parseObject(JSONObject.toJSONString(userDtos.get(0)));

        if (!paramObj.getString("oldPwd").equals(userDtos.get(0).getPassword())) {
            throw new IllegalArgumentException("原始密码错误");
        }
        userInfo.putAll(paramObj);
        userInfo.put("password", paramObj.getString("newPwd"));

        return userInfo;
    }
}
