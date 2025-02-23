package com.newland.property.job.util;

import cn.hutool.core.date.LocalDateTimeUtil;
import cn.hutool.http.HttpResponse;
import cn.hutool.http.HttpUtil;
import cn.hutool.json.JSONUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.newland.property.core.client.OutRestTemplate;
import com.newland.property.dto.mapping.Mapping;
import com.newland.property.dto.owner.OwnerCarDto;
import com.newland.property.utils.cache.CommonCache;
import com.newland.property.utils.cache.MappingCache;
import com.newland.property.utils.constant.DooRConstant;
import com.newland.property.utils.exception.CmdException;
import com.newland.property.utils.factory.ApplicationContextFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;

import java.time.ZoneId;

/**
 * @author simon feng
 * @date 2024/6/3 11:39
 * @description 道尔道闸工具类
 */
public class DoorUtil {

    private static Logger logger = LoggerFactory.getLogger(DoorUtil.class);

    public static String getToken(String[] split) {
        String key = split[0];
        String value = CommonCache.getValue(DooRConstant.REDIS_PREFIX_TOKEN + key);
        if (value != null && !"".equals(value)) {
            return value;
        }
        Mapping mapping = MappingCache.getMapping(DooRConstant.DOOR_DOMAIN, DooRConstant.TOKEN_URL);
        String tokenUrl = mapping.getValue();
        // 获取入场数据，并同步到库中
        JSONObject request = new JSONObject();
        request.put("parkId", split[0]);
        request.put("appName", split[1]);
        HttpHeaders headers = new HttpHeaders();
        headers.add("Referer", "139.9.36.54");
        headers.add("Content-Type", "application/json");
        HttpEntity httpEntity = new HttpEntity(request.toString(), headers);
        OutRestTemplate outRestTemplate = ApplicationContextFactory.getBean("outRestTemplate", OutRestTemplate.class);
        ResponseEntity<String> responseEntity = outRestTemplate.newExchange(tokenUrl, HttpMethod.POST, httpEntity, String.class);
        String body = responseEntity.getBody();
        JSONObject responseJson = JSON.parseObject(body);
        if (responseJson.getInteger("status") == 1) {
            String data = responseJson.getString("data");
            CommonCache.setValue(DooRConstant.REDIS_PREFIX_TOKEN + key, data, 2 * 60 * 60);
            return data;
        } else {
            logger.error("获取token失败");
        }
        return null;
    }





    /**
     * 验证传入的第三方停车🐴编码 用-分开
     * @param thirdAreaNum
     */
    public static void validateThirdAreaNum(String thirdAreaNum){
        if(thirdAreaNum.split("-").length != 2){
            throw new IllegalArgumentException("停车场外部编码错误");
        }
    }

    /**
     * 本地类型转换为道尔类型
     * @param leaseType
     * @return
     */
    public static int getCardTypeIdFromLeaseType(String leaseType){
        if (leaseType == null) {
            throw new IllegalArgumentException("leaseType cannot be null");
        }
        //11~18:月租车A~H,41~42:免费车A~B,51~58:储值车A~H
        switch (leaseType) {
            case "H":
                return 11;
            case "NM":
                return 41;
            default:
                throw new IllegalArgumentException("Unknown leaseType: " + leaseType);
        }
    }



