package com.newland.property.job.adapt.hcIotNew.http;

import com.alibaba.fastjson.JSONObject;
import com.newland.property.core.client.RestTemplate;
import com.newland.property.core.factory.AuthenticationFactory;
import com.newland.property.core.factory.GenerateCodeFactory;
import com.newland.property.dto.machine.MachineTranslateErrorDto;
import com.newland.property.intf.common.IMachineTranslateErrorInnerServiceSMO;
import com.newland.property.job.adapt.hcIot.IotConstant;
import com.newland.property.po.machine.MachineTranslateErrorPo;
import com.newland.property.utils.cache.CommonCache;
import com.newland.property.utils.cache.MappingCache;
import com.newland.property.utils.constant.CommonConstant;
import com.newland.property.utils.util.DateUtil;
import com.newland.property.utils.util.StringUtil;
import com.newland.property.vo.ResultVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.UUID;

@Service
public class SendIotImpl implements ISendIot {

    public static final String GET_TOKEN_URL = "/iot/api/login.pcUserLogin";
    private static final String DEFAULT_IOT_URL = "https://things.homecommunity.cn";
    public static final String IOT_DOMAIN = "IOT"; // 物联网域


    @Autowired
    private RestTemplate outRestTemplate;

    @Autowired
    private IMachineTranslateErrorInnerServiceSMO machineTranslateErrorInnerServiceSMOImpl;


    @Override
    public ResultVo get(String url) {
        HttpHeaders header = getHeaders(url, "", HttpMethod.POST);
        HttpEntity<String> httpEntity = new HttpEntity<String>("", header);
        ResponseEntity<String> tokenRes = outRestTemplate.exchange(url, HttpMethod.POST, httpEntity, String.class);

        String body = tokenRes.getBody();
        JSONObject paramOut = JSONObject.parseObject(body);

        return new ResultVo(paramOut.getIntValue("code"), paramOut.getString("msg"), paramOut.get("data"));
    }

    @Override
    public ResultVo post(String url, JSONObject paramIn) {
        url = getUrl(url);
        HttpHeaders header = getHeaders(url, paramIn.toJSONString(), HttpMethod.POST);
        HttpEntity<String> httpEntity = new HttpEntity<String>(paramIn.toJSONString(), header);
        ResponseEntity<String> tokenRes = outRestTemplate.exchange(url, HttpMethod.POST, httpEntity, String.class);

        String body = tokenRes.getBody();
        JSONObject paramOut = JSONObject.parseObject(body);

        if(paramOut.getIntValue("code") != ResultVo.CODE_OK){
            saveTranslateError(paramIn.getString("communityId"),paramIn.toJSONString(),body,url);
        }

        int total =1;
        int records = 1;
        if(paramOut.containsKey("total")){
            total = paramOut.getIntValue("total");
        }

        if(paramOut.containsKey("records")){
            records = paramOut.getIntValue("records");
        }

        return new ResultVo(records,total,paramOut.getIntValue("code"), paramOut.getString("msg"), paramOut.get("data"));
    }



    public void saveTranslateError(String communityId,String reqJson, String resJson, String url) {
        MachineTranslateErrorPo machineTranslateErrorPo = new MachineTranslateErrorPo();
        machineTranslateErrorPo.setLogId(GenerateCodeFactory.getGeneratorId(GenerateCodeFactory.CODE_PREFIX_logId));
        machineTranslateErrorPo.setCommunityId(communityId);
        machineTranslateErrorPo.setMachineTranslateId(GenerateCodeFactory.getGeneratorId(GenerateCodeFactory.CODE_PREFIX_machineTranslateId));
        machineTranslateErrorPo.setReqBody(reqJson);
        machineTranslateErrorPo.setReqHeader("");
        machineTranslateErrorPo.setResBody(resJson);
        machineTranslateErrorPo.setReqPath(url);
        machineTranslateErrorPo.setCommunityId("-1");
        machineTranslateErrorPo.setReqType(MachineTranslateErrorDto.REQ_TYPE_URL);
        machineTranslateErrorInnerServiceSMOImpl.saveMachineTranslateError(machineTranslateErrorPo);
    }


