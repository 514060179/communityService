package com.newland.property.boot.smo.login.impl;

import com.alibaba.fastjson.JSONObject;
import com.newland.property.boot.smo.DefaultAbstractComponentSMO;
import com.newland.property.boot.smo.login.IAdminLoginPropertyAccountServiceSMO;
import com.newland.property.core.cache.NewlandPropertyRedisConfig;
import com.newland.property.core.context.IPageData;
import com.newland.property.core.factory.AuthenticationFactory;
import com.newland.property.core.log.LoggerFactory;
import com.newland.property.utils.cache.CommonCache;
import com.newland.property.utils.util.Assert;
import com.newland.property.utils.util.StringUtil;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;


/**
 * 登录信息实现类
 * Created by wuxw on 2019/3/20.
 */

@Service("adminLoginPropertyAccountServiceSMOImpl")
public class AdminLoginPropertyAccountServiceSMOImpl extends DefaultAbstractComponentSMO implements IAdminLoginPropertyAccountServiceSMO {
    private final static Logger logger = LoggerFactory.getLogger(AdminLoginPropertyAccountServiceSMOImpl.class);

    @Autowired
    private RestTemplate restTemplate;


    /**
     * 登录处理
     *
     * @param pd 页面请求对象
     * @return
     */
    @Override
    public ResponseEntity<String> doLogin(IPageData pd) {

        ResponseEntity<String> responseEntity = null;
        JSONObject loginInfo = JSONObject.parseObject(pd.getReqData());
        validate(loginInfo);
        loginInfo.put("curPasswd", AuthenticationFactory.passwdMd5(loginInfo.getString("curPasswd")));
        responseEntity = this.callCenterService(restTemplate, pd, loginInfo.toJSONString(), "login.adminLoginProperty", HttpMethod.POST);
        if (responseEntity.getStatusCode() == HttpStatus.OK) {
            JSONObject userInfo = JSONObject.parseObject(responseEntity.getBody());
            pd.setToken(userInfo.getString("token"));
            clearUserCache(userInfo);
        }
        return responseEntity;
    }

    /**
     * 校验验证码
     *
     * @param reqJson 页面请求对象
     * @return
     */
    public void validate(JSONObject reqJson) {

        Assert.hasKeyAndValue(reqJson, "username", "未包含需要登录的用户名");
        Assert.hasKeyAndValue(reqJson, "userId", "未包含需要登录的用户ID");
        Assert.hasKeyAndValue(reqJson, "curPasswd", "未包含当前用户的密码");
        Assert.hasKeyAndValue(reqJson, "curUserName", "未包含当前用户的用户名");

    }

    /**
     * 清理用户缓存
     *
     * @param userInfo
     */
    private void clearUserCache(JSONObject userInfo) {
        //员工商户缓存 getStoreInfo
        String storeId = "";

        String storeInfo = CommonCache.getValue("getStoreInfo" + NewlandPropertyRedisConfig.GET_STORE_INFO_EXPIRE_TIME_KEY + "::" + userInfo.getString("userId"));
        if (!StringUtil.isEmpty(storeInfo)) {
            CommonCache.removeValue("getStoreInfo" + NewlandPropertyRedisConfig.GET_STORE_INFO_EXPIRE_TIME_KEY + "::" + userInfo.getString("userId"));
            JSONObject storeObj = JSONObject.parseObject(storeInfo);
            storeId = storeObj.getJSONObject("msg").getString("storeId");
            CommonCache.removeValue("getStoreEnterCommunitys" + NewlandPropertyRedisConfig.GET_STORE_ENTER_COMMUNITYS_EXPIRE_TIME_KEY + "::" + storeId);
        }
        //员工权限
        CommonCache.removeValue("getUserPrivileges" + NewlandPropertyRedisConfig.DEFAULT_EXPIRE_TIME_KEY + "::" + userInfo.getString("userId"));
    }


    public RestTemplate getRestTemplate() {
        return restTemplate;
    }

    public void setRestTemplate(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }
}
