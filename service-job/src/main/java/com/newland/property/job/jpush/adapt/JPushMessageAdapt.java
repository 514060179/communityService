package com.newland.property.job.jpush.adapt;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.newland.property.core.log.LoggerFactory;
import com.newland.property.dto.msg.JPushMessageDto;
import com.newland.property.dto.user.UserAliasDto;
import com.newland.property.intf.user.IUserAliasInnerServiceSMO;
import com.newland.property.job.jpush.IJPushMessageAdapt;
import com.newland.property.utils.cache.MappingCache;
import com.newland.property.utils.httpUtil.HttpRequest;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

/**
 * @author Moonny
 */
@Service("jpushMessageService")
public class JPushMessageAdapt implements IJPushMessageAdapt {

    private static final Logger logger = LoggerFactory.getLogger(JPushMessageAdapt.class);

    private static final String JPUSH_DOMAIN = "JPUSH_DOMAIN";

    private static final String JPUSH_APP_KEY = "appKey";

    private static final String JPUSH_MASTER_SECRET = "masterSecret";

    private static final String JPUSH_HOST_URL = "jpushHostUrl";

    private static final String JPUSH_ALIAS_PREFIX = "aliasPrefix";

    @Autowired
    private IUserAliasInnerServiceSMO userAliasInnerServiceSMOImpl;

    @Override
    public void pushMessage(JPushMessageDto jPushMessageDto) {
        try {
            Map<String, String> headers = new HashMap<>();

            String appKey = MappingCache.getValue(JPUSH_DOMAIN, JPUSH_APP_KEY);
            String masterSecret = MappingCache.getValue(JPUSH_DOMAIN, JPUSH_MASTER_SECRET);
            String aliasPrefix = MappingCache.getValue(JPUSH_DOMAIN, JPUSH_ALIAS_PREFIX);
            String hostUrl = MappingCache.getValue(JPUSH_DOMAIN, JPUSH_HOST_URL);

            String authString  = appKey + ":" + masterSecret;
            String authBase64 = Base64.getEncoder().encodeToString(authString.getBytes("UTF-8"));
            headers.put("Authorization", "Basic " + authBase64);

            com.alibaba.fastjson.JSONObject param = new com.alibaba.fastjson.JSONObject();
            param.put("platform", "all");

            // 发给特定用户
            com.alibaba.fastjson.JSONObject audience = new com.alibaba.fastjson.JSONObject();
            JSONArray alias = new JSONArray();
            String userAlias = aliasPrefix + jPushMessageDto.getUserId();
            alias.add(userAlias);
            audience.put("alias", alias);
            param.put("audience", audience);

            com.alibaba.fastjson.JSONObject alert = new com.alibaba.fastjson.JSONObject();
            alert.put("title", jPushMessageDto.getTitle());
            alert.put("body", jPushMessageDto.getContent());

            com.alibaba.fastjson.JSONObject extras = new com.alibaba.fastjson.JSONObject();
            extras.put("pageType", jPushMessageDto.getPageType());
            extras.put("pageUrl", jPushMessageDto.getPageUrl());
            extras.put("communityId", jPushMessageDto.getCommunityId());

            com.alibaba.fastjson.JSONObject notification = new com.alibaba.fastjson.JSONObject();

            UserAliasDto userAliasDto = new UserAliasDto();
            userAliasDto.setUserId(userAlias);
            userAliasDto.setStatusCd(0);
            int aliasCount = this.userAliasInnerServiceSMOImpl.getUserAliasCount(userAliasDto);

            JSONObject ios = new JSONObject();
            ios.put("alert", alert);
            ios.put("extras", extras);
            notification.put("ios", ios);

            JSONObject android = new JSONObject();
            android.put("alert", jPushMessageDto.getContent());
            android.put("title", jPushMessageDto.getTitle());
            android.put("extras", extras);
            notification.put("android", android);

            JSONObject hmos = new JSONObject();
            hmos.put("alert", jPushMessageDto.getContent());
            hmos.put("title", jPushMessageDto.getTitle());
            hmos.put("extras", extras);
            notification.put("hmos", hmos);

            param.put("notification", notification);

            com.alibaba.fastjson.JSONObject options = new com.alibaba.fastjson.JSONObject();
            options.put("apns_production", true);
//            if (aliasPrefix.equals("prod_")) {
//                options.put("apns_production", true);
//            } else {
//                options.put("apns_production", false);
//            }

            param.put("options", options);
            logger.info("push to ios request:" + param.toJSONString());

            if(aliasCount > 0) {
                String result = HttpRequest.sendJsonPost(hostUrl, headers, param.toJSONString(), 5);
                logger.info("push to ios result: " + result);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            logger.info("push to ios error: " + ex.getMessage());
        }
    }
}