    /**
     * 月租车开户
     * @return
     */
    public static cn.hutool.json.JSONObject createMonthlyCar(String thirdAreaNum, OwnerCarDto newInfo) {
        cn.hutool.json.JSONObject result = new cn.hutool.json.JSONObject();
        validateThirdAreaNum(thirdAreaNum);
        //判断是否已经开户
        cn.hutool.json.JSONObject existJson = detailMonthlyCar(thirdAreaNum, newInfo.getCarNum());
        if(existJson.getInt("status") == 1){
            return existJson;
        }
        cn.hutool.json.JSONObject jsonParams = JSONUtil.createObj();
        jsonParams.put("parkId", thirdAreaNum.split("-")[0]);
        jsonParams.put("carNo", newInfo.getCarNum());
        //lease_type和cardTypeId转换
        jsonParams.put("cardTypeId", getCardTypeIdFromLeaseType(newInfo.getLeaseType())); //`lease_type` varchar(12) NOT NULL DEFAULT 'H' COMMENT '租赁类型，H 月租车 S 出售车 I 内部车 NM 免费车',
        jsonParams.put("startTime", LocalDateTimeUtil.format(newInfo.getStartTime().toInstant()
                .atZone(ZoneId.systemDefault())
                .toLocalDateTime(), "yyyy-MM-dd HH:mm:ss"));
        jsonParams.put("endTime", LocalDateTimeUtil.format(newInfo.getEndTime().toInstant()
                .atZone(ZoneId.systemDefault())
                .toLocalDateTime(), "yyyy-MM-dd HH:mm:ss"));
        jsonParams.put("balanceMoney", newInfo.getBalanceMoney());
        jsonParams.put("payType", newInfo.getPayType());
        jsonParams.put("contactName", newInfo.getOwnerName());
        jsonParams.put("concatPhone", newInfo.getLink());
        Mapping mapping = MappingCache.getMapping(DooRConstant.DOOR_DOMAIN, DooRConstant.CREATE_MONTHLY_CAR_URL);
        // 发送 POST 请求
        HttpResponse response = HttpUtil.createPost(mapping.getValue())
                .header("Content-Type", "application/json")
                .header("token", DoorUtil.getToken(thirdAreaNum.split("-")))  // 添加 token 到请求头
                .header("Referer","139.9.36.54")
                .body(jsonParams.toString())
                .execute();

        // 解析响应
        String body = response.body();
        cn.hutool.json.JSONObject jsonResponse = JSONUtil.parseObj(body);

        if (jsonResponse.getJSONObject("head").getInt("status") == 1) {
            result.put("status", 1);
            result.put("carNo", jsonResponse.getJSONObject("body").getStr("carNo"));
        } else {
            System.out.println(jsonResponse.getJSONObject("head").getStr("message"));
            throw new CmdException("開戶失敗");
        }
        return result;
    }

    /**
     * 销户
     * @return
     */
    public static cn.hutool.json.JSONObject deleteMonthlyCar(String thirdAreaNum, String carNo) {
        validateThirdAreaNum(thirdAreaNum);
        // 构造请求参数
        cn.hutool.json.JSONObject jsonParams = JSONUtil.createObj();
        jsonParams.put("parkId", thirdAreaNum.split("-")[0]);
        jsonParams.put("carNo", carNo);
        Mapping mapping = MappingCache.getMapping(DooRConstant.DOOR_DOMAIN, DooRConstant.DELETE_MONTHLY_CAR_URL);
        // 发送 POST 请求
        HttpResponse response = HttpUtil.createPost(mapping.getValue())
                .header("Content-Type", "application/json")
                .header("Referer","139.9.36.54")
                .header("token", DoorUtil.getToken(thirdAreaNum.split("-")))
                .body(jsonParams.toString())
                .execute();

        // 解析响应
        String body = response.body();
        cn.hutool.json.JSONObject jsonResponse = JSONUtil.parseObj(body);

        cn.hutool.json.JSONObject result = new cn.hutool.json.JSONObject();
        if (jsonResponse.getJSONObject("head").getInt("status") == 1) {
            result.put("status", 1);
            result.put("carNo", jsonResponse.getJSONObject("body").getStr("carNo"));
        } else {
            result.put("status", 0);
            result.put("message", jsonResponse.getJSONObject("head").getStr("message"));
        }
        return result;
    }

    /**
     * 详情
     * @return
     */
    public static cn.hutool.json.JSONObject detailMonthlyCar(String thirdAreaNum, String carNo) {
        validateThirdAreaNum(thirdAreaNum);
        // 构造请求参数
        cn.hutool.json.JSONObject jsonParams = JSONUtil.createObj();
        jsonParams.put("parkId", thirdAreaNum.split("-")[0]);
        jsonParams.put("carNo", carNo);
        Mapping mapping = MappingCache.getMapping(DooRConstant.DOOR_DOMAIN, DooRConstant.DETAIL_MONTHLY_CAR_URL);
        // 发送 POST 请求
        HttpResponse response = HttpUtil.createPost(mapping.getValue())
                .header("Content-Type", "application/json")
                .header("Referer","139.9.36.54")
                .header("token", DoorUtil.getToken(thirdAreaNum.split("-")))
                .body(jsonParams.toString())
                .execute();

        // 解析响应
        String body = response.body();
        cn.hutool.json.JSONObject jsonResponse = JSONUtil.parseObj(body);

        cn.hutool.json.JSONObject result = new cn.hutool.json.JSONObject();
        if (jsonResponse.getJSONObject("head").getInt("status") == 1) {
            result.put("status", 1);
            result.put("carNo", jsonResponse.getJSONObject("body").getStr("carNo"));
        } else {
            result.put("status", 0);
            result.put("message", jsonResponse.getJSONObject("head").getStr("message"));
        }
        return result;
    }

