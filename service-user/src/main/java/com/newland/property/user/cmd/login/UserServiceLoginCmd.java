package com.newland.property.user.cmd.login;

import com.alibaba.fastjson.JSONObject;
import com.newland.property.core.annotation.NewlandPropertyCmd;
import com.newland.property.core.context.ICmdDataFlowContext;
import com.newland.property.core.event.cmd.Cmd;
import com.newland.property.core.event.cmd.CmdEvent;
import com.newland.property.core.factory.AuthenticationFactory;
import com.newland.property.core.factory.GenerateCodeFactory;
import com.newland.property.core.log.LoggerFactory;
import com.newland.property.dto.store.StoreDto;
import com.newland.property.dto.store.StoreUserDto;
import com.newland.property.dto.user.UserLoginDto;
import com.newland.property.intf.store.IStoreInnerServiceSMO;
import com.newland.property.intf.user.IUserInnerServiceSMO;
import com.newland.property.intf.user.IUserLoginInnerServiceSMO;
import com.newland.property.po.user.UserLoginPo;
import com.newland.property.service.context.DataQuery;
import com.newland.property.service.smo.IQueryServiceSMO;
import com.newland.property.utils.cache.CommonCache;
import com.newland.property.utils.cache.MappingCache;
import com.newland.property.utils.constant.CommonConstant;
import com.newland.property.utils.constant.MappingConstant;
import com.newland.property.utils.constant.ResponseConstant;
import com.newland.property.utils.constant.ServiceCodeConstant;
import com.newland.property.utils.exception.CmdException;
import com.newland.property.utils.exception.SMOException;
import com.newland.property.utils.util.Assert;
import com.newland.property.utils.util.DateUtil;
import com.newland.property.utils.util.StringUtil;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@NewlandPropertyCmd(serviceCode = "user.service.login")
public class UserServiceLoginCmd extends Cmd{

    private final static Logger logger = LoggerFactory.getLogger(UserServiceLoginCmd.class);

    @Autowired
    private IQueryServiceSMO queryServiceSMOImpl;

    @Autowired
    private IUserLoginInnerServiceSMO userLoginInnerServiceSMOImpl;

    @Autowired
    private IStoreInnerServiceSMO storeInnerServiceSMOImpl;

    @Autowired
    private IUserInnerServiceSMO userInnerServiceSMOImpl;
    @Override
    public void validate(CmdEvent event, ICmdDataFlowContext context, JSONObject reqJson) throws CmdException {
        Assert.jsonObjectHaveKey(reqJson, "username", "用户登录，未包含username节点，请检查" + reqJson);
        Assert.jsonObjectHaveKey(reqJson, "passwd", "用户登录，未包含passwd节点，请检查" + reqJson);
    }

