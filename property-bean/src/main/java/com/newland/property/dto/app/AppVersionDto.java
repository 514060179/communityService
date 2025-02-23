package com.newland.property.dto.app;

import java.util.Date;

public class AppVersionDto {
    private int id;
    private String appId;
    private String platform;
    private String channel;
    private String code;
    private String name;
    private String desc;
    private String url;
    private int isForceUpdate;
    private int status;
    private Date createTime;
    private Date updateTime;
    private int isOpenTip;
    private String lang;
    private int inAppUpdate;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getAppId() {
        return appId;
    }

    public void setAppId(String appId) {
        this.appId = appId;
    }

    public String getPlatform() {
        return platform;
    }

    public void setPlatform(String platform) {
        this.platform = platform;
    }

    public String getChannel() {
        return channel;
    }

    public void setChannel(String channel) {
        this.channel = channel;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public int getIsForceUpdate() {
        return isForceUpdate;
    }

    public void setIsForceUpdate(int isForceUpdate) {
        this.isForceUpdate = isForceUpdate;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    public int getIsOpenTip() {
        return isOpenTip;
    }

    public void setIsOpenTip(int isOpenTip) {
        this.isOpenTip = isOpenTip;
    }

    public String getLang() {
        return lang;
    }

    public void setLang(String lang) {
        this.lang = lang;
    }

    public int getInAppUpdate() {
        return inAppUpdate;
    }

    public void setInAppUpdate(int inAppUpdate) {
        this.inAppUpdate = inAppUpdate;
    }
}
