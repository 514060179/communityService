package com.newland.property.core.factory;

import com.aliyuncs.CommonRequest;
import com.aliyuncs.CommonResponse;
import com.aliyuncs.DefaultAcsClient;
import com.aliyuncs.IAcsClient;
import com.aliyuncs.exceptions.ClientException;
import com.aliyuncs.exceptions.ServerException;
import com.aliyuncs.http.MethodType;
import com.aliyuncs.profile.DefaultProfile;
import com.newland.property.dto.sms.SmsConfigDto;
import com.newland.property.core.log.LoggerFactory;
import com.newland.property.utils.cache.MappingCache;
import com.newland.property.vo.ResultVo;
import org.slf4j.Logger;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Random;
/*
pom.xml
<dependency>
  <groupId>com.aliyun</groupId>
  <artifactId>aliyun-java-sdk-core</artifactId>
  <version>4.0.3</version>
</dependency>
*/

/**
 * Created by wuxw on 2019/3/23.
 */
public class AliSendMessageFactory {


    private final static Logger logger = LoggerFactory.getLogger(AliSendMessageFactory.class);


    public final static int DEFAULT_MESSAGE_CODE_LENGTH = 6;

    public final static String ALI_SMS_DOMAIN = "ALI_SMS";


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

    public static void sendMessage(String tel, String code) {

        //开始发送验证码
        logger.debug("发送号码为{}，短信码为{}", tel, code);
        DefaultProfile profile = DefaultProfile.getProfile(MappingCache.getValue(ALI_SMS_DOMAIN, "region"),
                MappingCache.getValue(ALI_SMS_DOMAIN, "accessKeyId"),
                MappingCache.getValue(ALI_SMS_DOMAIN, "accessSecret"));
        IAcsClient client = new DefaultAcsClient(profile);

        CommonRequest request = new CommonRequest();
        request.setSysMethod(MethodType.POST);
        request.setSysDomain("dysmsapi.aliyuncs.com");
        request.setSysVersion("2017-05-25");
        request.setSysAction("SendSms");
        request.putQueryParameter("RegionId", MappingCache.getValue(ALI_SMS_DOMAIN, "region"));
        request.putQueryParameter("PhoneNumbers", tel);
        request.putQueryParameter("SignName", MappingCache.getValue(ALI_SMS_DOMAIN, "signName"));
        request.putQueryParameter("TemplateCode", MappingCache.getValue(ALI_SMS_DOMAIN, "TemplateCode"));
        request.putQueryParameter("TemplateParam", "{\"code\":" + code + "}");

        try {
            CommonResponse response = client.getCommonResponse(request);
            logger.debug("发送验证码信息：{}", response.getData());
            LogFactory.saveOutLog("SMS", "{\"code\":" + code + "}", new ResponseEntity(response.getData(), HttpStatus.OK));
        } catch (ServerException e) {
            e.printStackTrace();
        } catch (ClientException e) {
            e.printStackTrace();
        }
    }

    public static void sendMessage(String areaCode, String tel, String code) {
        //开始发送验证码
        logger.debug("发送号码区号为{}，发送号码为{}，短信码为{}",areaCode, tel, code);
        DefaultProfile profile = DefaultProfile.getProfile(MappingCache.getValue(ALI_SMS_DOMAIN, "region"),
                MappingCache.getValue(ALI_SMS_DOMAIN, "accessKeyId"),
                MappingCache.getValue(ALI_SMS_DOMAIN, "accessSecret"));
        IAcsClient client = new DefaultAcsClient(profile);

        CommonRequest request = new CommonRequest();
        request.setSysMethod(MethodType.POST);
        request.setSysDomain("dysmsapi.aliyuncs.com");
        request.setSysVersion("2017-05-25");
        request.setSysAction("SendSms");
        request.putQueryParameter("RegionId", MappingCache.getValue(ALI_SMS_DOMAIN, "region"));
        request.putQueryParameter("PhoneNumbers", tel);
        request.putQueryParameter("SignName", MappingCache.getValue(ALI_SMS_DOMAIN, "signName"));
        if ("86".equals(areaCode)) {
            request.putQueryParameter("TemplateCode", MappingCache.getValue(ALI_SMS_DOMAIN, "TemplateCode"));
        } else {
            request.putQueryParameter("TemplateCode", MappingCache.getValue(ALI_SMS_DOMAIN, "TemplateCode853"));
        }
        request.putQueryParameter("TemplateParam", "{\"code\":" + code + "}");

        try {
            CommonResponse response = client.getCommonResponse(request);
            logger.debug("发送验证码信息：{}", response.getData());
            LogFactory.saveOutLog("SMS", "{\"code\":" + code + "}", new ResponseEntity(response.getData(), HttpStatus.OK));
        } catch (ServerException e) {
            e.printStackTrace();
        } catch (ClientException e) {
            e.printStackTrace();
        }
    }

    public static ResultVo sendOweFeeSms(String tel, Object param, SmsConfigDto smsConfigDto) {
        //开始发送验证码
        DefaultProfile profile = DefaultProfile.getProfile(smsConfigDto.getRegion().trim(),
                smsConfigDto.getAccessKeyId().trim(),
                smsConfigDto.getAccessSecret().trim());
        IAcsClient client = new DefaultAcsClient(profile);

        CommonRequest request = new CommonRequest();
        request.setSysMethod(MethodType.POST);
        request.setSysDomain("dysmsapi.aliyuncs.com");
        request.setSysVersion("2017-05-25");
        request.setSysAction("SendSms");
        request.putQueryParameter("RegionId", smsConfigDto.getRegion().trim());
        request.putQueryParameter("PhoneNumbers", tel);
        request.putQueryParameter("SignName", smsConfigDto.getSignName().trim());
        request.putQueryParameter("TemplateCode", smsConfigDto.getTemplateCode().trim());
        request.putQueryParameter("TemplateParam", param.toString());

        try {
            CommonResponse response = client.getCommonResponse(request);
            logger.debug("发送欠费信息：{}", response.getData());
        } catch (ServerException e) {
            e.printStackTrace();
        } catch (ClientException e) {
            e.printStackTrace();
        }

        return new ResultVo(ResultVo.CODE_OK,ResultVo.MSG_OK);
    }
}
