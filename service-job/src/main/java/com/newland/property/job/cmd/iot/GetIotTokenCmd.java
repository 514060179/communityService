package com.newland.property.job.cmd.iot;

import com.alibaba.fastjson.JSONObject;
import com.newland.property.core.annotation.NewlandPropertyCmd;
import com.newland.property.core.context.CmdContextUtils;
import com.newland.property.core.context.ICmdDataFlowContext;
import com.newland.property.core.event.cmd.Cmd;
import com.newland.property.core.event.cmd.CmdEvent;
import com.newland.property.dto.store.StoreDto;
import com.newland.property.dto.store.StoreUserDto;
import com.newland.property.dto.user.UserDto;
import com.newland.property.intf.store.IStoreUserV1InnerServiceSMO;
import com.newland.property.intf.store.IStoreV1InnerServiceSMO;
import com.newland.property.intf.user.IUserV1InnerServiceSMO;
import com.newland.property.job.adapt.hcIot.IotConstant;
import com.newland.property.job.adapt.hcIotNew.http.ISendIot;
import com.newland.property.utils.cache.MappingCache;
import com.newland.property.utils.exception.CmdException;
import com.newland.property.utils.util.Assert;
import com.newland.property.vo.ResultVo;
import org.springframework.beans.factory.annotation.Autowired;

import java.text.ParseException;
import java.util.List;

/**
 * 获取IOT的token
 */
@NewlandPropertyCmd(serviceCode = "iot.getIotToken")
public class GetIotTokenCmd extends Cmd {

    @Autowired
    private IUserV1InnerServiceSMO userV1InnerServiceSMOImpl;

    @Autowired
    private IStoreUserV1InnerServiceSMO storeUserV1InnerServiceSMOImpl;

    @Autowired
    private IStoreV1InnerServiceSMO storeV1InnerServiceSMOImpl;

    public static final String URL_DOMAIN = "URL_DOMAIN"; // 物联网域


    @Autowired
    private ISendIot sendIotImpl;

    @Override
    public void validate(CmdEvent event, ICmdDataFlowContext context, JSONObject reqJson) throws CmdException, ParseException {

        Assert.hasKeyAndValue(reqJson, "targetUrl", "未包含targetUrl");
        Assert.hasKeyAndValue(reqJson, "communityId", "未包含communityId");

        String iotSwitch = MappingCache.getValue("IOT", "IOT_SWITCH");
        if (!"ON".equals(iotSwitch)) {
            throw new CmdException("物联网系统未部署");
        }
    }

    @Override
    public void doCmd(CmdEvent event, ICmdDataFlowContext context, JSONObject reqJson) throws CmdException, ParseException {

        String userId = CmdContextUtils.getUserId(context);

        UserDto userDto = new UserDto();
        userDto.setUserId(userId);
        List<UserDto> userDtos = userV1InnerServiceSMOImpl.queryUsers(userDto);

        Assert.listOnlyOne(userDtos, "用户未登录");

        StoreUserDto storeUserDto = new StoreUserDto();
        storeUserDto.setUserId(userId);
        List<StoreUserDto> storeUserDtos = storeUserV1InnerServiceSMOImpl.queryStoreUsers(storeUserDto);

        Assert.listOnlyOne(storeUserDtos, "未包含商户");


        StoreDto storeDto = new StoreDto();
        storeDto.setStoreId(storeUserDtos.get(0).getStoreId());
        List<StoreDto> storeDtos = storeV1InnerServiceSMOImpl.queryStores(storeDto);

        Assert.listOnlyOne(storeDtos, "商户不存在");

        JSONObject staff = new JSONObject();
        staff.put("tel", userDtos.get(0).getTel());
        staff.put("storeName", storeDtos.get(0).getName());

        ResultVo resultVo = sendIotImpl.post("/iot/api/sso.getSsoToken", staff);

        if (resultVo.getCode() != ResultVo.CODE_OK) {
            throw new CmdException(resultVo.getMsg());
        }

        JSONObject paramOut = (JSONObject) resultVo.getData();

        String iotUrl = MappingCache.getValue(URL_DOMAIN, IotConstant.IOT_URL);
        String targetUrl = iotUrl + reqJson.getString("targetUrl");
        String url = iotUrl + "/sso.html?token=" + paramOut.getString("token") +"&communityId=" + reqJson.getString("communityId") + "&targetUrl=" + targetUrl;
        paramOut.put("url", url);
        context.setResponseEntity(ResultVo.createResponseEntity(paramOut));

    }
}
