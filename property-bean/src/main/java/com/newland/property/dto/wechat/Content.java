package com.newland.property.dto.wechat;

/**
 * @description: 消息内容字体颜色
 * @author: zcc
 * @create: 2020-06-16 09:46
 **/
public class Content {
    //消息内容
    private String value;
    //消息字体颜色
    private String color = "#173177";

    public Content( String value) {
       this.value = value;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }
}
