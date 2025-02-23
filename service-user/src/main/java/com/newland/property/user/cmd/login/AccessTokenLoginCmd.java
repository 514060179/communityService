package com.newland.property.user.cmd.login;


import com.alibaba.fastjson.JSONObject;
import com.newland.property.core.annotation.NewlandPropertyCmd;
import com.newland.property.core.context.ICmdDataFlowContext;
import com.newland.property.core.event.cmd.Cmd;
import com.newland.property.core.event.cmd.CmdEvent;
import com.newland.property.core.factory.AuthenticationFactory;
import com.newland.property.core.factory.GenerateCodeFactory;
import com.newland.property.core.log.LoggerFactory;
import com.newland.property.dto.store.StoreUserDto;
import com.newland.property.dto.user.UserDto;
import com.newland.property.dto.user.UserLoginDto;
import com.newland.property.intf.store.IStoreInnerServiceSMO;
import com.newland.property.intf.user.IUserInnerServiceSMO;
import com.newland.property.intf.user.IUserLoginInnerServiceSMO;
import com.newland.property.po.user.UserLoginPo;
import com.newland.property.doc.annotation.*;
import com.newland.property.utils.cache.CommonCache;
import com.newland.property.utils.cache.MappingCache;
import com.newland.property.utils.constant.CommonConstant;
import com.newland.property.utils.constant.MappingConstant;
import com.newland.property.utils.constant.ResponseConstant;
import com.newland.property.utils.exception.CmdException;
import com.newland.property.utils.exception.SMOException;
import com.newland.property.utils.util.*;
import com.newland.property.vo.ResultVo;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.io.IOException;
import java.text.ParseException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


@NewlandPropertyCmdDoc(title = "单点登录accessToken自登陆（自用）",
        description = "1、通过接口/app/login.getAccessToken 获取到accessToken,<br/>" +
                "2、三方系统通过302 跳转的方式调转到物业系统 http://wuye.xx.com/sso.html?hcAccessToken={accessToken}&targetUrl=您要跳转的页面<br/>" +
                "3、物业系统即可完成自登陆功能</br>"+
                "注意：系统默认情况下是不启用单点登录功能的，请联系开发，配置中心中启用（SSO_SWITCH 的值改为ON）</br>",
        httpMethod = "post",
        url = "http://{ip}:{port}/app/login.accessTokenLogin",
        resource = "userDoc",
        author = "吴学文",
        serviceCode = "login.accessTokenLogin",
        seq = 18
)

@NewlandPropertyParamsDoc(
        headers = {
                @NewlandPropertyHeaderDoc(name="APP-ID",defaultValue = "通过dev账户分配应用",description = "应用APP-ID"),
                @NewlandPropertyHeaderDoc(name="TRANSACTION-ID",defaultValue = "uuid",description = "交易流水号"),
                @NewlandPropertyHeaderDoc(name="REQ-TIME",defaultValue = "20220917120915",description = "请求时间 YYYYMMDDhhmmss"),
                @NewlandPropertyHeaderDoc(name="property-LANG",defaultValue = "zh-cn",description = "语言中文"),
                @NewlandPropertyHeaderDoc(name="USER-ID",defaultValue = "-1",description = "调用用户ID 一般写-1"),
        },
        params = {
                @NewlandPropertyParamDoc(name = "hcAccessToken", length = 30, remark = "token 通过服务端调用 接口方式获取到"),
        })

@NewlandPropertyResponseDoc(
        params = {
                @NewlandPropertyParamDoc(name = "code", type = "int", length = 11, defaultValue = "0", remark = "返回编号，0 成功 其他失败"),
                @NewlandPropertyParamDoc(name = "msg", type = "String", length = 250, defaultValue = "成功", remark = "描述"),
                @NewlandPropertyParamDoc(name = "data", type = "Object", remark = "有效数据"),
                @NewlandPropertyParamDoc(parentNodeName = "data",name = "userId", type = "String", remark = "用户ID"),
                @NewlandPropertyParamDoc(parentNodeName = "data",name = "token", type = "String", remark = "临时票据"),
        }
)

@NewlandPropertyExampleDoc(
        reqBody="{'hcAccessToken':'112sdfsdfsdfsdfsdf'}",
        resBody="{'code':0,'msg':'成功','data':{'userId':'123123','token':'123213'}}"
)

@NewlandPropertyCmd(serviceCode = "login.accessTokenLogin")
public class AccessTokenLoginCmd extends Cmd {
    private final static Logger logger = LoggerFactory.getLogger(AccessTokenLoginCmd.class);

