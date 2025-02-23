package com.newland.property.dto.wechat;

import com.alibaba.fastjson.JSONObject;

/**
 * @program: MicroCommunity
 * @description: 微信公众号发送物业费信息模板
 * @author: zcc
 * @create: 2020-06-16 09:41
 **/
public class PropertyFeeTemplateMessage {
    //接收者openid
    private String touser;
    //模板ID
    private String template_id;
    //模板跳转链接（海外帐号没有跳转能力）
    private String url;
    //    private String color;//模板内容字体颜色，不填默认为黑色
    //模板数据
    private JSONObject data;

    private Miniprogram miniprogram;

    public String getTouser() {
        return touser;
    }

    public void setTouser(String touser) {
        this.touser = touser;
    }

    public String getTemplate_id() {
        return template_id;
    }

    public void setTemplate_id(String template_id) {
        this.template_id = template_id;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }


    public JSONObject getData() {
        return data;
    }

    public void setData(JSONObject data) {
        this.data = data;
    }

    public Miniprogram getMiniprogram() {
        return miniprogram;
    }

    public void setMiniprogram(Miniprogram miniprogram) {
        this.miniprogram = miniprogram;
    }
}