    /**
     * 续费
     * @return
     */
    public static cn.hutool.json.JSONObject renewMonthlyCar(String thirdAreaNum, String carNo, String newStartTime, String newEndTime, String balanceMoney, Integer payType) {
        validateThirdAreaNum(thirdAreaNum);
        // 构造请求参数
        cn.hutool.json.JSONObject jsonParams = JSONUtil.createObj();
        jsonParams.put("parkId", thirdAreaNum.split("-")[0]);
        jsonParams.put("carNo",  carNo);
        jsonParams.put("newStartTime", newStartTime);
        jsonParams.put("newEndTime", newEndTime);
        jsonParams.put("balanceMoney", balanceMoney);
        jsonParams.put("payType", payType);
        Mapping mapping = MappingCache.getMapping(DooRConstant.DOOR_DOMAIN, DooRConstant.RENEW_MONTHLY_CAR_URL);
        // 发送 POST 请求
        HttpResponse response = HttpUtil.createPost(mapping.getValue())
                .header("Content-Type", "application/json")
                .header("Referer","139.9.36.54")
                .header("token", DoorUtil.getToken(thirdAreaNum.split("-")))
                .body(jsonParams.toString())
                .execute();

        // 解析响应
        String body = response.body();
        cn.hutool.json.JSONObject jsonResponse = JSONUtil.parseObj(body);

        cn.hutool.json.JSONObject result = new cn.hutool.json.JSONObject();
        if (jsonResponse.getJSONObject("head").getInt("status") == 1) {
            result.put("status", 1);
            result.put("carNo", jsonResponse.getJSONObject("body").getStr("carNo"));
        } else {
            throw new CmdException("續費失敗");
        }
        return result;
    }

    /**
     * 缴费历史
     * @param carNo
     * @return
     */
    public static cn.hutool.json.JSONObject feeHistory(String thirdAreaNum, String carNo) {
        validateThirdAreaNum(thirdAreaNum);
        // 构造请求参数
        cn.hutool.json.JSONObject jsonParams = JSONUtil.createObj();
        jsonParams.put("parkId", thirdAreaNum.split("-")[0]);
        jsonParams.put("carNo", carNo);
        // 发送 POST 请求
        Mapping mapping = MappingCache.getMapping(DooRConstant.DOOR_DOMAIN, DooRConstant.FEE_HISTORY_URL);
        HttpResponse response = HttpUtil.createPost(mapping.getValue())
                .header("Content-Type", "application/json")
                .header("Referer","139.9.36.54")
                .header("token", DoorUtil.getToken(thirdAreaNum.split("-")))
                .body(jsonParams.toString())
                .execute();

        // 解析响应
        String body = response.body();
        cn.hutool.json.JSONObject jsonResponse = JSONUtil.parseObj(body);

        cn.hutool.json.JSONObject result = new cn.hutool.json.JSONObject();
        if (jsonResponse.getJSONObject("head").getInt("status") == 1) {
            result.put("status", 1);
            result.put("list", jsonResponse.getJSONArray("body"));
        } else {
            result.put("status", 0);
            result.put("message", jsonResponse.getJSONObject("head").getStr("message"));
        }
        return result;
    }

    /**
     * 套餐费用列表
     * @return
     */
    public static cn.hutool.json.JSONObject longRentalRate(String thirdAreaNum) {
        validateThirdAreaNum(thirdAreaNum);
        // 构造请求参数
        validateThirdAreaNum(thirdAreaNum);
        cn.hutool.json.JSONObject jsonParams = JSONUtil.createObj();
        jsonParams.put("parkId", thirdAreaNum.split("-")[0]);
        // 发送 POST 请求
        Mapping mapping = MappingCache.getMapping(DooRConstant.DOOR_DOMAIN, DooRConstant.FEE_HISTORY_URL);
        HttpResponse response = HttpUtil.createGet(mapping.getValue().replace(":parkingNo", thirdAreaNum.split("-")[0]))
                .header("Referer","139.9.36.54")
                .header("token", DoorUtil.getToken(thirdAreaNum.split("-")))
                .execute();
        // 解析响应
        String body = response.body();
        cn.hutool.json.JSONObject jsonResponse = JSONUtil.parseObj(body);

        cn.hutool.json.JSONObject result = new cn.hutool.json.JSONObject();
        if (jsonResponse.getJSONObject("head").getInt("status") == 1) {
            result.put("status", 1);
            result.put("list", jsonResponse.getJSONArray("body"));
        } else {
            result.put("status", 0);
            result.put("message", jsonResponse.getJSONObject("head").getStr("message"));
        }
        return result;
    }

}
