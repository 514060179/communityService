package com.newland.property;

import cn.hutool.http.HttpResponse;
import cn.hutool.http.HttpUtil;
import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.newland.property.utils.exception.CmdException;


public class DrzkUtil {

    private final String TOKEN_URL = "https://cloud-2018.drzk.cn/api/index/auth/token";
    private final String CREATE_MONTHLY_CAR_URL = "https://cloud-2018.drzk.cn/api/index/monthlycar/create";
    private final String RENEW_MONTHLY_CAR_URL = "https://cloud-2018.drzk.cn/api/index/monthlycar/newdate";
    private final String DELETE_MONTHLY_CAR_URL = "https://cloud-2018.drzk.cn/api/index/monthlycar/del";
    private final String DETAIL_MONTHLY_CAR_URL = "https://cloud-2018.drzk.cn/api/index/monthlycar/info";
    private final String FEE_HISTORY = "https://cloud-2018.drzk.cn/api/index/monthlycar/history";
    private final String LONG_RENTAL_RATE = "https://cloud-2018.drzk.cn/api/index/monthlycar/long-rental-rate/:parkingNo";

    private String appName;
    private String parkId;
    private String token = "4A832C8E5DDD2F30DB0D0F4C2E6067DCDBEE3480C72D1884B0FFFCB7BC311D07D7CC89B966A487CF9FA55F9DBD30562D69DCA19C1FAF53172F591087C2715046E377EE36300AD7DE4F6302E490CCDB81";

    public DrzkUtil(String appName, String parkId) {
        this.appName = appName;
        this.parkId = parkId;
        this.token = getToken();  // 获取并存储 token
    }
    /**
     * 获取token
     * @return
     */
    public String getToken() {
        // 构造请求参数
        JSONObject jsonParams = JSONUtil.createObj();
        jsonParams.put("appName", appName);
        jsonParams.put("parkId", parkId);

        // 发送 POST 请求
        HttpResponse response = HttpUtil.createPost(TOKEN_URL)
                .header("Content-Type", "application/json")
                .body(jsonParams.toString())
                .execute();

        // 解析响应
        String body = response.body();
        JSONObject jsonResponse = JSONUtil.parseObj(body);

        if (jsonResponse.getInt("status") == 1) {
            return jsonResponse.getStr("data");
        } else {
            throw new RuntimeException("Failed to get token: " + jsonResponse.getStr("msg"));
        }
    }

