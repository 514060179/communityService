package com.newland.property.boot.smo.login.impl;

import com.alibaba.fastjson.JSONObject;
import com.newland.property.boot.smo.DefaultAbstractComponentSMO;
import com.newland.property.boot.smo.login.IPropertyAppLoginSMO;
import com.newland.property.core.context.IPageData;
import com.newland.property.core.context.PageData;
import com.newland.property.core.factory.AuthenticationFactory;
import com.newland.property.core.log.LoggerFactory;
import com.newland.property.dto.file.FileRelDto;
import com.newland.property.dto.owner.OwnerDto;
import com.newland.property.intf.common.IFileRelInnerServiceSMO;
import com.newland.property.utils.cache.MappingCache;
import com.newland.property.utils.constant.MappingConstant;
import com.newland.property.utils.exception.SMOException;
import com.newland.property.utils.util.Assert;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;

/**
 * wx登录
 */
@Service("propertyAppLoginSMOImpl")
public class PropertyAppLoginSMOImpl extends DefaultAbstractComponentSMO implements IPropertyAppLoginSMO {

    private final static Logger logger = LoggerFactory.getLogger(PropertyAppLoginSMOImpl.class);

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private IFileRelInnerServiceSMO fileRelInnerServiceSMOImpl;

    @Override
    public ResponseEntity<String> doLogin(IPageData pd) throws SMOException {
        return businessProcess(pd);
    }

    @Override
    protected void validate(IPageData pd, JSONObject paramIn) {

        //super.validatePageInfo(pd);

        Assert.hasKeyAndValue(paramIn, "username", "请求报文中未包含用户名");
        Assert.hasKeyAndValue(paramIn, "password", "请求报文中未包含密码");
        //super.checkUserHasPrivilege(pd, restTemplate, PrivilegeCodeConstant.LIST_ORG);
    }

    @Override
    protected ResponseEntity<String> doBusinessProcess(IPageData pd, JSONObject paramIn) {

        logger.debug("doLogin入参：" + paramIn.toJSONString());
        ResponseEntity<String> responseEntity;

        JSONObject loginInfo = JSONObject.parseObject(pd.getReqData());

        loginInfo.put("passwd", AuthenticationFactory.passwdMd5(loginInfo.getString("password")));
        responseEntity = this.callCenterService(restTemplate, pd, loginInfo.toJSONString(), "user.service.login", HttpMethod.POST);
        if (responseEntity.getStatusCode() != HttpStatus.OK) {
            return responseEntity;
        }

        JSONObject userInfo = JSONObject.parseObject(responseEntity.getBody());
        if (userInfo.containsKey("code") && 0 != userInfo.getIntValue("code")) {
            return responseEntity;
        }
        //根据用户查询商户信息
        String userId = userInfo.getString("userId");

        pd = PageData.newInstance().builder(userId, "", "", pd.getReqData(),
                "", "", "", "",
                pd.getAppId());
        responseEntity = super.getStoreInfo(pd, restTemplate);

        if (responseEntity.getStatusCode() != HttpStatus.OK) {
            return responseEntity;
        }

        JSONObject storeInfo = JSONObject.parseObject(responseEntity.getBody().toString());

        Assert.jsonObjectHaveKey(storeInfo, "storeId", "根据员工未查到商户信息");
        Assert.jsonObjectHaveKey(storeInfo, "storeTypeCd", "根据员工未查到商户类型信息");
        userInfo.put("storeId", storeInfo.getString("storeId"));
        userInfo.put("storeName", storeInfo.getString("name"));
        userInfo.put("storeTypeCd", storeInfo.getString("storeTypeCd"));
        //获取头像
        FileRelDto fileRelDto = new FileRelDto();
        fileRelDto.setObjId(userId);
        userInfo.put("url","");
        List<FileRelDto> fileRelDtos = fileRelInnerServiceSMOImpl.queryFileRels(fileRelDto);
        if (fileRelDtos != null && fileRelDtos.size() >= 1) {
            String imgUrl = MappingCache.getValue(MappingConstant.FILE_DOMAIN, "IMG_PATH");
            for (FileRelDto tmpFileRelDto : fileRelDtos) {
                if (tmpFileRelDto.getFileSaveName().startsWith("http")) {
                    userInfo.put("url",tmpFileRelDto.getFileSaveName());
                } else {
                    userInfo.put("url",imgUrl + tmpFileRelDto.getFileSaveName());
                }
            }
        }
        JSONObject paramOut = new JSONObject();
        paramOut.put("result", 0);

        paramOut.put("code", 0);
        paramOut.put("msg", "成功");
        paramOut.put("userInfo", userInfo);
        paramOut.put("token", userInfo.getString("token"));
        //pd.setToken(JSONObject.parseObject(responseEntity.getBody()).getString("token"));

        return new ResponseEntity<>(paramOut.toJSONString(), HttpStatus.OK);
    }

    public RestTemplate getRestTemplate() {
        return restTemplate;
    }

    public void setRestTemplate(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

}