    @Override
    public void doCmd(CmdEvent event, ICmdDataFlowContext context, JSONObject reqJson) throws CmdException {

        DataQuery dataQuery = new DataQuery();
        dataQuery.setServiceCode(ServiceCodeConstant.SERVICE_CODE_QUERY_USER_LOGIN);
        JSONObject param = new JSONObject();
        param.put("userCode", reqJson.getString("username"));
        param.put("pwd", reqJson.getString("passwd"));
        param.put("levelCdTag", "1");
        dataQuery.setRequestParams(param);
        queryServiceSMOImpl.commonQueryService(dataQuery);
        ResponseEntity<String> responseEntity = dataQuery.getResponseEntity();
        if (responseEntity.getStatusCode() != HttpStatus.OK) {
            context.setResponseEntity(new ResponseEntity<>("初始化商户", HttpStatus.FORBIDDEN));
            return;
        }
        Assert.isJsonObject(responseEntity.getBody(), "调用登录查询异常,返回报文有误，不是有效的json格式 " + responseEntity.getBody());

        JSONObject resultInfo = JSONObject.parseObject(responseEntity.getBody());
        if (!resultInfo.containsKey("user") || !resultInfo.getJSONObject("user").containsKey("userPwd")
                || !resultInfo.getJSONObject("user").containsKey("userId")) {
            responseEntity = new ResponseEntity<String>("用户或密码错误", HttpStatus.UNAUTHORIZED);
            context.setResponseEntity(responseEntity);
            return;
        }

        JSONObject userInfo = resultInfo.getJSONObject("user");
        String userPwd = userInfo.getString("userPwd");
        if (!userPwd.equals(reqJson.getString("passwd"))) {
            responseEntity = new ResponseEntity<String>("密码错误", HttpStatus.UNAUTHORIZED);
            context.setResponseEntity(responseEntity);
            return;
        }


        //检查商户状态
        StoreUserDto storeUserDto = new StoreUserDto();
        storeUserDto.setUserId(userInfo.getString("userId"));
        List<StoreUserDto> storeUserDtos = storeInnerServiceSMOImpl.getStoreUserInfo(storeUserDto);

        if (storeUserDtos != null && storeUserDtos.size() > 0) {
            String state = storeUserDtos.get(0).getState();
            if ("48002".equals(state)) {
                responseEntity = new ResponseEntity<String>("当前商户限制登录，请联系管理员", HttpStatus.UNAUTHORIZED);
                context.setResponseEntity(responseEntity);
                return;
            }

            StoreDto storeDto = new StoreDto();
            storeDto.setStoreId(storeUserDtos.get(0).getStoreId());
            List<StoreDto> storeDtos = storeInnerServiceSMOImpl.getStores(storeDto);
            if (storeDtos != null && storeDtos.size() > 0) {
                userInfo.put("storeType", storeDtos.get(0).getStoreTypeCd());
            }
        }

        try {
            Map userMap = new HashMap();
            userMap.put(CommonConstant.LOGIN_USER_ID, userInfo.getString("userId"));
            userMap.put(CommonConstant.LOGIN_USER_NAME, userInfo.getString("userName"));
            String token = AuthenticationFactory.createAndSaveToken(userMap);
            userInfo.remove("userPwd");
            userInfo.put("token", token);
            //记录登录日志
            UserLoginPo userLoginPo = new UserLoginPo();
            userLoginPo.setLoginId(GenerateCodeFactory.getGeneratorId(GenerateCodeFactory.CODE_PREFIX_loginId));
            userLoginPo.setLoginTime(DateUtil.getNow(DateUtil.DATE_FORMATE_STRING_A));
            userLoginPo.setPassword(userPwd);
            userLoginPo.setSource(UserLoginDto.SOURCE_WEB);
            userLoginPo.setToken(token);
            userLoginPo.setUserId(userInfo.getString("userId"));
            userLoginPo.setUserName(userInfo.getString("userName"));
            userLoginInnerServiceSMOImpl.saveUserLogin(userLoginPo);
            responseEntity = new ResponseEntity<String>(userInfo.toJSONString(), HttpStatus.OK);

            // 保存最新token
            String key = CommonConstant.USER_LATEST_JWT_TOKEN + userInfo.getString("userId");
            CommonCache.removeValue(key);
            String expireTime = MappingCache.getValue(MappingConstant.KEY_JWT_EXPIRE_TIME);
            if (StringUtil.isNullOrNone(expireTime)) {
                expireTime = CommonConstant.DEFAULT_JWT_EXPIRE_TIME;
            }
            CommonCache.setValue(key, token, Integer.parseInt(expireTime));

            context.setResponseEntity(responseEntity);
        } catch (Exception e) {
            logger.error("登录异常：", e);
            throw new SMOException(ResponseConstant.RESULT_CODE_INNER_ERROR, "系统内部错误，请联系管理员");
        }
    }

    /**
     * 对请求报文处理
     *
     * @param paramIn
     * @return
     */
    private JSONObject refreshParamIn(String paramIn) {
        JSONObject paramObj = JSONObject.parseObject(paramIn);
        paramObj.put("userId", "-1");
        paramObj.put("levelCd", "0");

        return paramObj;
    }
}