    @Autowired
    private IUserLoginInnerServiceSMO userLoginInnerServiceSMOImpl;

    @Autowired
    private IStoreInnerServiceSMO storeInnerServiceSMOImpl;

    @Autowired
    private IUserInnerServiceSMO userInnerServiceSMOImpl;

    @Override
    public void validate(CmdEvent event, ICmdDataFlowContext context, JSONObject reqJson) throws CmdException, ParseException {

        Assert.hasKeyAndValue(reqJson, "hcAccessToken", "未包含token信息");

        // 判断系统是否开启了 单点登录的功能
        String ssoSwitch = MappingCache.getValue(MappingConstant.DOMAIN_SYSTEM_SWITCH, "SSO_SWITCH");

        if (!"ON".equals(ssoSwitch)) {
            throw new CmdException("系统未开启单点登录功能请联系开发者");
        }

    }

    @Override
    public void doCmd(CmdEvent event, ICmdDataFlowContext context, JSONObject reqJson) throws CmdException, ParseException {
        UserDto userDto = null;

        ResponseEntity<String> responseEntity = null;
        // todo 尝试根据 token 去缓存中获取

        String userInfo = CommonCache.getAndRemoveValue(reqJson.getString("hcAccessToken") + "_sso");
        if (!StringUtil.isEmpty(userInfo)) {
            JSONObject userJson = JSONObject.parseObject(userInfo);
            userDto = BeanConvertUtil.covertBean(userJson, UserDto.class);
        }

        //todo 尝试 解码token 拿到用户名密码 登录
        // 这部分逻辑建议 删除，这个是某客户 对方厂商偷懒直接 浏览器跳转带 用户名密码登录的方式，不安全
        if (userDto == null) {
            try {
                userInfo = new String(Base64Convert.base64ToByte(reqJson.getString("hcAccessToken")), "UTF-8");
                JSONObject userJson = JSONObject.parseObject(userInfo);
                userDto = new UserDto();
                userDto.setTel(userJson.getString("tel"));
                userDto.setPassword(AuthenticationFactory.passwdMd5(userJson.getString("passwd")));
                userDto.setLevelCds(new String[]{UserDto.LEVEL_CD_ADMIN, UserDto.LEVEL_CD_STAFF});
                List<UserDto> userDtos = userInnerServiceSMOImpl.getUsers(userDto);

                Assert.listOnlyOne(userDtos, "用户不存在");
                userDto = userDtos.get(0);
            } catch (IOException e) {
                throw new CmdException("登录失败");
            }
        }
        // todo 到这里

        //检查商户状态
        StoreUserDto storeUserDto = new StoreUserDto();
        storeUserDto.setUserId(userDto.getUserId());
        List<StoreUserDto> storeUserDtos = storeInnerServiceSMOImpl.getStoreUserInfo(storeUserDto);

        if (storeUserDtos != null && storeUserDtos.size() > 0) {
            String state = storeUserDtos.get(0).getState();
            if ("48002".equals(state)) {
                responseEntity = new ResponseEntity<String>("当前商户限制登录，请联系管理员", HttpStatus.UNAUTHORIZED);
                context.setResponseEntity(responseEntity);
                return;
            }
        }
        try {
            Map userMap = new HashMap();
            userMap.put(CommonConstant.LOGIN_USER_ID, userDto.getUserId());
            userMap.put(CommonConstant.LOGIN_USER_NAME, userDto.getUserName());
            String token = AuthenticationFactory.createAndSaveToken(userMap);
            JSONObject user = BeanConvertUtil.beanCovertJson(userDto);
            user.remove("userPwd");
            user.put("token", token);
            //记录登录日志
            UserLoginPo userLoginPo = new UserLoginPo();
            userLoginPo.setLoginId(GenerateCodeFactory.getGeneratorId(GenerateCodeFactory.CODE_PREFIX_loginId));
            userLoginPo.setLoginTime(DateUtil.getNow(DateUtil.DATE_FORMATE_STRING_A));
            userLoginPo.setPassword(userDto.getPassword());
            userLoginPo.setSource(UserLoginDto.SOURCE_WEB);
            userLoginPo.setToken(token);
            userLoginPo.setUserId(userDto.getUserId());
            userLoginPo.setUserName(userDto.getUserName());
            userLoginInnerServiceSMOImpl.saveUserLogin(userLoginPo);
            context.setResponseEntity(ResultVo.createResponseEntity(user));
        } catch (Exception e) {
            logger.error("登录异常：", e);
            throw new SMOException(ResponseConstant.RESULT_CODE_INNER_ERROR, "系统内部错误，请联系管理员");
        }
    }
}