    /**
     * 封装头信息
     *
     * @return
     */
    private HttpHeaders getHeaders(String url, String param, HttpMethod method) {
        HttpHeaders header = new HttpHeaders();
        header.add(CommonConstant.APP_ID.toLowerCase(), MappingCache.getValue(IOT_DOMAIN, "APP_ID"));
        header.add(CommonConstant.USER_ID.toLowerCase(), CommonConstant.ORDER_DEFAULT_USER_ID);
        header.add(CommonConstant.TRANSACTION_ID.toLowerCase(), UUID.randomUUID().toString());
        header.add(CommonConstant.REQUEST_TIME.toLowerCase(), DateUtil.getDefaultFormateTimeString(new Date()));
        createSign(header, method, url, param);
        header.add("Authorization", "Bearer " + getToken(false));
        return header;
    }

    public String getToken(boolean refreshAccessToken) {
        String token = CommonCache.getValue(IotConstant.HC_TOKEN);
        if (!StringUtil.isEmpty(token) && !refreshAccessToken) {
            return token;
        }

        String url = getUrl(GET_TOKEN_URL);

        String userName = MappingCache.getValue(IOT_DOMAIN, "IOT_USERNAME");
        String password = MappingCache.getValue(IOT_DOMAIN, "IOT_PASSWORD");
        JSONObject param = new JSONObject();
        param.put("username", userName);
        param.put("passwd", password);

        HttpHeaders header = new HttpHeaders();
        header.add(CommonConstant.APP_ID.toLowerCase(), MappingCache.getValue(IOT_DOMAIN, "APP_ID"));
        header.add(CommonConstant.USER_ID.toLowerCase(), CommonConstant.ORDER_DEFAULT_USER_ID);
        header.add(CommonConstant.TRANSACTION_ID.toLowerCase(), UUID.randomUUID().toString());
        header.add(CommonConstant.REQUEST_TIME.toLowerCase(), DateUtil.getDefaultFormateTimeString(new Date()));
        createSign(header, HttpMethod.POST, url, param.toJSONString());
        HttpEntity<String> httpEntity = new HttpEntity<String>(param.toJSONString(), header);

        ResponseEntity<String> tokenRes = outRestTemplate.exchange(url, HttpMethod.POST, httpEntity, String.class);


        JSONObject tokenObj = JSONObject.parseObject(tokenRes.getBody());
        if (tokenObj.getIntValue("code") != 0) {
            throw new IllegalArgumentException("获取token失败" + tokenRes.getBody());
        }
        if (!tokenObj.containsKey("token")) {
            throw new IllegalArgumentException("获取token失败" + tokenRes.getBody());
        }

        token = tokenObj.getString("token");
        int expiresIn = 30 * 60; //todo 30分钟

        CommonCache.setValue(IotConstant.HC_TOKEN, token, expiresIn - 200);

        return token;
    }

    private static String getUrl(String param) {
        String url = MappingCache.getValue(IOT_DOMAIN, IotConstant.IOT_URL);

        if (StringUtil.isEmpty(url)) {
            return DEFAULT_IOT_URL + param;
        }

        return url + param;
    }

    /**
     * 创建鉴权
     *
     * @param headers
     * @param httpMethod
     * @param url
     * @param param
     */
    public static void createSign(HttpHeaders headers, HttpMethod httpMethod, String url, String param) {
        String appId = headers.getFirst(CommonConstant.APP_ID);
        String transactionId = headers.getFirst(CommonConstant.TRANSACTION_ID);
        String requestTime = headers.getFirst(CommonConstant.REQUEST_TIME);
        String securityCode = MappingCache.getValue(IOT_DOMAIN, "APP_SECRET");

        if (StringUtil.isEmpty(securityCode)) {
            return;
        }
        String paramStr = "";
        if (HttpMethod.GET == httpMethod) {
            paramStr = url.substring(url.indexOf("?"));
        } else {
            paramStr = param;
        }
        String sign = transactionId + requestTime + appId + paramStr + securityCode;
        headers.remove("sign");
        headers.add("sign", AuthenticationFactory.md5(sign));
    }

}
