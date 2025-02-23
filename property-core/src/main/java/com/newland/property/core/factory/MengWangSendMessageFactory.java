package com.newland.property.core.factory;

import com.newland.property.core.log.LoggerFactory;
import com.newland.property.utils.cache.MappingCache;
import com.newland.property.utils.util.DateUtils;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;

import java.net.URLDecoder;
import java.security.MessageDigest;
import java.util.Date;
import java.util.Random;

import com.newland.property.utils.httpUtil.HttpRequest;

/**
 * 梦网发送短信
 */
public class MengWangSendMessageFactory {

    private final static Logger logger = LoggerFactory.getLogger(MengWangSendMessageFactory.class);

    public final static int DEFAULT_MESSAGE_CODE_LENGTH = 6;

    public final static String MENGWANG_SMS_DOMAIN = "MENGWANG_SMS";

    public final static String MENGWANG_SMS_USERID = "userid";

    public final static String MENGWANG_SMS_PASSWORD = "password";

    public final static String MENGWANG_SMS_FIXED_STRING = "fixedString";

    public final static String MENGWANG_SMS_SENDER_ID = "senderId";

    public final static String MENGWANG_SMS_HOST_URLS = "hostUrls";

    public final static String MENGWANG_SMS_TIMEOUT_SECONDS = "timeoutSeconds";

    public final static String MENGWANG_SMS_API_PATH = "apiPath";

    public final static String MENGWANG_SMS_SIGN_NAME = "signName";

    /**
     * 生成6位短信码
     *
     * @return
     */
    public static String generateMessageCode() {
        return generateMessageCode(DEFAULT_MESSAGE_CODE_LENGTH);
    }

    /**
     * 生成验证码
     *
     * @param limit 位数
     * @return
     */
    public static String generateMessageCode(int limit) {
        Random random = new Random();
        String result = "";
        for (int i = 0; i < limit; i++) {
            result += (random.nextInt(9) + 1);
        }
        return result;
    }

    public static void sendMessage(String areaCode, String tel, String code) {
        try {
            //开始发送验证码
            logger.debug("发送号码区号为{},发送号码为{}，短信码为{}", areaCode, tel, code);

            String userId = MappingCache.getValue(MENGWANG_SMS_DOMAIN, MENGWANG_SMS_USERID);
            String timestamp = DateUtils.getDateStr(new Date(), "MMddHHmmss");
            String fixedString = MappingCache.getValue(MENGWANG_SMS_DOMAIN, MENGWANG_SMS_FIXED_STRING);
            String password = MappingCache.getValue(MENGWANG_SMS_DOMAIN, MENGWANG_SMS_PASSWORD);
            String senderId = MappingCache.getValue(MENGWANG_SMS_DOMAIN, MENGWANG_SMS_SENDER_ID);
            String svrtype = "1";
            String signName = MappingCache.getValue(MENGWANG_SMS_DOMAIN, MENGWANG_SMS_SIGN_NAME);
            if (StringUtils.isEmpty(signName)) {
                signName = "動脈";
            }

            String phone = "";
            if (tel.length() == 8) {
                phone = "853" + tel;
            } else {
                phone = tel;
            }

            String pwdStr = userId + fixedString + password + timestamp;
            MessageDigest digest = MessageDigest.getInstance("MD5");
            digest.update(pwdStr.getBytes());

            String content = String.format("您的驗證碼%s，有效期10分鐘。本條為官方信息，注意防範詐騙。", code);
            content = "【" + signName + "】" + content;

            byte[] bytes = digest.digest();
            StringBuffer buf = new StringBuffer(bytes.length * 2);
            for (int i = 0; i < bytes.length; i++) {
                if ((bytes[i] & 0xff) < 16) {
                    buf.append("0");
                }
                buf.append(Long.toString(bytes[i] & 0xff, 16));
            }
            String pwd = buf.toString();

            com.alibaba.fastjson.JSONObject dataBody = new com.alibaba.fastjson.JSONObject();
            dataBody.put("userid", userId);
            dataBody.put("pwd", pwd);
            dataBody.put("mobile", phone);
            dataBody.put("content", content);
            dataBody.put("timestamp", timestamp);
            dataBody.put("svrtype", svrtype);
            dataBody.put("exno", senderId);
            dataBody.put("custid", String.valueOf(System.currentTimeMillis()));
            dataBody.put("exdata", String.valueOf(System.currentTimeMillis()));

            int timeoutSeconds = 3;
            String timeoutSecondsStr = MappingCache.getValue(MENGWANG_SMS_DOMAIN, MENGWANG_SMS_TIMEOUT_SECONDS);
            if (StringUtils.isNotEmpty(timeoutSecondsStr)) {
                timeoutSeconds = Integer.parseInt(timeoutSecondsStr);
            }

            String apiPath = MappingCache.getValue(MENGWANG_SMS_DOMAIN, MENGWANG_SMS_API_PATH);
            String[] hostUrls = MappingCache.getValue(MENGWANG_SMS_DOMAIN, MENGWANG_SMS_HOST_URLS).split(",");

            logger.info("sendBody: " + dataBody.toJSONString());

            // 默认发国内服务器主用，失败再发主用，再失败发备用
            for (String hostUrl : hostUrls) {
                String apiUrl = hostUrl + apiPath;
                String result = HttpRequest.sendJsonPost(apiUrl, null, dataBody.toJSONString(), timeoutSeconds);

                logger.info("api url: " + apiUrl + ", result: " + result);

                if (StringUtils.isNotEmpty(result)) {
                    com.alibaba.fastjson.JSONObject resultJSONObject = com.alibaba.fastjson.JSONObject.parseObject(result);
                    if (resultJSONObject.getInteger("result") == 0) {
                        return;
                    } else {
//                    logDTO.setStatus(3);
//                    logDTO.setReason(URLDecoder.decode(resultJSONObject.getString("desc"), "UTF-8"));
                    }
                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            logger.info("send meng wang sms error: " + ex.getMessage());
        }
    }
}