    /**
     * 月租车开户
     * @return
     */
    public JSONObject createMonthlyCar(String carNo, int cardTypeId, String startTime, String endTime,
                                       String balanceMoney, int payType, String contactName, String concatPhone,
                                       String puId, String planName, JSONArray subParkId) {

        JSONObject jsonParams = JSONUtil.createObj();
        jsonParams.put("parkId", parkId);
        jsonParams.put("carNo", carNo);
        jsonParams.put("cardTypeId", cardTypeId);
        jsonParams.put("startTime", startTime);
        jsonParams.put("endTime", endTime);
        jsonParams.put("balanceMoney", balanceMoney);
        jsonParams.put("payType", payType);
        jsonParams.put("contactName", contactName);
        jsonParams.put("concatPhone", concatPhone);

        // 发送 POST 请求
        HttpResponse response = HttpUtil.createPost(CREATE_MONTHLY_CAR_URL)
                .header("Content-Type", "application/json")
                .header("token", token)  // 添加 token 到请求头
                .header("Referer","139.9.36.54")
                .body(jsonParams.toString())
                .execute();

        // 解析响应
        String body = response.body();
        JSONObject jsonResponse = JSONUtil.parseObj(body);

        JSONObject result = new JSONObject();
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
     * 销户
     * @return
     */
    public JSONObject deleteMonthlyCar(String carNo) {
        // 构造请求参数
        JSONObject jsonParams = JSONUtil.createObj();
        jsonParams.put("parkId", parkId);
        jsonParams.put("carNo", carNo);
        // 发送 POST 请求
        HttpResponse response = HttpUtil.createPost(DELETE_MONTHLY_CAR_URL)
                .header("Content-Type", "application/json")
                .header("Referer","139.9.36.54")
                .header("token", token)
                .body(jsonParams.toString())
                .execute();

        // 解析响应
        String body = response.body();
        JSONObject jsonResponse = JSONUtil.parseObj(body);

        JSONObject result = new JSONObject();
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
    public JSONObject detailMonthlyCar(String carNo) {
        // 构造请求参数
        JSONObject jsonParams = JSONUtil.createObj();
        jsonParams.put("parkId", parkId);
        jsonParams.put("carNo", carNo);
        // 发送 POST 请求
        HttpResponse response = HttpUtil.createPost(DETAIL_MONTHLY_CAR_URL)
                .header("Content-Type", "application/json")
                .header("Referer","139.9.36.54")
                .header("token", token)
                .body(jsonParams.toString())
                .execute();

        // 解析响应
        String body = response.body();
        JSONObject jsonResponse = JSONUtil.parseObj(body);

        JSONObject result = new JSONObject();
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
    public JSONObject renewMonthlyCar( String carNo, String newStartTime, String newEndTime, String balanceMoney, Integer payType) {
        // 构造请求参数
        JSONObject jsonParams = JSONUtil.createObj();
        jsonParams.put("parkId", parkId);
        jsonParams.put("carNo",  carNo);
        jsonParams.put("newStartTime", newStartTime);
        jsonParams.put("newEndTime", newEndTime);
        jsonParams.put("balanceMoney", balanceMoney);
        jsonParams.put("payType", balanceMoney);

        // 发送 POST 请求
        HttpResponse response = HttpUtil.createPost(RENEW_MONTHLY_CAR_URL)
                .header("Content-Type", "application/json")
                .header("Referer","139.9.36.54")
                .header("token", token)
                .body(jsonParams.toString())
                .execute();

        // 解析响应
        String body = response.body();
        JSONObject jsonResponse = JSONUtil.parseObj(body);

        JSONObject result = new JSONObject();
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
     * @return
     */
    public JSONObject feeHistory(String carNo) {
        // 构造请求参数
        JSONObject jsonParams = JSONUtil.createObj();
        jsonParams.put("parkId", parkId);
        jsonParams.put("carNo", carNo);
        // 发送 POST 请求
        HttpResponse response = HttpUtil.createPost(FEE_HISTORY)
                .header("Content-Type", "application/json")
                .header("Referer","139.9.36.54")
                .header("token", token)
                .body(jsonParams.toString())
                .execute();

        // 解析响应
        String body = response.body();
        JSONObject jsonResponse = JSONUtil.parseObj(body);

        JSONObject result = new JSONObject();
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
    public JSONObject longRentalRate() {
        // 构造请求参数
        JSONObject jsonParams = JSONUtil.createObj();
        jsonParams.put("parkId", parkId);
        // 发送 POST 请求
        HttpResponse response = HttpUtil.createGet(LONG_RENTAL_RATE.replace(":parkingNo", parkId))
                .header("Referer","139.9.36.54")
                .header("token", token)
                .execute();
        // 解析响应
        String body = response.body();
        JSONObject jsonResponse = JSONUtil.parseObj(body);

        JSONObject result = new JSONObject();
        if (jsonResponse.getJSONObject("head").getInt("status") == 1) {
            result.put("status", 1);
            result.put("list", jsonResponse.getJSONArray("body"));
        } else {
            result.put("status", 0);
            result.put("message", jsonResponse.getJSONObject("head").getStr("message"));
        }
        return result;
    }

    public static void main(String[] args) {
        try {
            DrzkUtil drzkUtil = new DrzkUtil("9015e3545dae4044848be00ce28fcbbe", "X51810900025");
//            JSONObject result = drzkUtil.createMonthlyCar(
//                    "粤J9988A", 11, "2024-05-01 00:00:00", "2025-05-01 00:00:00",
//                    "5000", 5, "张三", "13800138000", null, null, null);
//            JSONObject result = drzkUtil.renewMonthlyCar(
//                    "粤J9988A",  "2024-05-01 00:00:00", "2025-05-01 00:00:00",
//                    "5000", 5);
//            System.out.println("Result: " + result);
            JSONObject result = drzkUtil.feeHistory("粤J5950Z");
            System.out.println(result.getJSONArray("list"));
//            JSONObject result = drzkUtil.longRentalRate();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
