package com.newland.property.common.charge.factory.kehang;

import com.alibaba.fastjson.JSONObject;
import com.newland.property.core.factory.AuthenticationFactory;
import com.newland.property.core.log.LoggerFactory;
import com.newland.property.utils.cache.MappingCache;
import com.newland.property.utils.factory.ApplicationContextFactory;
import com.newland.property.utils.util.DateUtil;
import com.newland.property.utils.util.PayUtil;
import org.slf4j.Logger;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.HttpStatusCodeException;
import org.springframework.web.client.RestTemplate;

import java.util.*;

public class KeHangChargeUtils {

    private static Logger logger = LoggerFactory.getLogger(KeHangChargeUtils.class);


    public static final String KE_HANG_DOMAIN = "KE_HANG_CHARGE";


    public static final String KE_HANG_APP_ID = "APP_ID";
    public static final String KE_HANG_APP_SECURE = "APP_SECURE";

    public static final String URL = "https://api.poseidong.com/api/gateway";



    /**
     * 请求充电桩 接口
     * <p>
     * MappingCache.getValue(DING_DING_DOMAIN, DING_DING_APP_ID),
     * MappingCache.getValue(DING_DING_DOMAIN, DING_DING_APP_SECURE
     *
     * @param api
     * @param body
     * @param httpMethod
     * @return
     * @throws Exception
     */
    public static String execute(String api, String body, HttpMethod httpMethod) throws Exception {
        RestTemplate outRestTemplate = ApplicationContextFactory.getBean("outRestTemplate", RestTemplate.class);
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add("app_id", MappingCache.getValue(KE_HANG_DOMAIN, KE_HANG_APP_ID));
        httpHeaders.add("psd_entry", DateUtil.getCurrentDate().getTime() + "");

        JSONObject bodyObj = JSONObject.parseObject(body);
        bodyObj.put("api",api);
        bodyObj.put("nonce", PayUtil.makeUUID(16));
        bodyObj.put("sign",getSign(bodyObj));

        HttpEntity httpEntity = new HttpEntity(body, httpHeaders);
        ResponseEntity<String> response = null;
        try {
            response = outRestTemplate.exchange(URL, httpMethod, httpEntity, String.class);
        } catch (HttpStatusCodeException e) {
            logger.error("请求异常", e.getResponseBodyAsString());
            throw new IllegalArgumentException(e.getResponseBodyAsString());
        }

        return response.getBody();
    }




    private static String getSign(JSONObject bodyObj) throws Exception {

        SortedMap map = new TreeMap();

        for (String key: bodyObj.keySet()) {
            String vlaue = bodyObj.getString("key");
            map.put(key, vlaue);
        }

        StringBuffer sb = new StringBuffer();
        Set es = map.entrySet();
        Iterator<?> it = es.iterator();
        while (it.hasNext()) {
            Map.Entry entry = (Map.Entry) it.next();
            String k = (String) entry.getKey();
            if (entry.getValue() != null || !"".equals(entry.getValue())) {
                String v = String.valueOf(entry.getValue());
                if (null != v && !"".equals(v) && !"sign".equals(k) && !"key".equals(k)) {
                    sb.append(k + "=" + v + "&");
                }
            }
        }
        sb.append("key=" + MappingCache.getValue(KE_HANG_DOMAIN, KE_HANG_APP_SECURE));
        String sign = AuthenticationFactory.md5(sb.toString()).toUpperCase();
        return sign;
    }
}
